package long_bloomfilter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LongBitSetSerializer {
    public static void serialize(LongBitSet lbs, DataOutputStream dos) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(dos);
        oos.writeObject(lbs);
        oos.flush();
    }

    public static LongBitSet deserialize(DataInputStream dis) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(dis);
        try {
            return (LongBitSet) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
