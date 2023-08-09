package io.github.vvcogo;

import io.github.vvcogo.hashing.HashAlgorithms;

public class TestMain {

    public static void main(String[] args) {
        BloomFilterConfiguration config = new BloomFilterConfiguration(1_000_000, 1_000, 2,0.01, HashAlgorithms.MURMUR_KIRSCH_MITZENMACHER);
        BloomFilter<String> bf = new StandardLongBloomFilter<>(config);
        bf.add("hello");
        bf.add("test");
        System.out.println(bf.mightContains("hello"));
        System.out.println(bf.mightContains("test"));
        System.out.println(bf.mightContains("hellotest"));
    }
}
