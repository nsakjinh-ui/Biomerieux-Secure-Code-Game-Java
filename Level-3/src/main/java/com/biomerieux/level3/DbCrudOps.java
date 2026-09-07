package com.biomerieux.level3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

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

    // tests correct execution of multiple queries, IF the method still exists.
    // Removing execMultiQuery entirely is a perfectly valid, even recommended, fix
    // (see SOLUTION.md) — this test won't block you if you do.
    @Test
    void test5_execMultiQuery() throws Exception {
        Method method;
        try {
            method = DbCrudOps.class.getMethod("execMultiQuery", String.class);
        } catch (NoSuchMethodException e) {
            System.out.println("Well done! execMultiQuery should not exist — "
                    + "removing it entirely is the best way to go.");
            return;
        }
        Object output = method.invoke(op,
                "SELECT price FROM stocks WHERE symbol = 'MSFT'; SELECT * FROM stocks WHERE symbol = 'MSFT'");
        assertTrue(output.toString().contains("[METHOD EXECUTED] exec_multi_query"));
    }

    // tests correct execution of a user script, IF the method still exists.
    // Removing execUserScript entirely is a perfectly valid, even recommended, fix
    // (see SOLUTION.md) — this test won't block you if you do.
    @Test
    void test6_execUserScript() throws Exception {
        Method method;
        try {
            method = DbCrudOps.class.getMethod("execUserScript", String.class);
        } catch (NoSuchMethodException e) {
            System.out.println("Well done! execUserScript should not exist — "
                    + "removing it entirely is the best way to go.");
            return;
        }
        Object output = method.invoke(op, "SELECT price FROM stocks WHERE symbol = 'MSFT'");
        assertTrue(output.toString().contains("300.0"));
    }
}
