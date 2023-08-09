package io.github.vvcogo.hashing;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class CryptoHash {

    private CryptoHash() {
    }

    public static long[] hash(byte[] msg, int k, long m, String method) {
        try {
            MessageDigest digest = MessageDigest.getInstance(method);
            long[] hashes = new long[k];
            for (int i = 0; i < k; i++) {
                byte[] hash = digest.digest(msg);
                digest.update(hash);
                hashes[i] = bytesToLong(hash) % m;
            }
            return hashes;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }
}
