package io.github.vvcogo.hashing;

public final class HashAlgorithms {

    private HashAlgorithms() {
    }

    public static final HashingAlgorithm MURMUR = new MurmurHash.MurmurHashAlgorithm();

    public static final HashingAlgorithm MURMUR_KIRSCH_MITZENMACHER = new MurmurHash.MurmurHashKirschMitzenmacherAlgorithm();

    public static final HashingAlgorithm SHA256 = (msg, k, m) -> CryptoHash.hash(msg, k, m, "SHA-256");
}
