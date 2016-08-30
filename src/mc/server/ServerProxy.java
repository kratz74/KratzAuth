/*
 * (C) 2016 Tomas Kraus
 */
package mc.server;

import cpw.mods.fml.common.registry.GameRegistry;
import mc.common.AuthProxy;
import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * Mod server proxy.
 */
public class ServerProxy implements AuthProxy {
    
    /**
     * Cancel player events.
     */
    public static class CancelEvents {
        
	@ForgeSubscribe
	public void playerInteract(PlayerInteractEvent e) {
		cancel(e);
	}

	@ForgeSubscribe
	public void playerHurt(LivingHurtEvent e) {
		cancel(e);
	}

	@ForgeSubscribe
	public void playerAttack(AttackEntityEvent e) {
		cancel(e);
	}

	@ForgeSubscribe
	public void playerInteractEntity(EntityInteractEvent e) {
		cancel(e);
	}
	
	@ForgeSubscribe
	public void minecartInterract(MinecartCollisionEvent e) {
		cancel (e);
	}

	@ForgeSubscribe
	public void playerFillBucket(FillBucketEvent e) {
		cancel(e);
	}

	@ForgeSubscribe
	public void playerItemPickup(EntityItemPickupEvent e) {
		cancel(e);
	}

	@ForgeSubscribe
	public void playerChat(ServerChatEvent e) {
//		if (!((Boolean) Auth.players.get(e.player)).booleanValue())
//			e.setCanceled(true);
	}

	@ForgeSubscribe
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
        GameRegistry.registerPlayerTracker(new PlayerEvent());
        MinecraftForge.EVENT_BUS.register(new CancelEvents());
    }

}
