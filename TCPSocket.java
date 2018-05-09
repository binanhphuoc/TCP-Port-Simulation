import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/////////////////////////////////////////////////////////////

class WaitForAck extends TimerTask
{
    public static int count = 0;
    static TCPDatagram datagram;
    static int sourcePort;
    static int destPort;
    static int firsthop;

    public WaitForAck(TCPDatagram d, int sourcePort, int destPort, int firsthop)
    {
        datagram = d;
        WaitForAck.sourcePort = sourcePort;
        WaitForAck.destPort = destPort;
        WaitForAck.firsthop = firsthop;
    }

    public void run()
    {
        count++;
        send();
    }

    void send()
    {
        datagram.SourcePort = sourcePort;
        datagram.DestPort = destPort;
        try {
            Socket sendingSocket = new Socket(InetAddress.getByName("localhost"), firsthop);
            DataOutputStream outstream = new DataOutputStream(sendingSocket.getOutputStream());
            byte[] buffer = new byte[1024];
            buffer = datagram.toArray();
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

}

/////////////////////////////////////////////////////////////

class SYNThread extends Thread {

    public boolean ackRcv = false;
    TCPDatagram sentDatagram;
    int sourcePort;
    int destPort;
    int firsthop;

    public SYNThread(TCPDatagram sentDatagram, int sourcePort, int destPort, int firsthop) {
        this.sentDatagram = sentDatagram;
        this.sourcePort = sourcePort;
        this.destPort = destPort;
        this.firsthop = firsthop;
    }

    public void run()   // Send SYN
    {
        // creating an instance of timer class
        Timer timer = new Timer();
        TimerTask task = new WaitForAck(sentDatagram, sourcePort, destPort, firsthop);
        timer.schedule(task, 0, 20000);
        System.out.println(task.scheduledExecutionTime());
        while (WaitForAck.count < 3 && !ackRcv){};
        timer.cancel();
        ackRcv = false;
        return;
    }
}


/////////////////////////////////////////////////////////////


/////////////////////////////////////////////////////////////


public class TCPSocket {

    // Connection variables
    int destPort;
    int sourcePort;
    int firsthop;

    // SYN
    SYNThread t;

    // Recently received datagram
    int seqNo;
    boolean isConnected = false;
    boolean ackRcv = false;
    TCPDatagram sentDatagram;   // replaced by a new datagram if receiving an ACK

    public TCPSocket(int SrcPort, int DestPort, int firsthop) {
        this.firsthop = firsthop;
        this.destPort = DestPort;
        this.sourcePort = SrcPort;
    }

    public void run() throws IOException {

        // Create a server socket listening for incoming datagrams
        ServerSocket welcomeSocket = new ServerSocket(sourcePort);

        // Create a SYN datagram
        TCPDatagram toSend = new TCPDatagram();
        toSend.SYN = 1;
        Random rand = new Random();
        toSend.SeqNo = rand.nextInt(1000) + 1;

        // Set seqNo
        seqNo = toSend.SeqNo;

        // Send the datagram
        sentDatagram = toSend;
        t = new SYNThread(sentDatagram, sourcePort, destPort, firsthop);
        t.start();

        // Listen for incoming datagrams
        Socket clientSocket = welcomeSocket.accept();
        processSocket(clientSocket);


    }

    void processSocket(Socket clientSocket)
    {
        try {
            InputStream is = clientSocket.getInputStream();
            byte[] s = new byte[1024];
            //is.skip(4);
            is.read(s);
            TCPDatagram tcpDatagram = new TCPDatagram();
            tcpDatagram.fromArray(s);

            processDatagram(tcpDatagram);

        }
        catch(IOException io) {
            System.out.println("ERROR: Cannot read data from the input stream.");
            System.out.println(io.getMessage());
        }
    }

    void processDatagram(TCPDatagram tcpDatagram) {

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
