package longfastbloomfilter;

import java.math.BigInteger;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {

        long expectedNumberOfElements = 1000000;
        double falsePosProb = .01;

        BigInteger bitSize  = BloomFilterCalculations.calculateMinBitArraySize(BigInteger.valueOf(expectedNumberOfElements), falsePosProb);
        int k = BloomFilterCalculations.optimalNumberOfHashFunctions(bitSize, BigInteger.valueOf(expectedNumberOfElements));
        IHash murmur = new MurmurHash();
        BloomFilterConfiguration config = new BloomFilterConfiguration(bitSize, BigInteger.valueOf(expectedNumberOfElements), k, falsePosProb);
        Function<Integer, byte[]> fSerializer = (num) -> BigInteger.valueOf(num).toByteArray();

        StandardBloomFilter<Integer> standardBF = new StandardBloomFilter<>(config,murmur, fSerializer);

        System.out.println("BF expecting "+ expectedNumberOfElements + " elements.");
        System.out.println("Bitmap size: m = " + config.bitSetSize());
        System.out.println("N hash functions: k = " + config.numberOfHashFunctions());
        System.out.println("=========================================================");

        double start = System.currentTimeMillis();
        for (int i = 0; i < expectedNumberOfElements; i++)
            standardBF.add(i);

        double addTime = (System.currentTimeMillis()- start)/1000.0;
        System.out.println("Added time: " + addTime + " seconds");
        System.out.println(config.falsePositiveRate());
        System.out.println("=========================================================");

        int falsePositives = 0;
        start = System.currentTimeMillis();
        for (int i = (int)expectedNumberOfElements; i < expectedNumberOfElements+expectedNumberOfElements; i++)
            if (standardBF.mightContains(i))
                falsePositives++;

        addTime = (System.currentTimeMillis()- start)/1000.0;
        System.out.println("Contains time: " + addTime + " seconds");
        System.out.println("False rate: " + falsePositives/(double)expectedNumberOfElements);

//        standardBF.getLongBitSet().set(0, 10522704);
//        System.out.println("=========================================================");
//        int falseNegative = 0;
//        start = System.currentTimeMillis();
//        for (int i = 0; i < expectedNumberOfElements; i++)
//            if (!standardBF.mightContains(i))
//                falseNegative++;
//
//        addTime = (System.currentTimeMillis()- start)/1000.0;
//        System.out.println("False rate: " + falseNegative/(double)expectedNumberOfElements);
//        System.out.println("=========================================================");

    }
}