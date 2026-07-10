// !!! If you use another hashing algorithm please keep the name Sha256Hasher or the tests won't pass !!!
package com.biomerieux.level4;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class Sha256Hasher {

    // produces the password hash by combining password + salt
    public String passwordHash(String password, String salt) {
        String hexDigest = sha256Hex(password);
        return BCrypt.hashpw(hexDigest, salt);
    }

    // verifies that the hashed password matches the plain text version on verification
    public boolean passwordVerification(String password, String passwordHash) {
        String hexDigest = sha256Hex(password);
        return BCrypt.checkpw(hexDigest, passwordHash);
    }

    private static String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
