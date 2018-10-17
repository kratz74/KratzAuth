/*
 * (C) 2016 Tomas Kraus
 */
package mc.server;

import static mc.common.CMAuth.CHANNEL;

import mc.common.AuthPacketRequest;
import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

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
        final String name = event.player.getDisplayNameString();
        final AuthPacketRequest request = new AuthPacketRequest(name);
        Logger.log(LogLevel.INFO, "Player %s logged in, sending authentication request.", name);
        CHANNEL.sendTo(request, (EntityPlayerMP)event.player);
    }

    /**
     * Player has logged off.
     * @param event Event data.
     */
    @SubscribeEvent
    public void onPlayerLogout(final PlayerLoggedOutEvent event) {
    }

}
