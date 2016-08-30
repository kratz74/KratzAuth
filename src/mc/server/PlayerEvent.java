/*
 * (C) 2016 Tomas Kraus
 */
package mc.server;

import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mc.common.Packet;
import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

/**
 * Player events handler.
 */
public class PlayerEvent implements IPlayerTracker {

    /**
     * New player logged in.
     * @param ep Player entity.
     */
    @Override
    public void onPlayerLogin(EntityPlayer ep) {
        String name = ep.getEntityName();
        Logger.log(LogLevel.INFO, "Player %s logged in, sending %s", name, Packet.AUTH_REQEST.getId());
        Packet250CustomPayload packet = Packet.request(name);
        PacketDispatcher.sendPacketToPlayer(packet, (Player)ep);
    }

    /**
     * Player has logged off.
     * @param ep Player entity.
     */
    @Override
    public void onPlayerLogout(EntityPlayer ep) {
    }

    /**
     * Player has changed dimension.
     * @param ep Player entity.
     */
    @Override
    public void onPlayerChangedDimension(EntityPlayer ep) {
    }

    /**
     * Player has re-spawned.
     * @param ep Player entity.
     */
    @Override
    public void onPlayerRespawn(EntityPlayer ep) {
    }
    
}
