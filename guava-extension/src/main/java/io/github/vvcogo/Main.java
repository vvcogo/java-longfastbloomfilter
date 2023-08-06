package io.github.vvcogo;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;

import java.nio.charset.Charset;

public class Main {
    public static void main(String[] args) {
        Funnel<CharSequence> funnel = Funnels.stringFunnel(Charset.defaultCharset());
        BloomFilter<String> bloomFilter = BloomFilter.create(funnel, 1_000_000, 0.1);
        bloomFilter.put("nub");
        bloomFilter.put("hello");
        System.out.println(bloomFilter.mightContain("nub"));
        System.out.println(bloomFilter.mightContain("hello"));
        System.out.println(bloomFilter.mightContain("ajfiuewhgi"));
    }
}