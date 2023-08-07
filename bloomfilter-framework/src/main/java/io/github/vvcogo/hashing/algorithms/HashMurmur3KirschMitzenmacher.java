package io.github.vvcogo.hashing.algorithms;

import io.github.vvcogo.hashing.AbstractMurmur3Hash;

import java.nio.ByteBuffer;

public class HashMurmur3KirschMitzenmacher extends AbstractMurmur3Hash {
    @Override
    public long[] hash(byte[] msg, int k, long m){

        long[] result = new long[k];

        long hash1 = super.calculations(msg);
        long hash2 = super.calculations(super.longToBytes(hash1));

        for (int i = 0; i < k; i++){
            result[i] = (hash1 + i * hash2) % m; //Kirsch Mitzenmacher method
        }
        return result;
    }
}
