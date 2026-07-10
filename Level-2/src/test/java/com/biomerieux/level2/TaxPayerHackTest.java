package com.biomerieux.level2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * These tests FAIL against the vulnerable TaxPayer.
 * Your goal is to fix TaxPayer so that both turn green,
 * while TaxPayerTest keeps passing too.
 */
class TaxPayerHackTest {

    // Example 1 - path traversal exploited on getProfPicture()
    // The blocklist only rejects paths starting with "/" or "..", so "./.." sneaks past it.
    @Test
    void test1_pathTraversalOnProfilePicture() throws Exception {
        TaxPayer taxPayer = new TaxPayer("username_test", "password_test");
        String input = "./../secret.txt";
        String output = taxPayer.getProfPicture(input);
        assertNull(output);
    }

    // Example 2 - path traversal exploited on getTaxFormAttachment() (no validation at all)
    @Test
    void test2_pathTraversalOnTaxFormAttachment() throws Exception {
        TaxPayer taxPayer = new TaxPayer("username_test", "password_test");
        String input = TaxPayer.resolveSecretFileOutsideBaseDir().toString();
        String output = taxPayer.getTaxFormAttachment(input);
        assertNull(output);
    }
}
