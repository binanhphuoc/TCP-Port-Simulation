# IMPLEMENTATION OF TCP

Language: Java

I. Network layer
-------------------------------------------------------
- Use TCP connection to simulate the communication channel of the network layer.
- There are total 8 routers, 3 agents, 1 Air-force base. Each of them has a its own port number, which acts similar to an IP address.


II. Network Layer: A Simple Router (with no queue)
-------------------------------------------------------
- This project focuses mainly on the end-end communication
between the agents. Therefore, the implementation of the
routers does not have queues, at least currently.
- Each router acts as a server which runs a while-loop
that keeps checking for a TCP connection from the
neighboring routers.
- Routers' connections are usually to pass data packets
to the next router.
- Each router will have its own PortNo. We assign each router a random number, as long as these PortNo's are known by other routers in the network.
- Thus, the while loop repeats the following steps:
  + Listen for a connection
  + Receive the packet on a connection
  + The packet contains a TCP Datagram. Check the destination port in the datagram against the forwarding table. The TCP Datagram can be extracted to get a specific header using TCPDatagram class. E.g:
  
      ```
      // Create an array to temporarily store the incoming packet
      byte[] s = new byte[4096];
      
      // Listen for an incoming packet
      Socket connectionSocket = welcomeSocket.accept();
      
      // Get the packet and store it into array s
      InputStream is = connectionSocket.getInputStream();
      is.read(s);
      
      // Extract the packet using TCPDatagram
      TCPDatagram tcpDatagram = new TCPDatagram();
      tcpDatagram.fromArray(s);
      
      // Get the headers
      System.out.println("Source Address Port: " + tcpDatagram.SourcePort);
      System.out.println("Dest Port: " + tcpDatagram.DestPort);
      System.out.println("Message sent: " + tcpDatagram.data);
      ```
      
  + Send the packet through the port found in the previous step
  + Log the transaction
- The forwarding table data for Router X is in file

```
./Routers/X.txt
```

- For more information about the format of data, please check:

```
./Routers/readme.md
```

III. Network Layer: TCP implementation at Agent
-------------------------------------------------------
- The agent is also a server process that always listens
to incoming packets at port (e.g 111). This port is
currently works as an IP address rather than a port at
the server. So let's call this kind of port: AddressPort.
- There are a 7 types of incoming packets:
  + DRP
  + TER
  + URG
  + ACK
  + RST
  + SYN
  + FIN
- If the server receives a connection request (a SYN packet)
at AddressPort (e.g 111), the server process will spawn a
different socket at a random port (e.g 44444). Now this new socket
is running on machine {AddressPort: 111, Port: 44444} in connection
with another socket on another agent machine.
- With this in mind, our packet datagram looks like:
{
  SourceAddressPort: 111,
  DestAddressPort: 101,
  {
    SourcePort: 44444,
    SourcePort: 55555,
    (other headers)
  }
}
- If the server (e.g at AddressPort 111) receives other
types of packet (not SYN), it will pass that packet to
the appropriate port (e.g 44444) using UDP connection.


IV. Agent: Server Process Implementation
-------------------------------------------------------

  + Read incoming packet
  +


V. Agent: Child threads Implementation
-------------------------------------------------------
+ Checksum
+ Determine the appropriate action based on the type. E.g:
  ~ Send ACK
  ~ Terminate the process
+ Display the data from the message
+ Get the input from the user
+ Create a packet and send to the other agent in the connection
