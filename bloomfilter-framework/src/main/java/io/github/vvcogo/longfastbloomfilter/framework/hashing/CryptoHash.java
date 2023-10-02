package io.github.vvcogo.longfastbloomfilter.framework.hashing;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class CryptoHash {

    private CryptoHash() {
        throw new UnsupportedOperationException("Cannot create an instance of "+ getClass().getName());
    }

    public static ByteBuffer hash(byte[] msg, int k, long m, String method) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(k * Long.BYTES);
            LongBuffer longBuffer = buffer.asLongBuffer();
            MessageDigest digest = MessageDigest.getInstance(method);
            while (k > 0) {
                byte[] hash = digest.digest(msg);
                digest.update(hash);
                ByteBuffer hashBuffer = ByteBuffer.wrap(hash);
                while (hashBuffer.remaining() > 7 && k > 0) {
                    long value = (hashBuffer.getLong() & Long.MAX_VALUE) % m;
                    longBuffer.put(value);
                    k--;
                }
            }
            return buffer;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("The specified algorithm is not valid!", e);
        }
    }
}
