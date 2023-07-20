package longfastbloomfilter;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Allows to serialize or deserialize the given type.
 * @param <T>   the given type.
 */
public interface ISerializer<T> {

    /**
     * Serialize the specified type into the specified DataOutputStream instance.
     * @param t         type that will be serialized
     * @param dataOutS  data into which serialization needs to happen
     * @throws IOException
     */
    void serialize(T t, DataOutputStream dataOutS) throws IOException;

    /**
     * Deserialize into the specified DataInputStream instance.
     * @param dataInS   data into which deserialization needs to happen
     * @return          the type that was deserialized
     * @throws IOException
     */
    T deserialize(DataInputStream dataInS) throws IOException;
}
