package io.github.vvcogo.hashing.algorithms;

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
