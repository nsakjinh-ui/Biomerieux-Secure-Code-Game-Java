/*
 * Please note:
 *
 * The first test class you should run in this level is DbCrudOpsTest, for database
 * creation, with all tests passing. Remember that running DbCrudOpsHackTest will
 * change the state of the database, causing some tests inside DbCrudOpsTest to fail.
 *
 * If you'd like to return to the initial state of the database, delete the database
 * file (level-3.db, created in this module's folder) and run the tests again.
 */
package com.biomerieux.level3;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {

    private static final Path DB_PATH = Paths.get("level-3.db");

    private Database() {
    }

    public static Connection createConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + DB_PATH.toAbsolutePath());
    }

    // creates the dummy database/table the first time it's needed, inserts seed data
    public static void ensureSeeded() {
        try (Connection con = createConnection(); Statement st = con.createStatement()) {
            var rs = st.executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='stocks'");
            if (!rs.next()) {
                st.execute("CREATE TABLE stocks (date text, symbol text, price real)");
                st.execute("INSERT INTO stocks VALUES ('2022-01-06', 'MSFT', 300.00)");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("ERROR initializing database", e);
        }
    }

    // resets the database to its pristine state (used by tests for a deterministic baseline)
    public static void reset() {
        try (Connection con = createConnection(); Statement st = con.createStatement()) {
            st.execute("DROP TABLE IF EXISTS stocks");
        } catch (SQLException e) {
            throw new IllegalStateException("ERROR resetting database", e);
        }
        ensureSeeded();
    }
}

