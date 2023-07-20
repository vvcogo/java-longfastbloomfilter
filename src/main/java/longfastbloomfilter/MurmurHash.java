package longfastbloomfilter;

public class MurmurHash implements IHash {

    private static final long M = 0xc6a4a7935bd1e995L;
    private static final int R = 47;

    @Override
    public long hash(byte[] data, int length, long seed) {

        long h = (seed & 0xffffffffL) ^ (length * M);
        int length8 = length / 8;

        for (int i = 0; i < length8; i++) {
            final int i8 = i * 8;
            long k =  ((long)data[i8] & 0xff) + (((long)data[i8+1] & 0xff) <<8)
                        +(((long)data[i8+2] & 0xff) << 16) +(((long)data[i8+3] & 0xff) << 24)
                        +(((long)data[i8+4] & 0xff) << 32) +(((long)data[i8+5] & 0xff) << 40)
                        +(((long)data[i8+6] & 0xff) << 48) +(((long)data[i8+7] & 0xff) << 56);
            k *= M;
            k ^= k >>> R;
            k *= M;

            h ^= k;
            h *= M;
        }

        switch (length % 8) {
            case 7:
                h ^= (long)(data[(length&~7)+6] & 0xff) << 48;
            case 6:
                h ^= (long)(data[(length&~7)+5] & 0xff) << 40;
            case 5:
                h ^= (long)(data[(length&~7)+4] & 0xff) << 32;
            case 4:
                h ^= (long)(data[(length&~7)+3] & 0xff) << 24;
            case 3:
                h ^= (long)(data[(length&~7)+2] & 0xff) << 16;
            case 2:
                h ^= (long)(data[(length&~7)+1] & 0xff) << 8;
            case 1:
                h ^= data[length&~7] & 0xff;
                h *= M;
        };

        h ^= h >>> R;
        h *= M;
        h ^= h >>> R;

        return h;
    }
}
