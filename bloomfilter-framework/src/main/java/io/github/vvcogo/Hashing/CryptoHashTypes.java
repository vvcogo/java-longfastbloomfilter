package io.github.vvcogo.Hashing;

public enum CryptoHashTypes {
    MD2("MD2"),
    MD5("MD5"),
    SHA_1("SHA-1"),
    SHA_224("SHA-224"),
    SHA_256("SHA-256"),
    SHA_384("SHA-384"),
    SHA_512("SHA-512");

    private final String type;

    CryptoHashTypes(final String type){
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
