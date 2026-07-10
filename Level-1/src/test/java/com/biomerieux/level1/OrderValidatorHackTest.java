package com.biomerieux.level1;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * These tests FAIL against the vulnerable OrderValidator.
 * Your goal is to fix OrderValidator so that all of them turn green,
 * while OrderValidatorTest keeps passing too.
 */
class OrderValidatorHackTest {

    // Tricks the system and walks away with 1 TV, despite valid payment & reimbursement
    @Test
    void test6_floatUnderflowExploit() {
        Item tvItem = new Item("product", "tv", 1000.00, 1);
        Item payment = new Item("payment", "invoice_4", 1e19, 1);
        Item payback = new Item("payment", "payback_4", -1e19, 1);
        Order order4 = new Order("4", List.of(payment, tvItem, payback));
        assertEquals("Order ID: 4 - Payment imbalance: $-1000.00", OrderValidator.validOrder(order4));
    }

    // Valid payments that should add up correctly, but do not (floating point precision)
    @Test
    void test7_floatingPointPrecision() {
        Item smallItem = new Item("product", "accessory", 3.3, 1);
        Item payment1 = new Item("payment", "invoice_5_1", 1.1, 1);
        Item payment2 = new Item("payment", "invoice_5_2", 2.2, 1);
        Order order5 = new Order("5", List.of(smallItem, payment1, payment2));
        assertEquals("Order ID: 5 - Full payment received!", OrderValidator.validOrder(order5));
    }

    // The total amount payable in an order should be limited
    @Test
    void test8_totalAmountLimit() {
        int numItems = 12;
        List<Item> items = new ArrayList<>();
        items.add(new Item("product", "tv", 99999, numItems));
        for (int i = 0; i < numItems; i++) {
            items.add(new Item("payment", "invoice_" + i, 99999, 1));
        }
        Order order1 = new Order("1", items);
        assertEquals("Total amount payable for an order exceeded", OrderValidator.validOrder(order1));

        // Put payments before products
        List<Item> reordered = new ArrayList<>(items.subList(1, items.size()));
        reordered.add(items.get(0));
        Order order2 = new Order("2", reordered);
        assertEquals("Total amount payable for an order exceeded", OrderValidator.validOrder(order2));
    }
}
