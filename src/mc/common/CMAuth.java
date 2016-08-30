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
import cpw.mods.fml.common.network.NetworkMod;
import mc.client.ClientPacketHandler;
import mc.server.DatabaseLookup;
import mc.server.ServerConfig;
import mc.server.ServerPacketHandler;

/**
 * Mod main class.
 */
@Mod(modid=ModVars.modid, name=ModVars.name, version=ModVars.version)
@NetworkMod(
        clientSideRequired=true, serverSideRequired=true,
        clientPacketHandlerSpec=@NetworkMod.SidedPacketHandler(
                channels={Packet.CHANNEL}, packetHandler=ClientPacketHandler.class),
        serverPacketHandlerSpec=@NetworkMod.SidedPacketHandler(
                channels={Packet.CHANNEL}, packetHandler=ServerPacketHandler.class))
public class CMAuth {
  
    /** Mod main class instance. */
    @Mod.Instance("CMAuth")
    public static CMAuth INSTANCE;

    /** Server and client proxies. */
    @SidedProxy(clientSide="mc.client.ClientProxy", serverSide="mc.server.ServerProxy")
    public static AuthProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
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
