package io.github.vvcogo;

import io.github.vvcogo.hashing.HashAlgorithms;

public class TestMain {

    public static void main(String[] args) {
        Serializer<String> serializer = new StringSerializer<>();
        BloomFilterConfiguration config = new BloomFilterConfiguration(1_000_000, 1_000, 2,0.01, HashAlgorithms.MURMUR_KIRSCH_MITZENMACHER, serializer);
        BloomFilter<String> bf = new StandardLongBloomFilter<>(config);
        bf.add("hello");
        bf.add("test");
        System.out.println(bf.mightContains("hello"));
        System.out.println(bf.mightContains("test"));
        System.out.println(bf.mightContains("hellotest"));
    }

    public static class StringSerializer<String> implements Serializer<String>{
        @Override
        public byte[] serialize(String element) {
            return new byte[0];
        }
    }
}
