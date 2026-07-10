/*
 * Follow the instructions below to get started:
 *
 * 1. OrderValidatorTest is passing but OrderValidator is vulnerable.
 * 2. Review the code. Can you spot the bug?
 * 3. Fix the code but ensure that OrderValidatorTest still passes.
 * 4. Run OrderValidatorHackTest and if it passes then CONGRATS!
 * 5. If stuck, read hint.js
 * 6. Compare your solution with SOLUTION.md
 */
package com.biomerieux.level1;

public class OrderValidator {

    public static String validOrder(Order order) {
        double net = 0;

        for (Item item : order.items()) {
            if (item.type().equals("payment")) {
                net += item.amount();
            } else if (item.type().equals("product")) {
                net -= item.amount() * item.quantity();
            } else {
                return "Invalid item type: " + item.type();
            }
        }

        if (net != 0) {
            return String.format("Order ID: %s - Payment imbalance: $%.2f", order.id(), net);
        } else {
            return String.format("Order ID: %s - Full payment received!", order.id());
        }
    }
}
