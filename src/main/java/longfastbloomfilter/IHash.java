package longfastbloomfilter;

/**
 * Interface that allows the implementation of a hash function.
 */
public interface IHash {

    /**
     * Generates a hash according to the given values.
     * @param data      the data that will be hashed.
     * @param length    the length of the array.
     * @param seed      the given seed.
     * @return          the number of hash functions.
     */
    long hash(final byte[] data, int length, long seed);
}
