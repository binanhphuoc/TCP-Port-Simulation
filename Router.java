import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

class Router{

    public static void main(String argv[]) throws Exception {

    String clientSentence;
    String capitalizedSentence;
    ServerSocket welcomeSocket = new ServerSocket(1111);

    while (true) {

    // Accept the connection
        Socket connectionSocket = welcomeSocket.accept();

        InputStream is = connectionSocket.getInputStream();
        byte[] s = new byte[4096];
        //is.skip(4);
        is.read(s);
        TCPDatagram tcpDatagram = new TCPDatagram();
        tcpDatagram.fromArray(s);
        System.out.println("Source Address Port " + tcpDatagram.SourcePort);
        System.out.println("Dest Port " + tcpDatagram.DestPort);
        System.out.println(tcpDatagram.data);

        /*
        Socket nextSocket = new Socket(InetAddress.getByName("localhost"), tcpDatagram.DestAddressPort);

        DataOutputStream outToClient = new DataOutputStream(nextSocket.getOutputStream());

       // Construct a 1K buffer to hold bytes on their way to the socket.
        byte[] buffer = new byte[1024];
        int bytes = 0;
        // Copy requested file into the socket's output stream.
        while((bytes = inFromClient.read(buffer)) != -1 ) {
            outToClient.write(buffer,0, bytes);
            */
        }
    }
}
