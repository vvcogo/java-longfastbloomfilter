package io.github.vvcogo.Hashing;

import java.util.Random;

/**
 * Code adapted from: https://github.com/yonik/java_util/tree/master
 */
public class Murmur3Hash implements HashingAlgorithm {

    private final Random rd = new Random(0);

    /** Returns the MurmurHash3_x64_128 hash, that is a good choice for longer strings or if you need more than 32 bits of hash. */
    @Override
    public long hash(byte[] msg, String algorithm){

        int len = msg.length;
        long seed = rd.nextLong(len);
        int offset = rd.nextInt(len);

        long h1 = seed & 0x00000000FFFFFFFFL;
        long h2 = seed & 0x00000000FFFFFFFFL;

        final long c1 = 0x87c37b91114253d5L;
        final long c2 = 0x4cf5ad432745937fL;

        int roundedEnd = offset + (len & 0xFFFFFFF0);  // round down to 16 byte block
        for (int i = offset; i < roundedEnd; i+=16) {
            long k1 = getLongLittleEndian(msg, i);
            long k2 = getLongLittleEndian(msg, i+8);
            k1 *= c1; k1  = Long.rotateLeft(k1,31); k1 *= c2; h1 ^= k1;
            h1 = Long.rotateLeft(h1,27); h1 += h2; h1 = h1*5+0x52dce729;
            k2 *= c2; k2  = Long.rotateLeft(k2,33); k2 *= c1; h2 ^= k2;
            h2 = Long.rotateLeft(h2,31); h2 += h1; h2 = h2*5+0x38495ab5;
        }

        long k1 = 0;
        long k2 = 0;

        switch (len & 15) {
            case 15: k2  = (msg[roundedEnd+14] & 0xffL) << 48;
            case 14: k2 |= (msg[roundedEnd+13] & 0xffL) << 40;
            case 13: k2 |= (msg[roundedEnd+12] & 0xffL) << 32;
            case 12: k2 |= (msg[roundedEnd+11] & 0xffL) << 24;
            case 11: k2 |= (msg[roundedEnd+10] & 0xffL) << 16;
            case 10: k2 |= (msg[roundedEnd+ 9] & 0xffL) << 8;
            case  9: k2 |= (msg[roundedEnd+ 8] & 0xffL);
                k2 *= c2;
                k2  = Long.rotateLeft(k2, 33);
                k2 *= c1;
                h2 ^= k2;
            case  8: k1  = ((long)msg[roundedEnd+7]) << 56;
            case  7: k1 |= (msg[roundedEnd+6] & 0xffL) << 48;
            case  6: k1 |= (msg[roundedEnd+5] & 0xffL) << 40;
            case  5: k1 |= (msg[roundedEnd+4] & 0xffL) << 32;
            case  4: k1 |= (msg[roundedEnd+3] & 0xffL) << 24;
            case  3: k1 |= (msg[roundedEnd+2] & 0xffL) << 16;
            case  2: k1 |= (msg[roundedEnd+1] & 0xffL) << 8;
            case  1: k1 |= (msg[roundedEnd  ] & 0xffL);
                k1 *= c1;
                k1  = Long.rotateLeft(k1,31);
                k1 *= c2;
                h1 ^= k1;
        }

        h1 ^= len;
        h2 ^= len;

        h1 += h2;
        h2 += h1;

        h1 = fmix64(h1);
        h2 = fmix64(h2);

        h1 += h2;
        h2 += h1;

        return (h1+h2) & 0x0FFFFFFFFFFFFFFFL;
    }

    private long fmix64(long k) {
        k ^= k >>> 33;
        k *= 0xff51afd7ed558ccdL;
        k ^= k >>> 33;
        k *= 0xc4ceb9fe1a85ec53L;
        k ^= k >>> 33;
        return k;
    }

    /** Gets a long from a byte buffer in little endian byte order. */
    private long getLongLittleEndian(byte[] buf, int offset) {
        return  ((long)buf[offset+7] << 56)   // no mask needed
                | ((buf[offset+6] & 0xffL) << 48)
                | ((buf[offset+5] & 0xffL) << 40)
                | ((buf[offset+4] & 0xffL) << 32)
                | ((buf[offset+3] & 0xffL) << 24)
                | ((buf[offset+2] & 0xffL) << 16)
                | ((buf[offset+1] & 0xffL) << 8)
                | (buf[offset] & 0xffL);        // no shift needed
    }
}
