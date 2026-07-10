package com.biomerieux.level4;

import java.util.Random;

public class RandomGenerator {

    private static final String ALPHABET =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // VULNERABLE: java.util.Random is not cryptographically secure
    private final Random random = new Random();

    // generates a random token
    public String generateToken(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    public String generateToken() {
        return generateToken(8);
    }

    // generates a bcrypt-shaped salt "by hand" using weak randomness
    public String generateSalt(int rounds) {
        StringBuilder digits = new StringBuilder(21);
        for (int i = 0; i < 21; i++) {
            digits.append(random.nextInt(10));
        }
        return String.format("$2b$%02d$%s.", rounds, digits);
    }

    public String generateSalt() {
        return generateSalt(12);
    }
}
