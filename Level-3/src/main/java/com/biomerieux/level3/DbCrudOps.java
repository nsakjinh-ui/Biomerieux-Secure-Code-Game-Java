package com.biomerieux.level3;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbCrudOps {

    // retrieves all info about a stock symbol from the stocks table
    // Example: getStockInfo("MSFT") results in executing
    // SELECT * FROM stocks WHERE symbol = 'MSFT'
    public String getStockInfo(String stockSymbol) throws SQLException {
        Database.ensureSeeded();
        try (Connection con = Database.createConnection(); Statement st = con.createStatement()) {
            StringBuilder res = new StringBuilder("[METHOD EXECUTED] get_stock_info\n");
            String query = "SELECT * FROM stocks WHERE symbol = '" + stockSymbol + "'";
            res.append("[QUERY] ").append(query).append("\n");

            // a block list (restricted characters) that should not exist in user-supplied input
            String restrictedChars = ";%&^!#-";
            boolean hasRestrictedChar = query.chars().anyMatch(c -> restrictedChars.indexOf(c) >= 0);
            long quoteCount = query.chars().filter(c -> c == '\'').count();
            boolean correctNumberOfSingleQuotes = quoteCount == 2;

            if (hasRestrictedChar || !correctNumberOfSingleQuotes) {
                res.append("CONFIRM THAT THE ABOVE QUERY IS NOT MALICIOUS TO EXECUTE");
            } else {
                try (ResultSet rs = st.executeQuery(query)) {
                    while (rs.next()) {
                        res.append("[RESULT] date=").append(rs.getString("date"))
                                .append(", symbol=").append(rs.getString("symbol"))
                                .append(", price=").append(rs.getDouble("price"));
                    }
                }
            }
            return res.toString();
        }
    }

    // retrieves the price of a stock symbol from the stocks table
    // Example: getStockPrice("MSFT") results in executing
    // SELECT price FROM stocks WHERE symbol = 'MSFT'
    public String getStockPrice(String stockSymbol) throws SQLException {
        Database.ensureSeeded();
        try (Connection con = Database.createConnection(); Statement st = con.createStatement()) {
            StringBuilder res = new StringBuilder("[METHOD EXECUTED] get_stock_price\n");
            String query = "SELECT price FROM stocks WHERE symbol = '" + stockSymbol + "'";
            res.append("[QUERY] ").append(query).append("\n");

            if (query.contains(";")) {
                // VULNERABLE: blindly executes whatever follows the semicolon (stacked queries)
                res.append("[SCRIPT EXECUTION]\n");
                st.executeUpdate(query);
            } else {
                try (ResultSet rs = st.executeQuery(query)) {
                    while (rs.next()) {
                        res.append("[RESULT] ").append(rs.getDouble(1)).append("\n");
                    }
                }
            }
            return res.toString();
        }
    }

    // updates stock price
    // Example: updateStockPrice("MSFT", 310.0) results in executing
    // UPDATE stocks SET price = '310' WHERE symbol = 'MSFT'
    public String updateStockPrice(String stockSymbol, double price) throws SQLException {
        Database.ensureSeeded();
        try (Connection con = Database.createConnection(); Statement st = con.createStatement()) {
            StringBuilder res = new StringBuilder("[METHOD EXECUTED] update_stock_price\n");
            // VULNERABLE: string-built query, price truncated to an int via "%d"-like formatting
            String query = "UPDATE stocks SET price = '" + (long) price + "' WHERE symbol = '" + stockSymbol + "'";
            res.append("[QUERY] ").append(query).append("\n");

            st.executeUpdate(query);
            return res.toString();
        }
    }

    // executes multiple queries
    // We believe this method shouldn't exist at all in the code, see SOLUTION.md
    public String execMultiQuery(String query) throws SQLException {
        Database.ensureSeeded();
        try (Connection con = Database.createConnection(); Statement st = con.createStatement()) {
            StringBuilder res = new StringBuilder("[METHOD EXECUTED] exec_multi_query\n");
            for (String single : query.split(";")) {
                if (single.isBlank()) {
                    continue;
                }
                String trimmed = single.trim();
                res.append("[QUERY] ").append(trimmed).append("\n");
                if (trimmed.toUpperCase().startsWith("SELECT")) {
                    try (ResultSet rs = st.executeQuery(trimmed)) {
                        while (rs.next()) {
                            res.append("[RESULT] ").append(rs.getObject(1)).append(" ");
                        }
                    }
                } else {
                    st.executeUpdate(trimmed);
                }
            }
            return res.toString();
        }
    }

    // executes any query, or multiple queries as a script, as defined by the user
    // We believe this method shouldn't exist at all in the code, see SOLUTION.md
    public String execUserScript(String query) throws SQLException {
        Database.ensureSeeded();
        try (Connection con = Database.createConnection(); Statement st = con.createStatement()) {
            StringBuilder res = new StringBuilder("[METHOD EXECUTED] exec_user_script\n");
            res.append("[QUERY] ").append(query).append("\n");
            if (query.contains(";")) {
                res.append("[SCRIPT EXECUTION]");
                st.executeUpdate(query);
            } else if (query.trim().toUpperCase().startsWith("SELECT")) {
                try (ResultSet rs = st.executeQuery(query)) {
                    while (rs.next()) {
                        res.append("[RESULT] ").append(rs.getObject(1));
                    }
                }
            } else {
                st.executeUpdate(query);
            }
            return res.toString();
        }
    }
}
