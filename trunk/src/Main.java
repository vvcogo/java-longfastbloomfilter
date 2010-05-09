

public class Main {

	public static void main(String[] args) {
		long n = 1000000;		
		double falsePosProb = .001;
		
		LongFastBloomFilter bfd = LongFastBloomFilter.getFilter(n, falsePosProb);
		System.out.println("Created bloom filter, expecting " + n + " elements.");
		System.out.println("Bit array size, m = " + bfd.getBitSetSize());
		System.out.println("Num hash functions, k = " + bfd.getNumHashFunctions());
		System.out.println("=========================================================");
		
		double start = System.currentTimeMillis();
		for (Integer i = 0; i < n; i++) {
			bfd.add(i.toString().getBytes());
		}
		double addTime = (System.currentTimeMillis()- start)/1000.0;
		System.out.println("lbf add time: " + addTime + " seconds");
		System.out.println(bfd.getCurrentFalsePositiveProbability());
		
		System.out.println("=========================================================");
		int falsePositives = 0;
		
		start = System.currentTimeMillis();
		for (Integer i = (int)n; i < n+n; i++) {
			if (bfd.contains(i.toString().getBytes())) {
				falsePositives++;
			}
		}
		addTime = (System.currentTimeMillis()- start)/1000.0;
		System.out.println("lbf contains time: " + addTime + " seconds");
		System.out.println("falseProb for lbf: " + falsePositives/(double)n);

		
		System.out.println("=========================================================");
		int falseNegative = 0;
		start = System.currentTimeMillis();
		for (Integer i = 0; i < n; i++) {
			if (!bfd.contains(i.toString().getBytes())) {
				falseNegative++;
			}
		}
		addTime = (System.currentTimeMillis()- start)/1000.0;
		System.out.println("falseNeg for lbf: " + falseNegative/(double)n);
		System.out.println("=========================================================");
	}

}
