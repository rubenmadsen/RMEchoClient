import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RMEchoPacket{
    public byte flags;
    public long id;
    public short data_len;
    public byte[] data;

    RMEchoPacket(byte flags, long id){
        this.flags = flags;
        this.id = id;
    }
    private byte[]construct(){
        //
        int off = 11;
        byte[] bytes = new byte[12 + this.data_len];
        //
        bytes[0] = flags;
        //
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(id);
        byte[] idBytes = buffer.array();
        bytes[1] = idBytes[7];
        bytes[2] = idBytes[6];
        bytes[3] = idBytes[5];
        bytes[4] = idBytes[4];
        bytes[5] = idBytes[3];
        bytes[6] = idBytes[2];
        bytes[7] = idBytes[1];
        bytes[8] = idBytes[0];
        //
        buffer = ByteBuffer.allocate((Short.BYTES));
        buffer.putShort(this.data_len);
        byte[] lenBytes = buffer.array();
        bytes[9]  = lenBytes[1];
        bytes[10] = lenBytes[0];
        for (int i = 0; i < this.data_len; i++) {
            bytes[i+off] = this.data[i];
        }
        //
        bytes[data_len+off] = '\0';
        return bytes;
    }
    public Object makeOriginalObject(byte[] bytes){
        Object obj = null;
        try{
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        }catch (Exception e){

        }
        return obj;
    }
    public byte[]makePacketFromString(String str){
        this.data_len = (short)str.length();
        this.data = (byte[]) str.getBytes();
        return construct();
    }
    public <T extends Serializable>byte[]makePacketFromObject(T anObject) throws IOException {

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(anObject);
        }ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(anObject);
            out.flush();

        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        this.data = bos.toByteArray();
        this.data_len = (short) this.data.length;

        return this.construct();
    }
}
