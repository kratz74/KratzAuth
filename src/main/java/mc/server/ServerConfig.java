/*
 * (C) 2016 Tomas Kraus
 */
package mc.server;

import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
//import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Server configuration.
 */
public class ServerConfig {

    /** Configuration category for database. */
    private static final String DB_CATEGORY = "database";

    /** Configuration category for MOTD. */
    private static final String LOGIN = "login";

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
     * Check whether users authentication is enabled.
     * @return Value of {@code true} if users authentication is enabled or {@code false} otherwise.
     */
    public static boolean isEnabled() {
        return INSTANCE.enable;
    }
    /**
     * Get server welcome message after successful login.
     * @return Server welcome message.
     */
    public static String getWelcomeMessage() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.welcomeMessage;
    }
    /**
     * Get server welcome info after successful login.
     * @return Server welcome info.
     */
    public static String getWelcomeInfo() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.welcomeInfo;
    }
    /**
     * Get server kick message after login failure.
     * @return Server kick message.
     */
    public static String getKickMessage() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.kickMessage;
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

    /** Enable/disable users authentication. */
    private final boolean enable;

    /** Welcome message after successful login. */
    private final String welcomeMessage;

    /** Welcome information after successful login. */
    private final String welcomeInfo;

    /** Kick message after login failure. */
    private final String kickMessage;

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
        dbHostProp.setComment("Database connection host name or IP");
        Property dbPortProp = config.get(DB_CATEGORY, "DbPort", 3306);
        dbPortProp.setComment("Database connection port");
        Property dbUserProp = config.get(DB_CATEGORY, "DbUser", "myUser");
        dbUserProp.setComment("Database connection user name");
        Property dbPassProp = config.get(DB_CATEGORY, "DbPassword", "myPassword");
        dbPassProp.setComment("Database connection user password");
        Property dbNameProp = config.get(DB_CATEGORY, "DbName", "forum");
        dbNameProp.setComment("Database name");
        Property userTableProp = config.get(DB_CATEGORY, "DbUserTable", "forum_users");
        userTableProp.setComment("Database table with user credentials");
        Property userColumnProp = config.get(DB_CATEGORY, "DbUserColumn", "username");
        userColumnProp.setComment("Database table column with user name");
        Property passwColumnProp = config.get(DB_CATEGORY, "DbPasswordColumn", "user_password");
        passwColumnProp.setComment("Database table column with user password");
        final Property enableProp;
        if (e.getSide().isServer()) {
            enableProp = config.get(DB_CATEGORY, "Enabled", "true");
            enableProp.setComment("Enable/disable users authentication");
        } else {
            enableProp = null;
        }
        final Property welcomeMessageProp = config.get(LOGIN, "WelcomeMessage", "Welcome to the Lord of the Rings Minecraft");
        welcomeMessageProp.setComment("Welcome message to display on login");
        final Property welcomeInfoProp = config.get(LOGIN, "WelcomeInfo", "Check http://www.carovnak.cz for news.");
        welcomeInfoProp.setComment("Welcome information to display on login");
        final Property kickMessageProp = config.get(LOGIN, "KickMessage", "Wrong user name or password. Check your registration on http://www.carovnak.cz web.");
        kickMessageProp.setComment("Kick message after login failure");
        config.save();
        dbHost = dbHostProp.getString();
        dbPort = dbPortProp.getInt();
        dbUser = dbUserProp.getString();
        dbPassword = dbPassProp.getString();
        dbName = dbNameProp.getString();
        dbTable = userTableProp.getString();
        dbUserColumn = userColumnProp.getString();
        dbPasswordColumn = passwColumnProp.getString();
        // Null on clients side means users authentication is always enabled.
        enable = enableProp != null ? enableProp.getBoolean() : true;
        welcomeMessage = welcomeMessageProp.getString();
        welcomeInfo = welcomeInfoProp.getString();
        kickMessage = kickMessageProp.getString();
        Logger.log(LogLevel.INFO, 1, "Database %s on %s@%s:%d", dbName, dbUser, dbHost, dbPort);
        Logger.log(LogLevel.INFO, 1, "Table: %s, users: %s, passwords: %s", dbTable, dbUserColumn, dbPasswordColumn);
        Logger.log(LogLevel.INFO, 1, "Welcome message: %s", welcomeMessage);
        Logger.log(LogLevel.INFO, 1, "Welcome info: %s", welcomeInfo);
        Logger.log(LogLevel.INFO, 1, "Kick message: %s", kickMessage);
    }

}
