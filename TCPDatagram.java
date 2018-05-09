import java.io.*;

class TCPDatagram
{
    //public int SourceAddressPort;
    //public int DestAddressPort;
    public int SourcePort;
    public int DestPort;
    public int SeqNo;
    public int AckNo;
    public int HeaderLength = 36;
    public byte DRP;
    public byte TER;
    public byte URG;
    public byte ACK;
    public byte RST;
    public byte SYN;
    public byte FIN;
    public int rwnd = 53270;
    public int checksum;
    public int UrgentDataPtr;
    public byte[] data = new byte[1004];
    public int dataLength;

    int calculatedChecksum;

    public boolean valid()
    {
        if (calculatedChecksum == checksum)
            return true;
        return false;
    }

    public void fromArray(byte[] bytes) throws IOException {

        calculatedChecksum = calculateChecksum(bytes);

        DataInputStream pkt = new DataInputStream(new ByteArrayInputStream(bytes));

        //SourceAddressPort = pkt.readShort();
        //DestAddressPort = pkt.readShort();
        SourcePort = pkt.readShort();
        DestPort = pkt.readShort();
        SeqNo = pkt.readInt();
        AckNo = pkt.readInt();

        int next = pkt.readShort();
        HeaderLength = (int) (next >>> 7);
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

    public byte[] toArray() throws IOException {
        ByteArrayOutputStream bOutput = new ByteArrayOutputStream(1024);
        DataOutputStream dout = new DataOutputStream(bOutput);
        //dout.writeShort(SourceAddressPort);
        //dout.writeShort(DestAddressPort);
        dout.writeShort(SourcePort);
        dout.writeShort(DestPort);
        dout.writeInt(SeqNo);
        dout.writeInt(AckNo);
        int temp = (int) ((HeaderLength << 9) | (DRP << 6) | (TER << 5) | (URG << 4) | (ACK << 3) | (RST << 2) | (SYN << 1) | FIN);
        dout.writeShort(temp);
        dout.writeShort(rwnd);

        // Calculate checksum
        byte[] byteTemp = bOutput.toByteArray();

        checksum = calculateChecksum(byteTemp);

        dout.writeShort(checksum);
        dout.writeShort(UrgentDataPtr);
        dout.write(data, 0, dataLength);
        dout.flush();
        dout.close();

        return bOutput.toByteArray();

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

    /**
     * Calculate the Internet Checksum of a buffer (RFC 1071 - http://www.faqs.org/rfcs/rfc1071.html)
     * Algorithm is
     * 1) apply a 16-bit 1's complement sum over all octets (adjacent 8-bit pairs [A,B], final odd length is [A,0])
     * 2) apply 1's complement to this final sum
     *
     * Notes:
     * 1's complement is bitwise NOT of positive value.
     * Ensure that any carry bits are added back to avoid off-by-one errors
     *
     *
     * @param buf The message
     * @return The checksum
     */
    int calculateChecksum(byte[] buf) {
        int length = 16;
        int i = 0;

        int sum = 0;
        int data;

        // Handle all pairs
        while (length > 1) {
            // Corrected to include @Andy's edits and various comments on Stack Overflow
            data = (((buf[i] << 8) & 0xFF00) | ((buf[i + 1]) & 0xFF));
            sum += data;
            // 1's complement carry bit correction in 16-bits (detecting sign extension)
            if ((sum & 0xFFFF0000) > 0) {
                sum = sum & 0xFFFF;
                sum += 1;
            }

            i += 2;
            length -= 2;
        }

        // Handle remaining byte in odd length buffers
        if (length > 0) {
            // Corrected to include @Andy's edits and various comments on Stack Overflow
            sum += (buf[i] << 8 & 0xFF00);
            // 1's complement carry bit correction in 16-bits (detecting sign extension)
            if ((sum & 0xFFFF0000) > 0) {
                sum = sum & 0xFFFF;
                sum += 1;
            }
        }

        // Final 1's complement value correction to 16-bits
        sum = ~sum;
        sum = sum & 0xFFFF;
        return sum;

    }

}
