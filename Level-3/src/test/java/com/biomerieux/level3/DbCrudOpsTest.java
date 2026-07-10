package com.biomerieux.level3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * These tests must keep passing after you fix the bugs in DbCrudOps.
 * Heads up: running DbCrudOpsHackTest changes the state of the database.
 * Each test here resets the database first so runs stay deterministic.
 */
class DbCrudOpsTest {

    private final DbCrudOps op = new DbCrudOps();

    @BeforeEach
    void resetDatabase() {
        Database.reset();
    }

    // tests correct retrieval of stock info given a symbol
    @Test
    void test1_getStockInfo() throws Exception {
        String output = op.getStockInfo("MSFT");
        assertTrue(output.contains("symbol=MSFT"));
        assertTrue(output.contains("price=300.0"));
    }

    // tests correct defense against SQLi when a user passes restricted characters
    @Test
    void test2_getStockInfoBlocksObviousInjection() throws Exception {
        String output = op.getStockInfo("MSFT'; UPDATE stocks SET price = '500' WHERE symbol = 'MSFT'--");
        assertTrue(output.contains("CONFIRM THAT THE ABOVE QUERY IS NOT MALICIOUS TO EXECUTE"));
    }

    // tests correct retrieval of stock price
    @Test
    void test3_getStockPrice() throws Exception {
        String output = op.getStockPrice("MSFT");
        assertTrue(output.contains("300.0"));
    }

    // tests correct update of stock price given symbol and updated price
    @Test
    void test4_updateStockPrice() throws Exception {
        op.updateStockPrice("MSFT", 310.0);
        String output = op.getStockPrice("MSFT");
        assertTrue(output.contains("310.0"));
    }

    // tests correct execution of multiple queries, if the method still exists
    @Test
    void test5_execMultiQuery() throws Exception {
        String output = op.execMultiQuery(
                "SELECT price FROM stocks WHERE symbol = 'MSFT'; SELECT * FROM stocks WHERE symbol = 'MSFT'");
        assertTrue(output.contains("[METHOD EXECUTED] exec_multi_query"));
    }

    // tests correct execution of a user script, if the method still exists
    @Test
    void test6_execUserScript() throws Exception {
        String output = op.execUserScript("SELECT price FROM stocks WHERE symbol = 'MSFT'");
        assertTrue(output.contains("300.0"));
    }
}
