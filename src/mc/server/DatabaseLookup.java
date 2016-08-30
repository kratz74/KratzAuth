/*
 * (C) 2016 Tomas Kraus
 */
package mc.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import mc.log.LogLevel;
import mc.log.Logger;

/**
 * User credentials database lookup.
 */
public class DatabaseLookup {

    /** Database lookup instance. */
    private static DatabaseLookup INSTANCE;

    /** MySQL JDBC driver. */
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    /**
     * Initialize database lookup.
     */
    public static void init() {
        INSTANCE = new DatabaseLookup();
    }

    public static String getHash(final String user) {
        return INSTANCE.getPasswordHash(user);
    }

    /** Database URL. */
    private final String dbUrl;

    /** JDBC connection. */
    Connection conn;

    /**
     * Creates an instance of database lookup.
     */
    private DatabaseLookup() {
        dbUrl = "jdbc:mysql://" + ServerConfig.getDbHost() + ":"
                + Integer.toString(ServerConfig.getDbPort()) + "/" + ServerConfig.getDbName();
        conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.log(LogLevel.FATAL, "Could not load com.mysql.jdbc.Driver JDBC driver");
        }
    }

    /**
     * Close JDBC connection.
     */
    private void closeConnection() {
        if (conn != null) {
           try {
               conn.close();
               conn = null;
               Logger.log(LogLevel.INFO, "Closed JDBC connection to %s", dbUrl);
           } catch (SQLException ex) { 
                Logger.log(LogLevel.WARNING, "Could not close JDBC connection to MySQL://%s@%s:%d/%s",
                        ServerConfig.getDbUser(), ServerConfig.getDbHost(),
                        ServerConfig.getDbPort(), ServerConfig.getDbName());
            } 
        }
    }

   /**
     * Close JDBC {@link Statement}.
     */
    private static void closeStatement(final Statement stmt) {
        if (stmt != null) {
           try {
               stmt.close();
           } catch (SQLException ex) { 
                Logger.log(LogLevel.WARNING, "Could not close JDBC statement");
            } 
        }
    }

    /**
     * Validate JDBC connection.
     */
    private boolean isConnectionValid() {
        try {
            return conn.isValid(1);
        } catch (SQLException ex) {
            Logger.log(LogLevel.WARNING, "JDBC connection validation failed");
            return false;
        }
    }

    /**
     * Check JDBC connection and connect it when needed.
     */
    private void checkConnection() {
        if (conn != null) {
            if (!isConnectionValid()) {
                closeConnection();
            }
        }
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(dbUrl,ServerConfig.getDbUser(),ServerConfig.getDbPassword());
                Logger.log(LogLevel.INFO, "Opened JDBC connection to %s", dbUrl);
            } catch (SQLException ex) {
                Logger.log(LogLevel.FATAL, "Could not open JDBC connection to %s: %s", dbUrl, ex.getLocalizedMessage());
            }
        }
    }

    private String getPasswordHash(final String user) {
        final StringBuilder sb = new StringBuilder();
        sb.append("SELECT c.");
        sb.append(ServerConfig.getDbPasswordColumn());
        sb.append(" AS hash FROM ");
        sb.append(ServerConfig.getDbTable());
        sb.append(" c WHERE c.");
        sb.append(ServerConfig.getDbUserColumn());
        sb.append("='");
        sb.append(user);
        sb.append("'");
        final String sql = sb.toString();
        Logger.log(LogLevel.INFO, "Executing \"%s\"", sql);
        checkConnection();
        Statement stmt = null;
        String hash = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                hash = rs.getString(1);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.log(LogLevel.WARNING, "Error executing \"%s\": %s", sql, ex.getLocalizedMessage());
        } finally {
            closeStatement(stmt);
        }
        return hash;
    }

}
