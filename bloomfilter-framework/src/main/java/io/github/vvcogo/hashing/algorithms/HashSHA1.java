package io.github.vvcogo.hashing.algorithms;

import io.github.vvcogo.hashing.AbstractCryptoHash;

public class HashSHA1 extends AbstractCryptoHash {

    public HashSHA1() {
        super("SHA-1");
    }

    public static class HashSHA512 extends AbstractCryptoHash{

        public HashSHA512() {
            super("SHA-512");
        }
    }
}
