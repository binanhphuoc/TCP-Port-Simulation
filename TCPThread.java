import java.io.IOException;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.*;
import java.util.Timer;
import java.util.Random;

public class TCPThread extends Thread{

    // Connection variables
    int MAX = 1024;
    LinkedList<TCPDatagram> inputQueue = new LinkedList<TCPDatagram>();
    int sourcePort;
    int destPort;
    int firsthop;

    // Recently received datagram
    int synNo;
    int ackNo;
    boolean isConnected = false;
    TCPDatagram currentAckDatagram;


    public TCPThread(TCPDatagram datagram, HashMap<Integer, TCPThread> threadHashMap, int firsthop)
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

    void send(TCPDatagram tcpDatagram)
    {
        tcpDatagram.SourcePort = sourcePort;
        tcpDatagram.DestPort = destPort;
        try {
            Socket sendingSocket = new Socket(InetAddress.getByName("localhost"), firsthop);
            DataOutputStream outstream = new DataOutputStream(sendingSocket.getOutputStream());
            byte[] buffer = new byte[1024];
            buffer = tcpDatagram.toArray();
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

        // Receiving SYN
        if (datagram.SYN == 1 && !isConnected) {

            // Create a SYNACK datagram
            TCPDatagram toSend = new TCPDatagram();
            toSend.SYN = 1;
            Random rand = new Random();
            toSend.SeqNo = rand.nextInt(1000) + 1;
            toSend.ACK = 1;
            toSend.AckNo = datagram.SeqNo+1;

            // Set ackNo
            // Set synNo, only used once to establish the connection
            ackNo = toSend.AckNo;
            synNo = toSend.SeqNo;

            // Send the datagram
            currentAckDatagram = toSend;
            send(toSend);
            return;
        }

        // Receive ACK for SYNACK
        if (datagram.ACK == 1 && datagram.AckNo == (synNo+1) && !isConnected) {
            // Establish the connection
            isConnected = true;
        }



        // Receive normal datagram

        if (isConnected) {
            if (datagram.SeqNo == ackNo) {
                // Create an acknowledgement for the data received
                TCPDatagram toSend = new TCPDatagram();
                toSend.ACK = 1;
                toSend.AckNo = datagram.SeqNo + datagram.dataLength;
                toSend.SeqNo = datagram.AckNo;

                // Set current ackNo
                ackNo = toSend.AckNo;

                // Echo the message
                toSend.data = datagram.data;
                toSend.dataLength = datagram.dataLength;

                // Send the datagram
                currentAckDatagram = toSend;
                send(toSend);
                return;
            }
            else
            {
                send(currentAckDatagram);
            }
        }

        System.out.println("ERROR: Connection is already shut down.");
    }

}
