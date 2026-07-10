package com.biomerieux.level1;

/**
 * Represents a line item in an order: either a 'payment' or a 'product'.
 * Note: quantity is a double on purpose (mirrors the original Python code,
 * which never enforces that quantity must be an integer).
 */
public record Item(String type, String description, double amount, double quantity) {
}
