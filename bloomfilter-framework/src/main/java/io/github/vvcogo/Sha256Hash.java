package io.github.vvcogo;

public class Sha256Hash implements HashingAlgorithm{
    @Override
    public long hash(byte[] data, int length, long seed) {
        return 0;
    }

    //https://stackoverflow.com/questions/11954086/which-hash-functions-to-use-in-a-bloom-filter
}
