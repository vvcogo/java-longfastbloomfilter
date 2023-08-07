package io.github.vvcogo.Hashing;

import io.github.vvcogo.Hashing.HashingAlgorithm;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoHash implements HashingAlgorithm {

    @Override
    public long hash(byte[] msg, String algorithm) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Invalid algorithm for hashing!", e);
        }
        byte[] msgDigest = md.digest(msg);
        return new BigInteger(msgDigest).longValue();
    }
}
