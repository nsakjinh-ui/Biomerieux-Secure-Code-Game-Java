package com.biomerieux.level2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TaxPayerTest {

    // Example 1 - shows a valid path to a profile picture
    @Test
    void test1_validProfilePicturePath() throws Exception {
        TaxPayer taxPayer = new TaxPayer("username_test", "password_test");
        String output = taxPayer.getProfPicture("prof_picture.png");

        // the returned path must stay inside the intended base directory
        assertTrue(output.startsWith(taxPayer.getBaseDir().toString()));
    }

    // Example 2 - shows a valid path to a tax form (within the intended base directory)
    @Test
    void test2_validTaxFormPath() throws Exception {
        TaxPayer taxPayer = new TaxPayer("username_test", "password_test");
        String input = taxPayer.getBaseDir().resolve("tax_form.pdf").toString();
        String output = taxPayer.getTaxFormAttachment(input);

        assertTrue(output.startsWith(taxPayer.getBaseDir().toString()));
    }
}
