
/**
 * This class implements a vector of bits that grows as needed. Each
 * component of the bit set has a <code>boolean</code> value. The
 * bits of a <code>BitSet</code> are indexed by nonnegative integers.
 * Individual indexed bits can be examined, set, or cleared. One
 * <code>BitSet</code> may be used to modify the contents of another
 * <code>BitSet</code> through logical AND, logical inclusive OR, and
 * logical exclusive OR operations.
 * <p>
 * By default, all bits in the set initially have the value
 * <code>false</code>.
 * <p>
 * Every bit set has a current size, which is the number of bits
 * of space currently in use by the bit set. Note that the size is
 * related to the implementation of a bit set, so it may change with
 * implementation. The length of a bit set relates to logical length
 * of a bit set and is defined independently of implementation.
 * <p>
 * Unless otherwise noted, passing a null parameter to any of the
 * methods in a <code>BitSet</code> will result in a
 * <code>NullPointerException</code>.
 *
 * <p>A <code>BitSet</code> is not safe for multithreaded use without
 * external synchronization.
 *
 * @author  Arthur van Hoff
 * @author  Michael McCloskey
 * @author  Martin Buchholz
 * @version 1.67, 04/07/06
 * @since   JDK1.0
 */
public class LongBitSet {
    /*
     * BitSets are packed into arrays of "words."  Currently a word is
     * a long, which consists of 64 bits, requiring 6 address bits.
     * The choice of word size is determined purely by performance concerns.
     */
    private final static int ADDRESS_BITS_PER_WORD = 6;
    
    /**
     * The internal field corresponding to the serialField "bits".
     */
    private long[] words;

    /**
     * The number of bits in use
     */
    private long size = 0;
    
    /**
     * Given a bit index, return word index containing it.
     */
    private static int wordIndex(long bitIndex) {
        return (int) (bitIndex >> ADDRESS_BITS_PER_WORD);
    }

    /**
     * Creates a bit set whose initial size is large enough to explicitly
     * represent bits with indices in the range <code>0</code> through
     * <code>nbits-1</code>. All bits are initially <code>false</code>.
     *
     * @param     nbits   the initial size of the bit set.
     * @exception NegativeArraySizeException if the specified initial size
     *               is negative.
     */
    public LongBitSet(long nbits) {
		// nbits can't be negative; size 0 is OK
		if (nbits < 0)
			throw new NegativeArraySizeException("nbits < 0: " + nbits);

		initWords(nbits);
		
		this.size = nbits;
	}

    private void initWords(long nbits) {
    	words = new long[wordIndex(nbits-1) + 1];
    }

    /**
     * Sets the bit at the specified index to <code>true</code>.
     *
     * @param     bitIndex   a bit index.
     * @exception IndexOutOfBoundsException if the specified index is negative.
     * @since     JDK1.0
     */
	public void set(long bitIndex) {
		int wordIndex = wordIndex(bitIndex);

		words[wordIndex] |= (1L << bitIndex); // Restores invariants
	}

    /**
     * Sets the bit specified by the index to <code>false</code>.
     *
     * @param     bitIndex   the index of the bit to be cleared.
     * @exception IndexOutOfBoundsException if the specified index is negative.
     * @since     JDK1.0
     */
	public void clear(long bitIndex) {
		if (bitIndex < 0)
			throw new IndexOutOfBoundsException("bitIndex < 0: " + bitIndex);

		int wordIndex = wordIndex(bitIndex);

		words[wordIndex] &= ~(1L << bitIndex);
	}

    /**
     * Sets all of the bits in this BitSet to <code>false</code>.
     *
     * @since   1.4
     */
/*    public void clear() {
        while (wordsInUse > 0)
            words[--wordsInUse] = 0;
    }*/

    /**
     * Returns the value of the bit with the specified index. The value
     * is <code>true</code> if the bit with the index <code>bitIndex</code>
     * is currently set in this <code>BitSet</code>; otherwise, the result
     * is <code>false</code>.
     *
     * @param     bitIndex   the bit index.
     * @return    the value of the bit with the specified index.
     * @exception IndexOutOfBoundsException if the specified index is negative.
     */
	public boolean get(long bitIndex) {
		int wordIndex = wordIndex(bitIndex);
		return ((words[wordIndex] & (1L << bitIndex)) != 0);
	}

    /**
     * Returns the number of bits of space actually in use by this
     * <code>BitSet</code> to represent bit values.
     * The maximum element in the set is the size - 1st element.
     *
     * @return  the number of bits currently in this bit set.
     */
    public long size() {
    	return size;
    }

}
