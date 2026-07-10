package com.biomerieux.level4;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * In the original game, this level has no functional exploit script: the goal
 * is to inspect the CodeQL scanning alerts (hardcoded secret, weak hashing
 * algorithm, insecure randomness) and fix them.
 * <p>
 * These two checks give you a quick, local signal of the same two issues while
 * you iterate, but don't skip reading the CodeQL alerts — is passing these two
 * assertions really enough for the code to be 100% secure? ;)
 */
class SecurityChecksTest {

    @Test
    void test1_shouldNotSelectTheBrokenMd5Hasher() {
        assertEquals("SHA256_hasher", Constants.PASSWORD_HASHER,
                "PASSWORD_HASHER should point to a modern, salted hashing algorithm, not MD5");
    }

    @Test
    void test2_secretKeyShouldNotBeHardcoded() {
        assertNotEquals("TjWnZr4u7x!A%D*G-KaPdSgVkXp2s5v8", Constants.SECRET_KEY,
                "SECRET_KEY should be loaded from the environment, not committed to source control");
    }
}
