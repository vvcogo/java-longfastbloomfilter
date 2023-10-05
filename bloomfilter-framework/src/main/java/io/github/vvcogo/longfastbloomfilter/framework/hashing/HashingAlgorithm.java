package io.github.vvcogo.longfastbloomfilter.framework.hashing;

import java.io.Serializable;
import java.nio.ByteBuffer;

public interface HashingAlgorithm extends Serializable {

    ByteBuffer hash(byte[] msg, int k, long m);
}
