package managers;

import java.math.BigInteger;

public class KeyManager {
    private static BigInteger n, e;

    public static BigInteger getN() {
        return n;
    }

    public static void setN(BigInteger n) {
        KeyManager.n = n;
    }

    public static BigInteger getE() {
        return e;
    }

    public static void setE(BigInteger e) {
        KeyManager.e = e;
    }
}
