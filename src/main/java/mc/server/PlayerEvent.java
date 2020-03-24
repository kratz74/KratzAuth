/*
 * (C) 2016 Tomas Kraus
 */
package mc.server;

import static mc.common.CMAuth.CHANNEL;

import mc.common.AuthPacketRequest;
import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

/**
 * Player events handler.
 */
public class PlayerEvent {

	/**
     * New player logged in.
     * @param event Event data.
     */
    @SubscribeEvent
    public void onPlayerLogin(final PlayerLoggedInEvent event) {
    	final PlayerEntity player = event.getPlayer();
        final String name = player.getDisplayName().getString();
        final AuthPacketRequest request = new AuthPacketRequest(name);
        Logger.log(LogLevel.INFO, "Player %s logged in, sending authentication request.", name);
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), request);
    }

    /**
     * Player has logged off.
     * @param event Event data.
     */
    @SubscribeEvent
    public void onPlayerLogout(final PlayerLoggedOutEvent event) {
    }

}
