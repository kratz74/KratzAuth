/*
 * (C) 2016 Tomas Kraus
 */
package mc.server;

import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

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
    public static void init(ModConfig.ModConfigEvent e) {
        INSTANCE = new ServerConfig(e);
    }

    private final ForgeConfigSpec spec;
    private final ForgeConfigSpec.IntValue versionSpec;

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
    private ServerConfig(ModConfig.ModConfigEvent e) {
    	Logger.log(LogLevel.INFO, "Loading server configuration");
    	ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    	versionSpec = builder
    			.comment("Configuration file version number")
    			.defineInRange("version", 1, 1, 2);

    	ForgeConfigSpec.ConfigValue<String> dbHostCfg = builder
    			.comment("Database connection host name or IP")
    			.define("DbHost", "127.0.0.1");
    	ForgeConfigSpec.ConfigValue<Integer> dbPortCfg = builder
    			.comment("Database connection port"
    					).define("DbPort", 3306);
    	ForgeConfigSpec.ConfigValue<String> dbUserCfg = builder
    			.comment("Database connection user name")
    			.define("DbUser", "myUser");
    	ForgeConfigSpec.ConfigValue<String> dbPasswordCfg = builder
    			.comment("Database connection user password")
    			.define("DbPassword", "myPassword");
    	ForgeConfigSpec.ConfigValue<String> dbNameCfg = builder
    			.comment("Database name")
    			.define("DbName", "forum");
    	ForgeConfigSpec.ConfigValue<String> dbTableCfg = builder
    			.comment("Database table with user credentials")
    			.define("DbUserTable", "forum_users");
    	ForgeConfigSpec.ConfigValue<String> dbUserColumnCfg = builder
    			.comment("Database table column with user name")
    			.define("DbUserColumn", "username");
    	ForgeConfigSpec.ConfigValue<String> dbPasswordColumnCfg = builder
    			.comment("Database table column with user password")
    			.define("DbPasswordColumn", "user_password");
    	ForgeConfigSpec.ConfigValue<Boolean> enableCfg = builder
    			.comment("Enable/disable users authentication")
    			.define("Enabled", true);
    	ForgeConfigSpec.ConfigValue<String> welcomeMessageCfg = builder
    			.comment("Welcome message to display on login")
    			.define("WelcomeMessage", "Welcome to the Lord of the Rings Minecraft");
    	ForgeConfigSpec.ConfigValue<String> welcomeInfoCfg = builder
    			.comment("Welcome information to display on login")
    			.define("WelcomeInfo", "Check http://www.carovnak.cz for news.");
    	ForgeConfigSpec.ConfigValue<String> kickMessageCfg = builder
    			.comment("Kick message after login failure")
    			.define("KickMessage", "Wrong user name or password. Check your registration on http://www.carovnak.cz web.");

    	builder.pop();
    	spec = builder.build();
    	spec.save();

        dbHost = dbHostCfg.get();
        dbPort = dbPortCfg.get();
        dbUser = dbUserCfg.get();
        dbPassword = dbPasswordCfg.get();
        dbName = dbNameCfg.get();
        dbTable = dbUserColumnCfg.get();
        dbUserColumn = dbUserColumnCfg.get();
        dbPasswordColumn = dbPasswordColumnCfg.get();
        // Null on clients side means users authentication is always enabled.
        enable = enableCfg != null ? enableCfg.get() : true;
        welcomeMessage = welcomeMessageCfg.get();
        welcomeInfo = welcomeInfoCfg.get();
        kickMessage = kickMessageCfg.get();
        Logger.log(LogLevel.INFO, 1, "Database %s on %s@%s:%d", dbName, dbUser, dbHost, dbPort);
        Logger.log(LogLevel.INFO, 1, "Table: %s, users: %s, passwords: %s", dbTable, dbUserColumn, dbPasswordColumn);
        Logger.log(LogLevel.INFO, 1, "Welcome message: %s", welcomeMessage);
        Logger.log(LogLevel.INFO, 1, "Welcome info: %s", welcomeInfo);
        Logger.log(LogLevel.INFO, 1, "Kick message: %s", kickMessage);
    }

}
