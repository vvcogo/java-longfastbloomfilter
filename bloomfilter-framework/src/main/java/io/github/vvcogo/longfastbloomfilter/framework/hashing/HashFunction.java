package io.github.vvcogo.longfastbloomfilter.framework.hashing;

public enum HashFunction {

    MURMUR(new MurmurHash.MurmurHashAlgorithm()),
    MURMUR_KIRSCH_MITZENMACHER(new MurmurHash.MurmurHashKirschMitzenmacherAlgorithm()),
    MD2((msg, k, m) -> CryptoHash.hash(msg, k, m, "MD2")),
    MD5((msg, k, m) -> CryptoHash.hash(msg, k, m, "MD5")),
    SHA1((msg, k, m) -> CryptoHash.hash(msg, k, m, "SHA-1")),
    SHA224((msg, k, m) -> CryptoHash.hash(msg, k, m, "SHA-224")),
    SHA256((msg, k, m) -> CryptoHash.hash(msg, k, m, "SHA-256")),
    SHA384((msg, k, m) -> CryptoHash.hash(msg, k, m, "SHA-384")),
    SHA512((msg, k, m) -> CryptoHash.hash(msg, k, m, "SHA-512"));

    private final HashingAlgorithm algorithm;

    HashFunction(HashingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public HashingAlgorithm getHashingAlgorithm() {
        return algorithm;
    }
}