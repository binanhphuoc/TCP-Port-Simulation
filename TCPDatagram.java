import java.io.*;

class TCPDatagram
{
    //public short SourceAddressPort;
    //public short DestAddressPort;
    public short SourcePort;
    public short DestPort;
    public int SeqNo;
    public int AckNo;
    public short HeaderLength;
    public byte DRP;
    public byte TER;
    public byte URG;
    public byte ACK;
    public byte RST;
    public byte SYN;
    public byte FIN;
    public short rwnd;
    public short checksum;
    public short UrgentDataPtr;
    public byte[] data = new byte[1004];
    public int dataLength;

    public void fromArray(byte[] bytes) throws IOException {
        DataInputStream pkt = new DataInputStream(new ByteArrayInputStream(bytes));

        //SourceAddressPort = pkt.readShort();
        //DestAddressPort = pkt.readShort();
        SourcePort = pkt.readShort();
        DestPort = pkt.readShort();
        SeqNo = pkt.readInt();
        AckNo = pkt.readInt();

        short next = pkt.readShort();
        HeaderLength = (short) (next >>> 7);
        int flags = ((next << 9) >>> 9);
        DRP = (byte) (((flags >>> 6) & 1) == 0 ? 0 : 1);
        TER = (byte) (((flags >>> 5) & 1) == 0 ? 0 : 1);
        URG = (byte) (((flags >>> 4) & 1) == 0 ? 0 : 1);
        ACK = (byte) (((flags >>> 3) & 1)== 0 ? 0 : 1);
        RST = (byte) (((flags >>> 2) & 1) == 0 ? 0 : 1);
        SYN = (byte) (((flags >>> 1) & 1) == 0 ? 0 : 1);
        FIN = (byte) (((flags >>> 0) & 1) == 0 ? 0 : 1);

        rwnd = pkt.readShort();
        checksum = pkt.readShort();
        UrgentDataPtr = pkt.readShort();
        dataLength = pkt.read(data);


    }

    public void getData(String message) throws IOException {
        byte[] b = message.getBytes();
        DataInputStream pkt = new DataInputStream(new ByteArrayInputStream(b));
        dataLength = pkt.read(data);
    }

    public void toArray(byte[] m) throws IOException {
        ByteArrayOutputStream bOutput = new ByteArrayOutputStream(1024);
        DataOutputStream dout = new DataOutputStream(bOutput);
        //dout.writeShort(SourceAddressPort);
        //dout.writeShort(DestAddressPort);
        dout.writeShort(SourcePort);
        dout.writeShort(DestPort);
        dout.writeInt(SeqNo);
        dout.writeInt(AckNo);
        short temp = (short) ((HeaderLength << 9) | (DRP << 6) | (TER << 5) | (URG << 4) | (ACK << 3) | (RST << 2) | (SYN << 1) | FIN);
        dout.writeShort(temp);
        dout.writeShort(rwnd);
        dout.writeShort(checksum);
        dout.writeShort(UrgentDataPtr);
        dout.write(data, 0, dataLength);
        dout.flush();
        dout.close();

        m = bOutput.toByteArray();

    }

    public void print()
    {
        System.out.println("Source Port: " + SourcePort);
        System.out.println("DestPort: " + DestPort);
        System.out.println("SeqNo: " + SeqNo);
        System.out.println("AckNo: " + AckNo);
        System.out.println("HeaderLength: " + HeaderLength);
        System.out.println("DRP: " + DRP);
        System.out.println("TER: " + TER);
        System.out.println("URG: "+URG);
        System.out.println("ACK: " + ACK);
        System.out.println("RST: " + RST);
        System.out.println("SYN: " + SYN);
        System.out.println("FIN: " + FIN);
        System.out.println("Window size: " + rwnd);
        System.out.println("Checksum: " + checksum);
        System.out.println("UrgentDataPtr: "+ UrgentDataPtr);
        String s = new String(data);
        System.out.println("Data: " + s);
        System.out.println();

    }

}
