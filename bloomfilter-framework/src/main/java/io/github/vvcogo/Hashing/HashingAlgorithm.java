package io.github.vvcogo.Hashing;

public interface HashingAlgorithm {

    long hash(final byte[] msg, String algorithm);
}
