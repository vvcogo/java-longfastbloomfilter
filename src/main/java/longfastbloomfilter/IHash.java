package longfastbloomfilter;

public interface IHash {
    /**
     * 
     * @param data
     * @param length
     * @param seed
     * @return
     */
    long hash(final byte[] data, int length, long seed);
}
