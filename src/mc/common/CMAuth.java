/*
 * (C) 2016 Tomas Kraus
 */
package mc.common;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import mc.log.LogLevel;
import mc.log.Logger;
import mc.server.DatabaseLookup;
import mc.server.ServerConfig;

/**
 * Mod main class.
 */
@Mod(modid=ModVars.modid, name=ModVars.name, version=ModVars.version)

public class CMAuth {
  
    /** Mod main class instance. */
    @Mod.Instance("KratzAuth")
    public static CMAuth INSTANCE;

    /** Minecraft networking wrapper. */
    public static SimpleNetworkWrapper CHANNEL;

    /** Server and client proxies. */
    @SidedProxy(clientSide="mc.client.ClientProxy", serverSide="mc.server.ServerProxy")
    public static AuthProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        Logger.log(LogLevel.INFO, "initializing mod");
        CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel("KratzAuth");
        // Server configuration and database lookup initialization.
        if (e.getSide().isServer()) {
            ServerConfig.init(e);
            DatabaseLookup.init();
        }
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent e) {
        proxy.register();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {
    }

}
