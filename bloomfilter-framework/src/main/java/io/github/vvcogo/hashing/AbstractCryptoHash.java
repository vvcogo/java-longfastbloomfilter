package io.github.vvcogo.hashing;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public abstract class AbstractCryptoHash implements HashingAlgorithm {

    protected String algorithm;
    protected AbstractCryptoHash(String algorithm){
        this.algorithm = algorithm;
    }

    @Override
    public long hash(byte[] msg) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] msgDigest = Objects.requireNonNull(md).digest(msg);
        return new BigInteger(msgDigest).longValue();
    }
}
