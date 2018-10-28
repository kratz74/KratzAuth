/*
 * (C) 2016 Tomas Kraus
 */
package mc.server;

import static mc.common.CMAuth.CHANNEL;

import mc.common.AuthPacketRequest;
import mc.common.AuthPacketResponse;
import mc.common.AuthProxy;
import mc.common.CMAuth;
import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Mod server proxy.
 */
public class ServerProxy implements AuthProxy {
    
    /**
     * Cancel player events.
     */
    public static class CancelEvents {
        
	@SubscribeEvent
	public void playerInteract(PlayerInteractEvent e) {
		cancel(e);
	}

	@SubscribeEvent
	public void playerHurt(LivingHurtEvent e) {
		cancel(e);
	}

	@SubscribeEvent
	public void playerAttack(AttackEntityEvent e) {
		cancel(e);
	}
	@SubscribeEvent
	public void playerInteractEntity(PlayerInteractEvent.EntityInteract e) {
		cancel(e);
	}
	
	@SubscribeEvent
	public void minecartInterract(MinecartCollisionEvent e) {
		cancel (e);
	}

	@SubscribeEvent
	public void playerFillBucket(FillBucketEvent e) {
		cancel(e);
	}

	@SubscribeEvent
	public void playerItemPickup(EntityItemPickupEvent e) {
		cancel(e);
	}

	@SubscribeEvent
	public void playerChat(ServerChatEvent e) {
//		if (!((Boolean) Auth.players.get(e.player)).booleanValue())
//			e.setCanceled(true);
	}

	@SubscribeEvent
	public void playerCmd(CommandEvent e) {
//		if (Vars.modEnabled) {
//			if (((e.sender instanceof EntityPlayer))
//					&& (!((Boolean) Auth.players.get((EntityPlayer) e.sender))
//							.booleanValue()))
//				e.setCanceled(true);
//		}
	}

	private void cancel(EntityEvent e) {
//		if (Vars.modEnabled) {
//			if (((e.entity instanceof EntityPlayer))
//					&& (!((Boolean) Auth.players.get((EntityPlayer) e.entity))
//							.booleanValue()))
//				e.setCanceled(true);
//		}
	}

    }

    /**
     * Register server event handlers.
     */
    @Override
    public void register() {
        Logger.log(LogLevel.INFO, "Registering server event handlers");
        FMLCommonHandler.instance().bus().register(new PlayerEvent());
        MinecraftForge.EVENT_BUS.register(new CancelEvents());
        CHANNEL.registerMessage(ServerResponsePacketHandler.class, AuthPacketResponse.class, CMAuth.RESPONSE_CHANNEL_ID, Side.SERVER);
        CHANNEL.registerMessage(ServerRequestPacketHandler.class, AuthPacketRequest.class, CMAuth.REQUEST_CHANNEL_ID, Side.SERVER);
    }

}
