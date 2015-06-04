package long_bloomfilter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LongFastBloomFilter {

    private LongBitSet longBitSet;
    private final int k;
    private long currentNumElements;

    private MurmurHash murmurHash;
    private long[] bitSetIndexes;
    private long hash1;
    private long hash2;

    static ICompactSerializer<LongFastBloomFilter> serializer = new LongFastBloomFilterSerializer();

    public static ICompactSerializer<LongFastBloomFilter> serializer() {
        return serializer;
    }

    public LongFastBloomFilter(int k, LongBitSet longBitSet) {
        this.k = k;
        this.longBitSet = longBitSet;
        currentNumElements = 0;
        bitSetIndexes = new long[k];
        murmurHash = new MurmurHash();
    }

     // [NOTE1] Added the maxBitSetSize parameter to receive the value to be used as the performance optimization
    public static LongFastBloomFilter getFilter(long predictedNumElements, double falsePositiveProbability, double maxBitSetSizeChange) {
        BloomFilterCalculations.BloomFilterSpecification bloomFilterSpec = BloomFilterCalculations.computeBloomFilterSpec(predictedNumElements, falsePositiveProbability);
        bloomFilterSpec.setMaxBitSetSizeChange(maxBitSetSizeChange);
        bloomFilterSpec = BloomFilterCalculations.optimizeBloomFilterForSpeed(bloomFilterSpec.K, bloomFilterSpec.bitSetSize, predictedNumElements, falsePositiveProbability);

        return new LongFastBloomFilter(bloomFilterSpec.K, new LongBitSet(bloomFilterSpec.bitSetSize));
    }

    private void setHashValues(byte[] element) {
        hash1 = murmurHash.hash(element, element.length, 0);
        hash2 = murmurHash.hash(element, element.length, hash1);
    }

    private void setBitSetIndexes(byte[] element) {
        for (int i = 0; i < k; i++) {
            bitSetIndexes[i] = (hash1 + i * hash2) % longBitSet.size();
        }
    }

    // [NOTE2] Modified this method to synchronized (avoid race conditions)
    public synchronized void add(byte[] element) {
        setHashValues(element);
        setBitSetIndexes(element);
        for (long bitSetIndex : bitSetIndexes) {
            longBitSet.set((bitSetIndex < 0) ? bitSetIndex + longBitSet.size() : bitSetIndex);
        }
        currentNumElements++;
    }

    public boolean contains(byte[] element) {
        setHashValues(element);
        for (int i = 0; i < k; i++) {
            final long index = (hash1 + i * hash2) % longBitSet.size();
            if (!longBitSet.get((index < 0) ? index + longBitSet.size() : index)) {
                return false;
            }
        }
        return true;
    }
    
     // [NOTE3] Created this method (thread-safe version of contains)
    public boolean contains(byte[] element, long theHash1, long theHash2) {
        for (int i = 0; i < k; i++) {
            final long index = (theHash1 + i * theHash2) % longBitSet.size();
            if (!longBitSet.get((index < 0) ? index + longBitSet.size() : index)) {
                return false;
            }
        }
        return true;
    }

    public double getCurrentFalsePositiveProbability() {
        return Math.pow((1 - Math.exp(-k * currentNumElements / (double)longBitSet.size())), k);
    }

    public long getCurrentNumberOfElements() {
        return currentNumElements;
    }

    public long getBitSetSize() {
        return longBitSet.size();
    }

    public int getNumHashFunctions() {
        return k;
    }

    public LongBitSet getLongBitSet() {
        return longBitSet;
    }

    public void clear() {
        currentNumElements = 0;
        longBitSet.clear();
    }

}

class LongFastBloomFilterSerializer implements ICompactSerializer<LongFastBloomFilter> {
    public void serialize(LongFastBloomFilter lbf, DataOutputStream dos) throws IOException {
        dos.writeInt(lbf.getNumHashFunctions());
        LongBitSetSerializer.serialize(lbf.getLongBitSet(), dos);
    }

    public LongFastBloomFilter deserialize(DataInputStream dis) throws IOException {
        int hashes = dis.readInt();
        LongBitSet lbf = LongBitSetSerializer.deserialize(dis);
        return new LongFastBloomFilter(hashes, lbf);
    }
}
