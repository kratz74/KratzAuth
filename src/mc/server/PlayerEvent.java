/*
 * (C) 2016 Tomas Kraus
 */
package mc.server;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import mc.common.AuthPacketRequest;
import static mc.common.CMAuth.CHANNEL;
import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraft.entity.player.EntityPlayerMP;

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
        final String name = event.player.getDisplayName();
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
