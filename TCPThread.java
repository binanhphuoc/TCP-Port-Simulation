import java.io.IOException;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.HashMap;
import java.net.*;
import java.util.Timer;

public class TCPThread extends Thread{

    int MAX = 1024;
    LinkedList<TCPDatagram> queue = new LinkedList<TCPDatagram>();
    short sourcePort;
    short destPort;
    short firsthop;
    int ackNo;
    int seqNo = 0;
    boolean isConnected = false;


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
            if (queue.size() > 0)
            {
                TCPDatagram datagram = queue.pop();
                processDatagram(datagram);
            }
        }
    }

    public void receive(TCPDatagram tcpDatagram)
    {
        queue.add(tcpDatagram);
    }

    void send(TCPDatagram tcpDatagram)
    {
        tcpDatagram.SourcePort = sourcePort;
        tcpDatagram.DestPort = destPort;
        try {
            Socket sendingSocket = new Socket(InetAddress.getByName("localhost"), firsthop);
            DataOutputStream outstream = new DataOutputStream(sendingSocket.getOutputStream());
            byte[] buffer = new byte[4096];
            tcpDatagram.toArray(buffer);
            outstream.write(buffer,0, 4096);
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

    void processDatagram(TCPDatagram datagram) {
        if (datagram.SYN == 1 && datagram.ACK == 0 && !isConnected) {
            ackNo = datagram.SeqNo + 1;
            TCPDatagram toSend = new TCPDatagram();
            toSend.SYN = 1;
            toSend.ACK = 1;
            toSend.SeqNo = seqNo;
            send(toSend);
        }
        else if (datagram.SYN == 1 && datagram.ACK == 1 && !isConnected)
        {
            isConnected = true;
            ackNo = datagram.SeqNo +1;
            TCPDatagram toSend = new TCPDatagram();
            toSend.ACK = 1;
            toSend.AckNo = ackNo;
            send(toSend);
        }
        else if (!isConnected && datagram.AckNo == seqNo+1)
        {
            isConnected = true;
        }
    }

}
