# Level 3 - Model solution

There are two valid levels of fix here. Both make `DbCrudOpsHackTest` and
`DbCrudOpsTest` pass. Pick the one that matches your goal.

## ✅ Minimum fix (required to pass the tests)

Only `getStockPrice` is actively exploited by `DbCrudOpsHackTest`. Fixing it is
enough to make every test green:

```java
public String getStockPrice(String stockSymbol) throws SQLException {
    Database.ensureSeeded();
    try (Connection con = Database.createConnection();
         PreparedStatement ps = con.prepareStatement("SELECT price FROM stocks WHERE symbol = ?")) {
        ps.setString(1, stockSymbol);
        StringBuilder res = new StringBuilder("[METHOD EXECUTED] get_stock_price\n");
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                res.append("[RESULT] ").append(rs.getDouble(1)).append("\n");
            }
        }
        return res.toString();
    }
}
```

`PreparedStatement` with a `?` placeholder makes user input **always** a plain
value, never SQL code — even a payload like `MSFT'; UPDATE stocks SET price =
'525' WHERE symbol = 'MSFT'--` is simply looked up as a (non-existent) literal
symbol, and no second statement ever runs.

## 🏆 Full fix (recommended, what a real code review / CodeQL would ask for)

`getStockInfo`, `getStockPrice`, and `updateStockPrice` all build SQL by string
concatenation — even `getStockInfo`'s partial "defense" (a blocklist of
restricted characters, plus a single-quote count check) is fragile and still
flagged by static analysis, because it isn't parameterized. On top of that:

`execMultiQuery` and `execUserScript` are worse — by design they let users
execute **arbitrary SQL scripts**. There is no safe way to "sanitize" free-form
SQL execution. The only correct fix is to **remove them entirely** and replace
ad hoc script execution with purpose-built, parameterized methods for each
legitimate operation the application actually needs.

```java
public String updateStockPrice(String stockSymbol, double price) throws SQLException {
    Database.ensureSeeded();
    try (Connection con = Database.createConnection();
         PreparedStatement ps = con.prepareStatement(
                 "UPDATE stocks SET price = ? WHERE symbol = ?")) {
        ps.setDouble(1, price);
        ps.setString(2, stockSymbol);
        ps.executeUpdate();
        return "[METHOD EXECUTED] update_stock_price\n";
    }
}

// getStockInfo follows the same pattern with a PreparedStatement.
// execMultiQuery and execUserScript are deleted from the class entirely.
```

If you remove `execMultiQuery` and `execUserScript`, `DbCrudOpsTest` will
detect it automatically and print "Well done!" instead of failing — you don't
need to touch the test file yourself.

## Key takeaways

- Always use `PreparedStatement` with `?` placeholders instead of building SQL
  strings by hand.
- Blocklists of "dangerous characters" are not a substitute for
  parameterization; they can always be bypassed by attackers who get creative.
- Never expose a method that executes arbitrary, user-supplied SQL scripts —
  don't just try to make it "safer", remove it.
