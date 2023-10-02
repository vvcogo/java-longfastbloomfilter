package io.github.vvcogo.longfastbloomfilter.framework.hashing;

import java.util.HashMap;

public final class HashFactory {

    private static final HashMap<String, HashingAlgorithm> HASHING_ALGORITHMS = new HashMap<>();

    static {
        registerHashingAlgorithm("murmur", new MurmurHash.MurmurHashAlgorithm());
        registerHashingAlgorithm("murmur_kirschmitz", new MurmurHash.MurmurHashKirschMitzenmacherAlgorithm());
        registerHashingAlgorithm("md2", (msg, k, m) -> CryptoHash.hash(msg, k, m, "MD2"));
        registerHashingAlgorithm("md5", (msg, k, m) -> CryptoHash.hash(msg, k, m, "MD5"));
        registerHashingAlgorithm("sha1", (msg, k, m) -> CryptoHash.hash(msg, k, m, "SHA-1"));
        registerHashingAlgorithm("sha224", (msg, k, m) -> CryptoHash.hash(msg, k, m, "SHA-224"));
        registerHashingAlgorithm("sha256", (msg, k, m) -> CryptoHash.hash(msg, k, m, "SHA-256"));
        registerHashingAlgorithm("sha384", (msg, k, m) -> CryptoHash.hash(msg, k, m, "SHA-384"));
        registerHashingAlgorithm("sha512", (msg, k, m) -> CryptoHash.hash(msg, k, m, "SHA-512"));
    }

    public static void registerHashingAlgorithm(String name, HashingAlgorithm algorithm) {
        HASHING_ALGORITHMS.put(name, algorithm);
    }

    public static HashingAlgorithm getHashingAlgorithm(String name) {
        return HASHING_ALGORITHMS.get(name);
    }
}
