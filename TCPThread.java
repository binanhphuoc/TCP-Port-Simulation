import java.util.HashMap;

public class TCPThread extends Thread{

    public TCPThread(TCPDatagram datagram, HashMap<Short, TCPThread> threadHashMap)
    {

        threadHashMap.put(datagram.SourcePort, this);
    }

    public void run()
    {
        
    }

}
