package com.biomerieux.level4;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class Md5Hasher {

    // VULNERABLE: MD5 is cryptographically broken and unsuitable for password hashing
    // (no salt, extremely fast to brute-force / rainbow-table).
    public String passwordHash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean passwordVerification(String password, String passwordHash) {
        String computed = passwordHash(password);
        return MessageDigest.isEqual(
                computed.getBytes(StandardCharsets.UTF_8),
                passwordHash.getBytes(StandardCharsets.UTF_8));
    }
}
