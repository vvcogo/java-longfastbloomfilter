package io.github.vvcogo.hashing.algorithms;

import io.github.vvcogo.hashing.AbstractMurmur3Hash;
import io.github.vvcogo.hashing.HashingAlgorithm;

import java.nio.ByteBuffer;
import java.util.Random;

public class HashMurmur3 extends AbstractMurmur3Hash {

    @Override
    public long[] hash(byte[] msg, int k, long m){

        long[] result = new long[k];
        long hash = super.calculations(msg);

        for (int i = 0; i < k; i++){
            result[i] = hash % m;
            hash = super.calculations(super.longToBytes(hash));
        }
        return result;
    }
}
