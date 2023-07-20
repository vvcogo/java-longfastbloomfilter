package longfastbloomfilter;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {

        long expectedNumberOfElements = 1_000_000;
        double falsePosProb = .01;

        BigInteger bitSize  = BloomFilterCalculations.calculateMinBitArraySize(BigInteger.valueOf(expectedNumberOfElements), falsePosProb);
        int k = BloomFilterCalculations.optimalNumberOfHashFunctions(bitSize, BigInteger.valueOf(expectedNumberOfElements));
        IHashingFunction murmur = new MurmurHash();
        BloomFilterConfiguration config = new BloomFilterConfiguration(bitSize, BigInteger.valueOf(expectedNumberOfElements), k, falsePosProb);
        Function<String, byte[]> fSerializer = str -> str.getBytes(Charset.defaultCharset());

        IBloomFilter<String> bf = new StandardBloomFilter<>(config, murmur, fSerializer);

        bf.add("google.com");
        bf.add("github.com");
        bf.add("youtube.com");

        System.out.println("google.com: " + bf.mightContains("google.com"));
        System.out.println("github.com: " + bf.mightContains("github.com"));
        System.out.println("youtube.com: " + bf.mightContains("youtube.com"));
        System.out.println("amazon.com: " + bf.mightContains("amazon.com"));

//
//        StandardBloomFilter<Integer> standardBF = new StandardBloomFilter<>(config,murmur, fSerializer);
//
//        System.out.println("BF expecting "+ expectedNumberOfElements + " elements.");
//        System.out.println("Bitmap size: m = " + config.bitSetSize());
//        System.out.println("N hash functions: k = " + config.numberOfHashFunctions());
//        System.out.println("=========================================================");
//
//        double start = System.currentTimeMillis();
//        for (int i = 0; i < expectedNumberOfElements; i++) {
//            standardBF.add(i);
//        }
//
//        double addTime = (System.currentTimeMillis()- start)/1000.0;
//        System.out.println("Added time: " + addTime + " seconds");
//        System.out.println(config.falsePositiveRate());
//        System.out.println("=========================================================");
//
//        int falsePositives = 0;
//        start = System.currentTimeMillis();
//        for (int i = (int)expectedNumberOfElements; i < expectedNumberOfElements * 2; i++)
//            if (standardBF.mightContains(i))
//                falsePositives++;
//
//        addTime = (System.currentTimeMillis()- start)/1000.0;
//        System.out.println("Contains time: " + addTime + " seconds");
//        System.out.println("False rate: " + falsePositives/(double)expectedNumberOfElements);
    }
}