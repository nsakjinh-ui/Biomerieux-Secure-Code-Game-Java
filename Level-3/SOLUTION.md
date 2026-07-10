# Level 3 - Model solution

```java
package com.biomerieux.level3;

import java.sql.*;

public class DbCrudOps {

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
    // execMultiQuery and execUserScript are removed entirely (see explanation below).
}
```

## Solution explanation

The methodology used to protect against SQL injection is **parameterized statements
(prepared statements)**. They protect against user input tampering with the query
logic by using `?` as placeholders, so user input is always treated as *data*,
never as *SQL code* — even a payload like `MSFT'; UPDATE stocks SET price = '525'
WHERE symbol = 'MSFT'--` is simply looked up as a (non-existent) literal symbol
value, and no second statement is ever executed.

`getStockInfo`, `getStockPrice`, and `updateStockPrice` build their queries by
string concatenation/formatting. Even `getStockInfo`'s partial "defense" (a
blocklist of restricted characters, plus a single-quote count check) is fragile:
it's still not parameterized, so it remains flagged by static analysis (CodeQL),
and more sophisticated payloads can eventually be crafted around any blocklist.

`execMultiQuery` and `execUserScript` are worse: by design they let users execute
**arbitrary SQL scripts**, including semicolon-separated statements. There is no
safe way to "sanitize" free-form SQL execution — the only correct fix is to
**remove these methods entirely** and replace ad hoc script execution with
purpose-built, parameterized methods for each legitimate operation the application
actually needs (like the ones above).

**Key takeaways:**
- Always use `PreparedStatement` with `?` placeholders instead of building SQL
  strings by hand — this is true in Java (`java.sql.PreparedStatement`), just as
  it is in Python (`cursor.execute(query, params)`), Ruby on Rails, or Django.
- Blocklists of "dangerous characters" are not a substitute for parameterization;
  they can always be bypassed by attackers who get creative.
- Never expose an endpoint that executes arbitrary, user-supplied SQL scripts.
