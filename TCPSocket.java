import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

public class TCPSocket {

    HashMap<Short, TCPThread> threadHashMap;
    short connectedRouter;

    public TCPSocket(int router)
    {
        connectedRouter = (short) router;
    }

    public void run() throws  IOException
    {
        ServerSocket serv = new ServerSocket(111);

        while (true)
        {
            Socket clientSocket = serv.accept();
            processPacket(clientSocket);
        }
    }

    public void processPacket(Socket packet) throws IOException {

        InputStream is = packet.getInputStream();
        byte[] s = new byte[4096];
        //is.skip(4);
        is.read(s);
        TCPDatagram tcpDatagram = new TCPDatagram();
        tcpDatagram.fromArray(s);

        TCPThread t = new TCPThread(tcpDatagram, threadHashMap);
        t.start();

    }

}
