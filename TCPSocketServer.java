import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

public class TCPSocketServer {

    HashMap<Integer, TCPThread> threadHashMap;
    int port;
    int firsthop;


    public TCPSocketServer(int port, int firsthop)
    {
        this.firsthop = firsthop;
        this.port = port;
    }

    public void run() throws  IOException
    {
        ServerSocket serv = new ServerSocket(port);

        while (true)
        {
            Socket clientSocket = serv.accept();
            processPacket(clientSocket);
        }
    }

    public void processPacket(Socket packet) throws IOException {

        InputStream is = packet.getInputStream();
        byte[] s = new byte[1024];
        //is.skip(4);
        is.read(s);
        TCPDatagram tcpDatagram = new TCPDatagram();
        tcpDatagram.fromArray(s);

        if (threadHashMap.containsKey(tcpDatagram.SourcePort)) {
            threadHashMap.get(tcpDatagram.SourcePort).receive(tcpDatagram);
        }
        else
        {
            TCPThread t = new TCPThread(tcpDatagram, threadHashMap, firsthop);
            t.start();
        }

    }

}
