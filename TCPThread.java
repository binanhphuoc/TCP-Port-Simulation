import java.io.IOException;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.*;
import java.util.Timer;

public class TCPThread extends Thread{

    // Connection variables
    int MAX = 1024;
    LinkedList<TCPDatagram> inputQueue = new LinkedList<TCPDatagram>();
    short sourcePort;
    short destPort;
    short firsthop;

    // Recently received datagram
    int ackNo;
    boolean isConnected = false;

    ArrayList<String> messages = new ArrayList<String>();
    LinkedList<TCPDatagram> outputQueue = new LinkedList<TCPDatagram>();
    TCPDatagram sentPacket;


    public TCPThread(TCPDatagram datagram, HashMap<Short, TCPThread> threadHashMap, short firsthop)
    {
        receive(datagram);
        threadHashMap.put(datagram.SourcePort, this);
        destPort = datagram.SourcePort;
        sourcePort = datagram.DestPort;
        this.firsthop = firsthop;
    }

    public void run()
    {
        while(true)
        {
            if (inputQueue.size() > 0)
            {
                TCPDatagram datagram = inputQueue.pop();
                try {
                    processDatagram(datagram);
                }
                catch(IOException io)
                {
                    System.out.println("ERROR: In run(): Cannot processDatagram()");
                }
            }
        }
    }

    public void receive(TCPDatagram tcpDatagram)
    {
        inputQueue.add(tcpDatagram);
        tcpDatagram.print();
    }

    void send()
    {
        TCPDatagram tcpDatagram = outputQueue.pop();
        sentPacket = tcpDatagram;
        tcpDatagram.SourcePort = sourcePort;
        tcpDatagram.DestPort = destPort;
        try {
            Socket sendingSocket = new Socket(InetAddress.getByName("localhost"), firsthop);
            DataOutputStream outstream = new DataOutputStream(sendingSocket.getOutputStream());
            byte[] buffer = new byte[1024];
            tcpDatagram.toArray(buffer);
            outstream.write(buffer,0, 1024);
            outstream.flush();
            outstream.close();
        }
        catch(UnknownHostException uhe)
        {
            System.out.println("ERROR: Unable to recognize the destination host.");
        }
        catch(IOException ioe)
        {
            System.out.println("ERROR: Unable to send packet.");
        }

    }

    void processDatagram(TCPDatagram datagram) throws IOException {

        // Process datagram
        if (datagram.SYN == 1 && !isConnected) {
            ackNo = datagram.SeqNo + 1;
            TCPDatagram toSend = new TCPDatagram();
            toSend.SYN = 1;
            toSend.ACK = 1;
            toSend.SeqNo = seqNo;
            outputQueue.add(toSend);
            send();
            return;
        }

        if (datagram.ACK == 1 && !isConnected)
        {
            isConnected = true;

            ackNo = datagram.SeqNo+datagram.dataLength;
            seqNo = datagram.AckNo;
            TCPDatagram toSend = new TCPDatagram();
            toSend.ACK = 1;
            toSend.AckNo = ackNo;

            toSend.getData(messages.get(0));
            messages.remove(0);
            toSend.SeqNo = seqNo;
            outputQueue.add(toSend);
            send();
            return;
        }
        else if (!isConnected && datagram.AckNo == seqNo+1)
        {
            isConnected = true;
        }

        // Send Datagram
    }

}
