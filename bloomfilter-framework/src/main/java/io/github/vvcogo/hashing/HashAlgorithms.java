package io.github.vvcogo.hashing;

public final class HashAlgorithms {

    private HashAlgorithms() {
    }

    public static final HashingAlgorithm MURMUR = new MurmurHash.MurmurHashAlgorithm();

    public static final HashingAlgorithm MURMUR_KIRSCH_MITZENMACHER = new MurmurHash.MurmurHashKirschMitzenmacherAlgorithm();

    public static final HashingAlgorithm MD2 = (msg, k, m) -> CryptoHash.hash(msg, k, m, "MD2");

    public static final HashingAlgorithm MD5 = (msg, k, m) -> CryptoHash.hash(msg, k, m, "MD5");

    public static final HashingAlgorithm SHA1 = (msg, k, m) -> CryptoHash.hash(msg, k, m, "SHA-1");

    public static final HashingAlgorithm SHA224 = (msg, k, m) -> CryptoHash.hash(msg, k, m, "SHA-224");

    public static final HashingAlgorithm SHA256 = (msg, k, m) -> CryptoHash.hash(msg, k, m, "SHA-256");

    public static final HashingAlgorithm SHA384 = (msg, k, m) -> CryptoHash.hash(msg, k, m, "SHA-384");

    public static final HashingAlgorithm SHA512 = (msg, k, m) -> CryptoHash.hash(msg, k, m, "SHA-512");
}
