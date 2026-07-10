package com.biomerieux.level1;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * These tests must keep passing after you fix the bug in OrderValidator.
 */
class OrderValidatorTest {

    // Example 1 - shows a valid and successful payment for a tv
    @Test
    void test1_validPayment() {
        Item tv = new Item("product", "tv", 1000.00, 1);
        Item payment = new Item("payment", "invoice_1", 1000.00, 1);
        Order order1 = new Order("1", List.of(payment, tv));
        assertEquals("Order ID: 1 - Full payment received!", OrderValidator.validOrder(order1));
    }

    // Example 2 - successfully detects payment imbalance as tv was never paid
    @Test
    void test2_detectsImbalance() {
        Item tv = new Item("product", "tv", 1000.00, 1);
        Order order2 = new Order("2", List.of(tv));
        assertEquals("Order ID: 2 - Payment imbalance: $-1000.00", OrderValidator.validOrder(order2));
    }

    // Example 3 - successfully reimburses client for a return so payment imbalance exists
    @Test
    void test3_reimbursement() {
        Item tv = new Item("product", "tv", 1000.00, 1);
        Item payment = new Item("payment", "invoice_3", 1000.00, 1);
        Item payback = new Item("payment", "payback_3", -1000.00, 1);
        Order order3 = new Order("3", List.of(payment, tv, payback));
        assertEquals("Order ID: 3 - Payment imbalance: $-1000.00", OrderValidator.validOrder(order3));
    }

    // Example 4 - handles invalid input such as placing an invalid order for 1.5 device
    @Test
    void test4_handlesFractionalQuantity() {
        Item tv = new Item("product", "tv", 1000, 1.5);
        Order order1 = new Order("1", List.of(tv));
        assertDoesNotThrow(() -> OrderValidator.validOrder(order1));
    }

    // Example 5 - handles an invalid item type called 'service'
    @Test
    void test5_invalidItemType() {
        Item service = new Item("service", "order shipment", 100, 1);
        Order order1 = new Order("1", List.of(service));
        assertEquals("Invalid item type: service", OrderValidator.validOrder(order1));
    }
}
