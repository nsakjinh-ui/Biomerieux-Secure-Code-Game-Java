# Level 1 - Model solution

```java
package com.biomerieux.level1;

import java.math.BigDecimal;

public class OrderValidator {

    private static final BigDecimal MAX_ITEM_AMOUNT = new BigDecimal("100000"); // max price of an item
    private static final int MAX_QUANTITY = 100;   // max quantity of an item
    private static final int MIN_QUANTITY = 0;     // min quantity of an item
    private static final BigDecimal MAX_TOTAL = new BigDecimal("1000000"); // max total accepted for an order

    public static String validOrder(Order order) {
        BigDecimal payments = BigDecimal.ZERO;
        BigDecimal expenses = BigDecimal.ZERO;

        for (Item item : order.items()) {
            if (item.type().equals("payment")) {
                BigDecimal amount = BigDecimal.valueOf(item.amount());
                if (amount.abs().compareTo(MAX_ITEM_AMOUNT) <= 0) {
                    payments = payments.add(amount);
                }
            } else if (item.type().equals("product")) {
                boolean isIntegerQuantity = item.quantity() == Math.floor(item.quantity());
                boolean quantityInRange = item.quantity() > MIN_QUANTITY && item.quantity() <= MAX_QUANTITY;
                boolean amountInRange = item.amount() > MIN_QUANTITY
                        && BigDecimal.valueOf(item.amount()).compareTo(MAX_ITEM_AMOUNT) <= 0;
                if (isIntegerQuantity && quantityInRange && amountInRange) {
                    expenses = expenses.add(BigDecimal.valueOf(item.amount())
                            .multiply(BigDecimal.valueOf(item.quantity())));
                }
            } else {
                return "Invalid item type: " + item.type();
            }
        }

        if (payments.abs().compareTo(MAX_TOTAL) > 0 || expenses.compareTo(MAX_TOTAL) > 0) {
            return "Total amount payable for an order exceeded";
        }

        if (payments.compareTo(expenses) != 0) {
            return String.format("Order ID: %s - Payment imbalance: $%.2f",
                    order.id(), payments.subtract(expenses));
        } else {
            return String.format("Order ID: %s - Full payment received!", order.id());
        }
    }
}
```

## Solution explanation

**A floating-point underflow vulnerability.**

In `OrderValidatorHackTest`, the attacker tricks the system by supplying an extremely
high amount as a fake payment, immediately followed by a payment reversal. The exploit
passes a huge number, causing an underflow while subtracting the cost of purchased
items, resulting in a net of zero.

It's good practice to limit your system's input to an acceptable range instead of
accepting any value. We also need to protect against a scenario where the attacker
sends a huge quantity of items, resulting in a huge net — we do this by bounding
every variable to reasonable values.

In addition, using `double` for financial calculations causes unexpected rounding and
comparison errors, because binary floating point cannot represent every decimal number
exactly (the classic `0.1 + 0.2 != 0.3` problem also exists in Java). The fix is to use
`BigDecimal`, ideally constructed from a `String` or via `BigDecimal.valueOf(double)`
(which goes through `Double.toString`) rather than `new BigDecimal(double)` directly,
which would preserve the binary floating-point rounding error.

Input validation should also check data types, not just numeric ranges — this is why
we verify that `quantity` is a whole number.
