/*
 * (C) 2016 Tomas Kraus
 */
package mc.common;

import mc.log.LogLevel;
import mc.log.Logger;
import mc.server.DatabaseLookup;
import mc.server.ServerConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

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
    /** Auth channel discriminator for request. */
    public static int REQUEST_CHANNEL_ID = 0x00;
    /** Auth channel discriminator for response. */
    public static int RESPONSE_CHANNEL_ID = 0x01;

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
