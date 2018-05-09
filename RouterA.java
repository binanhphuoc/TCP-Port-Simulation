import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

short getNextPortNumber(short portNumber) {
    switch(portNumber) {
        case 111: // Ann
            return 111; // Ann
            break;
        case 001: // Chan
            return 5555; // E
            break;
        case 100: // Jan
            return 2222; // B
            break;
        default:
            break;
    }
}

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

            // Close connection with current client
            connectionSocket.close()

            Socket nextSocket = new Socket(InetAddress.getByName("localhost"), getNextPortNumber(tcpDatagram.DestPort));

            DataOutputStream outToClient = new DataOutputStream(nextSocket.getOutputStream());

           // Construct a 1K buffer to hold bytes on their way to the socket.
            byte[] buffer = new byte[1024];
            int bytes = 0;
            // Copy requested file into the socket's output stream.
            while((bytes = inFromClient.read(buffer)) != -1 ) {
                outToClient.write(buffer,0, bytes);
            }
            nextSocket.close();
        }
    }
}
