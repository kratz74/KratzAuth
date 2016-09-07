/*
 * (C) 2016 Tomas Kraus
 */
package mc.server;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * Server configuration.
 */
public class ServerConfig {

    /** Configuration category for database. */
    private static final String DB_CATEGORY = "database";

    /** Configuration instance. */
    private static ServerConfig INSTANCE;

    /**
     * Get database connection host name or IP.
     * @return Database connection host name or IP.
     */
    public static String getDbHost() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.dbHost;
    }

    /**
     * Get database connection port.
     * @return Database connection port.
     */
    public static int getDbPort() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.dbPort;
    }

    /**
     * Get database connection user name.
     * @return Database connection user name.
     */
    public static String getDbUser() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.dbUser;
    }

    /**
     * Get database connection user password.
     * @return Database connection user password.
     */
    public static String getDbPassword() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.dbPassword;
    }

    /**
     * Get database name.
     * @return Database name.
     */
    public static String getDbName() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.dbName;
    }

    /**
     * Get database table with user credentials.
     * @return Database table with user credentials.
     */
    public static String getDbTable() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.dbTable;
    }

    /**
     * Get database table column with user name.
     * @return Database table column with user name.
     */
    public static String getDbUserColumn() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.dbUserColumn;
    }

    /**
     * Get database table column with user password.
     * @return Database table column with user password.
     */
    public static String getDbPasswordColumn() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.dbPasswordColumn;
    }

    /**
     * Initialize server configuration.
     * @param e Pre-initialization event.
     */
    public static void init(FMLPreInitializationEvent e) {
        INSTANCE = new ServerConfig(e);
    }

    /** Database connection host name or IP. */
    private final String dbHost;

    /** Database connection port. */
    private final int dbPort;

    /** Database connection user name. */
    private final String dbUser;

    /** Database connection user password. */
    private final String dbPassword;

    /** Database name. */
    private final String dbName;

    /** Database table with user credentials.*/
    private final String dbTable;

    /** Database table column with user name. */
    private final String dbUserColumn;

    /** Database table column with user password. */
    private final String dbPasswordColumn;

    /**
     * Creates an instance of server configuration object.
     * @param e Pre-initialization event.
     */
    private ServerConfig(FMLPreInitializationEvent e) {
        Configuration config = new Configuration(e.getSuggestedConfigurationFile());
        Logger.log(LogLevel.INFO, "Loading server configuration");
        config.load();
        config.addCustomCategoryComment(DB_CATEGORY, "Database configuration");
        Property dbHostProp = config.get(DB_CATEGORY, "DbHost", "127.0.0.1");
        dbHostProp.comment = "Database connection host name or IP";
        Property dbPortProp = config.get(DB_CATEGORY, "DbPort", 3306);
        dbPortProp.comment = "Database connection port";
        Property dbUserProp = config.get(DB_CATEGORY, "DbUser", "myUser");
        dbUserProp.comment = "Database connection user name";
        Property dbPassProp = config.get(DB_CATEGORY, "DbPassword", "myPassword");
        dbPassProp.comment = "Database connection user password";
        Property dbNameProp = config.get(DB_CATEGORY, "DbName", "forum");
        dbNameProp.comment = "Database name";
        Property userTableProp = config.get(DB_CATEGORY, "DbUserTable", "forum_users");
        userTableProp.comment = "Database table with user credentials";
        Property userColumnProp = config.get(DB_CATEGORY, "DbUserColumn", "username");
        userColumnProp.comment = "Database table column with user name";
        Property passwColumnProp = config.get(DB_CATEGORY, "DbPasswordColumn", "user_password");
        passwColumnProp.comment = "Database table column with user password";
        config.save();
        dbHost = dbHostProp.getString();
        dbPort = dbPortProp.getInt();
        dbUser = dbUserProp.getString();
        dbPassword = dbPassProp.getString();
        dbName = dbNameProp.getString();
        dbTable = userTableProp.getString();
        dbUserColumn = userColumnProp.getString();
        dbPasswordColumn = passwColumnProp.getString();
        Logger.log(LogLevel.INFO, 1, "Database %s on %s@%s:%d", dbName, dbUser, dbHost, dbPort);
        Logger.log(LogLevel.INFO, 1, "Table: %s, users: %s, passwords: %s", dbTable, dbUserColumn, dbPasswordColumn);
    }

}
