/*
 * (C) 2016 Tomas Kraus
 */
package mc.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import mc.log.LogLevel;
import mc.log.Logger;

/**
 * User credentials database lookup.
 */
public class DatabaseLookup {

	/** Cache record timeout in milliseconds. */
	private static final long CACHE_TIMEOUT = 60 * 60 * 1000;

	/**
	 * User record in user hash cache.
	 */
	private static final class UserCache {
		/** Stored user hash. */
		private final String hash;
		/** Record timestamp. */
		private final long ts;

		/**
		 * Creates a user hash cache instance with newly generated time stamp.
		 * 
		 * @param hash user hash
		 */
		private UserCache(String hash) {
			this.ts = System.currentTimeMillis();
			this.hash = hash;
		}
		private boolean valid() {
			return System.currentTimeMillis() - ts < CACHE_TIMEOUT;
		}

		private String getHash() {
			return hash;
		}
	}

	/** Database lookup instance. */
    private static DatabaseLookup INSTANCE;

    /** MySQL JDBC driver. */
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    /**
     * Initialize database lookup.
     */
    public static void init() {
        INSTANCE = new DatabaseLookup();
        INSTANCE.loadUsers();
    }

    public static String getHash(final String user) {
        return INSTANCE.getPasswordHash(user);
    }

    /** Database URL. */
    private final String dbUrl;

    /** JDBC connection. */
    Connection conn;

    private final Map<String, UserCache> cache;

    /**
     * Creates an instance of database lookup.
     */
    private DatabaseLookup() {
        dbUrl = "jdbc:mysql://" + ServerConfig.getDbHost() + ":"
                + Integer.toString(ServerConfig.getDbPort()) + "/" + ServerConfig.getDbName();
        conn = null;
        cache = new ConcurrentHashMap<>();
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
               Logger.log(LogLevel.INFO, "Closed JDBC connection to %s", dbUrl);
           } catch (SQLException ex) { 
                Logger.log(LogLevel.WARNING, "Could not close JDBC connection to MySQL://%s@%s:%d/%s",
                        ServerConfig.getDbUser(), ServerConfig.getDbHost(),
                        ServerConfig.getDbPort(), ServerConfig.getDbName());
            } finally {
                conn = null;				
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
            return conn.isValid(5);
        } catch (SQLException ex) {
            Logger.log(LogLevel.WARNING, "JDBC connection validation failed");
            return false;
        }
    }

    /**
     * Check JDBC connection and connect it when needed.
     */
    private Connection getConnection() {
        if (conn != null) {
            if (isConnectionValid()) {
            	return conn;
            } else {
                closeConnection();
            }
        }
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(dbUrl,ServerConfig.getDbUser(),ServerConfig.getDbPassword());
                Logger.log(LogLevel.INFO, "Opened JDBC connection to %s", dbUrl);
            } catch (SQLException ex) {
                Logger.log(LogLevel.FATAL, "Could not open JDBC connection to %s: %s", dbUrl, ex.getLocalizedMessage());
                return null;
            }
        }
        return conn;
    }

    // TODO: Some columns and table names are hardcoded. Modify them to use config!
    private void loadUsers() {
    	Logger.log(LogLevel.INFO, "Loading database users cache");
    	Connection conn = getConnection();
    	if (conn == null) {
    		return;
    	}
    	final StringBuilder sb = new StringBuilder();
    	sb.append("SELECT c.");
    	sb.append(ServerConfig.getDbUserColumn());
    	sb.append(" AS user, c."); 
    	sb.append(ServerConfig.getDbPasswordColumn());
    	
        sb.append(" AS hash FROM ");
        sb.append(ServerConfig.getDbTable());
        sb.append(" c INNER JOIN cm_user_group ug ON ug.user_id=c.user_id ");
        sb.append("INNER JOIN cm_groups g ON g.group_id=ug.group_id ");
        sb.append("WHERE g.group_name = ?");
        PreparedStatement stmt = null;
        try {
    	    stmt = conn.prepareStatement(sb.toString());
    	    stmt.setString(1, "Hráči");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
            	final String name = rs.getString(1);
            	final String hash = rs.getString(2);
            	if (name != null && hash != null) {
            		cache.put(name, new UserCache(hash));
            		Logger.log(LogLevel.INFO, "Cache initialization: %s valid for %d seconds", name, CACHE_TIMEOUT/1000);
            	}
            }
        } catch (SQLException ex) {
            Logger.log(LogLevel.WARNING, "Error executing \"%s\": %s", sb.toString(), ex.getLocalizedMessage());
        } finally {
            closeStatement(stmt);
        }
    }

    /**
     * Get password hash of an user.
     *
     * @param user user to check
     * @return password hash if exists
     */
    private String getPasswordHash(final String user) {
        final UserCache cached = cache.get(user);
        if (cached != null && cached.valid()) {
        	Logger.log(LogLevel.INFO, "User %s found in cache", user);
        	return cached.getHash();
        }
        final String hash = selectPasswordHash(user);
        if (hash != null) {
        	Logger.log(LogLevel.INFO, "User %s added to cache, valid for %d seconds", user, CACHE_TIMEOUT/1000);
        	cache.put(user, new UserCache(hash));
        }
        return hash;
    }
    	
    // TODO: Some columns and table names are hardcoded. Modify them to use config!
    /**
     * Select user hash from database.
     *
     * @param user user name to be used in WHERE condition
     * @return user hash map stored in database
     */
    private String selectPasswordHash(final String user) {	
    	final StringBuilder sb = new StringBuilder();
        sb.append("SELECT c.");
        sb.append(ServerConfig.getDbPasswordColumn());
        sb.append(" AS hash FROM ");
        sb.append(ServerConfig.getDbTable());
        sb.append(" c INNER JOIN cm_user_group ug ON ug.user_id=c.user_id ");
        sb.append("INNER JOIN cm_groups g ON g.group_id=ug.group_id ");
        sb.append("WHERE c.");
        sb.append(ServerConfig.getDbUserColumn());
        sb.append("= ? AND g.group_name = ?");
        final String sql = sb.toString();
        Logger.log(LogLevel.INFO, "Executing \"%s\"", sql);
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        String hash = null;
        if (conn == null) {
            return null;
        }
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user);
            stmt.setString(2, "Hráči");
            ResultSet rs = stmt.executeQuery();
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
