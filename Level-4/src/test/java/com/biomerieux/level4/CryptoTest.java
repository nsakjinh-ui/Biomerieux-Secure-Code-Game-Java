package com.biomerieux.level4;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CryptoTest {

    // verifies that hash and verification match each other for SHA256Hasher
    @Test
    void test1_sha256HasherRoundTrip() {
        RandomGenerator rd = new RandomGenerator();
        Sha256Hasher sha256 = new Sha256Hasher();
        boolean passVerification = sha256.passwordVerification("abc", sha256.passwordHash("abc", rd.generateSalt()));
        assertTrue(passVerification);
    }

    // verifies that hash and verification match each other for MD5Hasher (if it still exists)
    @Test
    void test2_md5HasherRoundTrip() {
        Md5Hasher md5 = new Md5Hasher();
        boolean md5Verification = md5.passwordVerification("abc", md5.passwordHash("abc"));
        assertTrue(md5Verification);
    }
}
