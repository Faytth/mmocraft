package org.unallied.mmocraft.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.unallied.mmocraft.constants.DatabaseConstants;

public class DatabaseConnection {
    private static ThreadLocal<Connection> con = new ThreadLocalConnection();
    private static String url = DatabaseConstants.DB_URL;
    private static String user = DatabaseConstants.DB_USER;
    private static String pass = DatabaseConstants.DB_PASS;

    public static Connection getConnection() {
        return con.get();
    }

    public static void release() throws SQLException {
        con.get().close();
        con.remove();
    }

    private static class ThreadLocalConnection extends ThreadLocal<Connection> {
        static {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("Could not locate the JDBC mysql driver.");
            }
        }

        @Override
        protected Connection initialValue() {
            return getConnection();
        }

        private Connection getConnection() {
            try {
                return DriverManager.getConnection(url, user, pass);
            } catch (SQLException sql) {
                System.out.println("Could not create a SQL Connection object. Please make sure you've correctly configured the database properties inside constants/ServerConstants.java. MAKE SURE YOU COMPILED!");
                return null;
            }
        }

        @Override
        public Connection get() {
            Connection con = super.get();
            try {
                if (!con.isClosed()) {
                    return con;
                }
            } catch (SQLException sql) {
                // Obtain a new connection
            }
            System.out.println("Getting connection");
            con = getConnection();
            super.set(con);
            return con;
        }
    }
}
