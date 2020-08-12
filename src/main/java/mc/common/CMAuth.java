/*
 * (C) 2016 Tomas Kraus
 */
package mc.common;

import io.netty.util.ResourceLeakDetector.Level;
import mc.client.ClientInit;
import mc.client.ClientMessagesHandler;
import mc.log.LogLevel;
import mc.log.Logger;
import mc.server.DatabaseLookup;
import mc.server.ServerConfig;
import mc.server.ServerMessagesHandler;
import mc.server.ServerInit;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * Mod main class.
 */
@Mod(value=ModVars.modid)
public class CMAuth {
  
    /** Mod main class instance. */
    public static CMAuth INSTANCE;

    /** Auth protocol version. */
    public static final String CHANNEL_PROTOCOL_VERSION = "1.0";
    /** Auth networking channel name. */
    private static final String CHANNEL_NAME = "auth";
    /** Auth networking channel resource location. */
    public static final ResourceLocation CHANNEL_RL = new ResourceLocation(ModVars.modid, CHANNEL_NAME);
    /** Minecraft networking simple channel for auth messages. */
    public static SimpleChannel CHANNEL;

    /** Reference to the event bus for this mod. Registration events are fired on this bus. */
    public static IEventBus MOD_EVENT_BUS;

    /** Server or client setup. */
    public static AuthInit init;

    public CMAuth() {
    	INSTANCE = this;
    	MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
    	MOD_EVENT_BUS.addListener(this::setup);
    	MOD_EVENT_BUS.addListener(this::config);
    	MOD_EVENT_BUS.addListener(this::clientSetup);
    	MOD_EVENT_BUS.addListener(this::serverSetup);
    	ServerConfig.init();
    	ServerConfig.register(ModLoadingContext.get());
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent e) {
        Logger.log(LogLevel.INFO, "Initializing " + ModVars.name + " " + ModVars.version);
        CHANNEL = NetworkRegistry.newSimpleChannel(
        		CHANNEL_RL, () -> CHANNEL_PROTOCOL_VERSION,
        		ClientMessagesHandler::checkProtocol, ServerMessagesHandler::checkProtocol);
    }

    @SubscribeEvent
    public void config(ModConfig.ModConfigEvent e) {
    	Logger.log(LogLevel.INFO, "Processing configuration event " + e.getConfig().getType());
    	ServerConfig.log();
    }
    
    @SubscribeEvent 
    public void clientSetup(FMLClientSetupEvent e) {
    	Logger.log(LogLevel.INFO, "Processing client setup event");
    	init = new ClientInit();
    	init.register(CHANNEL);
    	
    }

    @SubscribeEvent 
    public void serverSetup(FMLDedicatedServerSetupEvent e) {
    	Logger.log(LogLevel.INFO, "Processing server setup event");
    	init = new ServerInit();
    	init.register(CHANNEL);
    	ServerConfig.save();
    	DatabaseLookup.init();
    }

}
