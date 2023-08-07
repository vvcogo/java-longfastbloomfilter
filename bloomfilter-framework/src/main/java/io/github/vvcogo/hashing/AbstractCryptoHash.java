package io.github.vvcogo.hashing;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public abstract class AbstractCryptoHash implements HashingAlgorithm {

    protected String algorithm;
    private MessageDigest md;

    protected AbstractCryptoHash(String algorithm){
        this.algorithm = algorithm;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long[] hash(byte[] msg, int k, long m) {

        long[] hashes = new long[k];
        byte[] msgDigest = Objects.requireNonNull(md).digest(msg);

        for (int i = 0; i < k; i++){
            hashes[i] = bytesToLong(msgDigest);
            md.update(msgDigest);
            msgDigest = md.digest(msgDigest);
        }
        return hashes;
    }

    private long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }
}
