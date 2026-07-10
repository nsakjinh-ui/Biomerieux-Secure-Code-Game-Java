package com.biomerieux.level3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * This test FAILS against the vulnerable DbCrudOps.
 * Your goal is to fix DbCrudOps so it turns green, while DbCrudOpsTest keeps passing too.
 *
 * DbCrudOps has 5 methods: getStockInfo, getStockPrice, updateStockPrice, execMultiQuery
 * and execUserScript. All of them are vulnerable! Here we demonstrate a stacked-query
 * exploit against getStockPrice, which applies equally to getStockInfo and updateStockPrice.
 *
 * We believe execMultiQuery and execUserScript shouldn't exist at all — see SOLUTION.md.
 */
class DbCrudOpsHackTest {

    private final DbCrudOps op = new DbCrudOps();

    @BeforeEach
    void resetDatabase() {
        Database.reset();
    }

    @Test
    void test1_stackedQueryInjectionMustNotAlterPrice() throws Exception {
        // what the hacker passes: a stacked UPDATE hiding behind a semicolon and a comment
        op.getStockPrice("MSFT'; UPDATE stocks SET price = '525' WHERE symbol = 'MSFT'--");

        String priceAfter = op.getStockPrice("MSFT");
        assertFalse(priceAfter.contains("525.0"),
                "Stacked-query injection must not be able to alter stock prices");
    }

    // Further exploit input could be, e.g.:
    // op.getStockPrice("MSFT'; DROP TABLE stocks--")
}
