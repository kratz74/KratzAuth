/*
 * (C) 2016 Tomas Kraus
 */
package mc.server;

import org.apache.commons.lang3.tuple.Pair;

import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

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

    private static ForgeConfigSpec SPEC;

    /**
     * Get database connection host name or IP.
     * @return Database connection host name or IP.
     */
    public static String getDbHost() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.dbHost.get();
    }

    /**
     * Get database connection port.
     * @return Database connection port.
     */
    public static int getDbPort() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.dbPort.get();
    }

    /**
     * Get database connection user name.
     * @return Database connection user name.
     */
    public static String getDbUser() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.dbUser.get();
    }

    /**
     * Get database connection user password.
     * @return Database connection user password.
     */
    public static String getDbPassword() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.dbPassword.get();
    }

    /**
     * Get database name.
     * @return Database name.
     */
    public static String getDbName() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.dbName.get();
    }

    /**
     * Get database table with user credentials.
     * @return Database table with user credentials.
     */
    public static String getDbTable() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.dbTable.get();
    }

    /**
     * Get database table column with user name.
     * @return Database table column with user name.
     */
    public static String getDbUserColumn() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.dbUserColumn.get();
    }

    /**
     * Get database table column with user password.
     * @return Database table column with user password.
     */
    public static String getDbPasswordColumn() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.dbPasswordColumn.get();
    }

    /**
     * Check whether users authentication is enabled.
     * @return Value of {@code true} if users authentication is enabled or {@code false} otherwise.
     */
    public static boolean isEnabled() {
        return INSTANCE.enabled.get();
    }
    /**
     * Get server welcome message after successful login.
     * @return Server welcome message.
     */
    public static String getWelcomeMessage() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.welcomeMessage.get();
    }
    /**
     * Get server welcome info after successful login.
     * @return Server welcome info.
     */
    public static String getWelcomeInfo() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.welcomeInfo.get();
    }
    /**
     * Get server kick message after login failure.
     * @return Server kick message.
     */
    public static String getKickMessage() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Server configuration is not yet available");
        }
        return INSTANCE.kickMessage.get();
    }

    /**
     * Initialize server configuration.
     */
    public static void init() {
    	ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        final Pair<ServerConfig, ForgeConfigSpec> specPair = builder.configure(ServerConfig::new);
    	SPEC = specPair.getRight();
    	INSTANCE = specPair.getLeft();
    }

    public static void register(final ModLoadingContext context) {
    	context.registerConfig(ModConfig.Type.COMMON, SPEC);
    }

    public static void save() {
    	SPEC.save();
    }

    public static void log() {
        Logger.log(LogLevel.INFO, 1, "Database %s on %s@%s:%d", getDbName(), getDbUser(), getDbHost(), getDbPort());
        Logger.log(LogLevel.INFO, 1, "Table: %s, users: %s, passwords: %s", getDbTable(), getDbUserColumn(), getDbPasswordColumn());
        Logger.log(LogLevel.INFO, 1, "Welcome message: %s", getWelcomeMessage());
        Logger.log(LogLevel.INFO, 1, "Welcome info: %s", getWelcomeInfo());
        Logger.log(LogLevel.INFO, 1, "Kick message: %s", getKickMessage());
    }

    /** Database connection host name or IP. */
    private final ForgeConfigSpec.ConfigValue<String> dbHost;

    /** Database connection port. */
    private final ForgeConfigSpec.ConfigValue<Integer> dbPort;

    /** Database connection user name. */
    private final ForgeConfigSpec.ConfigValue<String> dbUser;

    /** Database connection user password. */
    private final ForgeConfigSpec.ConfigValue<String> dbPassword;

    /** Database name. */
    private final ForgeConfigSpec.ConfigValue<String> dbName;

    /** Database table with user credentials.*/
    private final ForgeConfigSpec.ConfigValue<String> dbTable;

    /** Database table column with user name. */
    private final ForgeConfigSpec.ConfigValue<String> dbUserColumn;

    /** Database table column with user password. */
    private final ForgeConfigSpec.ConfigValue<String> dbPasswordColumn;

    /** Enable/disable users authentication. */
    private final ForgeConfigSpec.ConfigValue<Boolean> enabled;

    /** Welcome message after successful login. */
    private final ForgeConfigSpec.ConfigValue<String> welcomeMessage;

    /** Welcome information after successful login. */
    private final ForgeConfigSpec.ConfigValue<String> welcomeInfo;

    /** Kick message after login failure. */
    private final ForgeConfigSpec.ConfigValue<String> kickMessage;

    /**
     * Creates an instance of server configuration object.
     * @param builder Configuration builder.
     */
    private ServerConfig(final ForgeConfigSpec.Builder builder) {
    	enabled = builder
    			.comment("Enable/disable users authentication")
    			.define("Enabled", true);
        builder.push(DB_CATEGORY);
    	dbHost = builder
    			.comment("Database connection host name or IP")
    			.define("DbHost", "127.0.0.1");
    	dbPort = builder
    			.comment("Database connection port")
    			.define("DbPort", 3306);
    	dbUser = builder
    			.comment("Database connection user name")
    			.define("DbUser", "myUser");
    	dbPassword = builder
    			.comment("Database connection user password")
    			.define("DbPassword", "myPassword");
    	dbName = builder
    			.comment("Database name")
    			.define("DbName", "forum");
    	dbTable = builder
    			.comment("Database table with user credentials")
    			.define("DbUserTable", "forum_users");
    	dbUserColumn = builder
    			.comment("Database table column with user name")
    			.define("DbUserColumn", "username");
    	dbPasswordColumn = builder
    			.comment("Database table column with user password")
    			.define("DbPasswordColumn", "user_password");
    	builder.pop();
    	builder.push(LOGIN);
    	welcomeMessage = builder
    			.comment("Welcome message to display on login")
    			.define("WelcomeMessage", "Welcome to the carovnak.cz Minecraft");
    	welcomeInfo = builder
    			.comment("Welcome information to display on login")
    			.define("WelcomeInfo", "Check http://www.carovnak.cz for news.");
    	kickMessage = builder
    			.comment("Kick message after login failure")
    			.define("KickMessage", "Wrong user name or password. Check your registration on http://www.carovnak.cz web.");
    	builder.pop();
    }

}
