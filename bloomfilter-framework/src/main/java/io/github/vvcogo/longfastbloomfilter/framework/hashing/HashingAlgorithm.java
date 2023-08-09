package io.github.vvcogo.longfastbloomfilter.framework.hashing;

import java.io.Serializable;

public interface HashingAlgorithm extends Serializable {

    long[] hash(byte[] msg, int k, long m);
}
