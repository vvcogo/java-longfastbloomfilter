
public class Main {

	public static void main(String[] args) {
		long expectedNumberOfElements = 1000000;		
		double falsePosProb = .01;
		
		LongFastBloomFilter longFastBloomFilter = LongFastBloomFilter.getFilter(expectedNumberOfElements, falsePosProb);
		
		System.out.println("Created bloom filter, expecting " + expectedNumberOfElements + " elements.");
		System.out.println("Bit array size, m = " + longFastBloomFilter.getBitSetSize());
		System.out.println("Num hash functions, k = " + longFastBloomFilter.getNumHashFunctions());
		System.out.println("=========================================================");
		
		double start = System.currentTimeMillis();
		for (Integer i = 0; i < expectedNumberOfElements; i++) {
			longFastBloomFilter.add(i.toString().getBytes());
		}
		double addTime = (System.currentTimeMillis()- start)/1000.0;
		System.out.println("lbf add time: " + addTime + " seconds");
		System.out.println(longFastBloomFilter.getCurrentFalsePositiveProbability());
		
		System.out.println("=========================================================");
		int falsePositives = 0;
		
		start = System.currentTimeMillis();
		for (Integer i = (int)expectedNumberOfElements; i < expectedNumberOfElements+expectedNumberOfElements; i++) {
			if (longFastBloomFilter.contains(i.toString().getBytes())) {
				falsePositives++;
			}
		}
		addTime = (System.currentTimeMillis()- start)/1000.0;
		System.out.println("lbf contains time: " + addTime + " seconds");
		System.out.println("falseProb for lbf: " + falsePositives/(double)expectedNumberOfElements);

		
		System.out.println("=========================================================");
		int falseNegative = 0;
		start = System.currentTimeMillis();
		for (Integer i = 0; i < expectedNumberOfElements; i++) {
			if (!longFastBloomFilter.contains(i.toString().getBytes())) {
				falseNegative++;
			}
		}
		addTime = (System.currentTimeMillis()- start)/1000.0;
		System.out.println("falseNeg for lbf: " + falseNegative/(double)expectedNumberOfElements);
		System.out.println("=========================================================");
	}

}
