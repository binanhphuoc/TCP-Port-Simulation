import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Ann {

    public static void main(String[] args)
    {
        TCPSocketServer server = new TCPSocketServer(111, 1111);
        try{
            server.run();
        }
        catch(IOException e)
        {
            System.out.println("ERROR: Cannot bind port.");
        }
    }
}
