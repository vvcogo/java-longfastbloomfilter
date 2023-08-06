package io.github.vvcogo;

import orestes.bloomfilter.BloomFilter;
import orestes.bloomfilter.FilterBuilder;

public class Main {

    public static void main(String[] args) {
        BloomFilter<String> bf = new FilterBuilder(1_000_000, 0.1)
                .buildBloomFilter();
        bf.add("nub");
        bf.add("hello");
        System.out.println(bf.contains("nub"));
        System.out.println(bf.contains("hello"));
        System.out.println(bf.contains("aojrfweoijtgowgh"));
    }
}