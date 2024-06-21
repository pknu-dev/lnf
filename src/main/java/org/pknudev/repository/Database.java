package org.pknudev.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.pknudev.common.Config;

public class Database {
    private static Connection conn = null;

    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("cannot load oracle driver", e);
        }
    }

    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(
                Config.getProperty("DATABASE_URL"),
                Config.getProperty("DATABASE_USER"),
                Config.getProperty("DATABASE_PASSWORD")
            );
        }
        return conn;
    }
}
