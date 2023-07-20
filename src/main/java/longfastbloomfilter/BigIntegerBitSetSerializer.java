package longfastbloomfilter;

import java.io.*;

/**
 * Class that allows to serialize or deserialize an object of the class {@code BigIntegerBitSet}.
 * Also implements the interface {@code ISerializer<T>}.
 */
public class BigIntegerBitSetSerializer implements ISerializer<BigIntegerBitSet> {
    @Override
    public void serialize(BigIntegerBitSet bigSet, DataOutputStream dataOutS) throws IOException {
        ObjectOutputStream objOutS = new ObjectOutputStream(dataOutS);
        objOutS.writeObject(bigSet);
        objOutS.flush();
    }

    @Override
    public BigIntegerBitSet deserialize(DataInputStream dataInS) throws IOException {
        ObjectInputStream objInS = new ObjectInputStream(dataInS);
        try {
            return (BigIntegerBitSet) objInS.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
