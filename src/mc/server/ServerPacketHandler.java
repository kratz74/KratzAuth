/*
 * (C) 2016 Tomas Kraus
 */
package mc.server;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import mc.common.Packet;
import mc.common.PacketContent;
import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

/**
 * Packet handler on server side.
 */
public class ServerPacketHandler implements IPacketHandler {

    /**
     * Process incoming packet.
     * @param inm    Network manager.
     * @param packet Packet payload.
     * @param player Player entity.
     */
    @Override
    public void onPacketData(INetworkManager inm, Packet250CustomPayload packet, Player player) {
        PacketContent content = new PacketContent(packet.data);
        Packet type = content.getType();
        if (type != null) {
            switch(type) {
                case AUTH_RESPONSE:
                    final String userName = content.getField(1);
                    final String password = content.getField(2);
                    Logger.log(LogLevel.INFO,
                            "Recieved %s packet for user %s with password %s", type.getId(), userName, password);
                    AuthResponseHandler handler = new AuthResponseHandler(player, password);
                    handler.handle();
                    break;
                default:
                    Logger.log(LogLevel.INFO, "Recieved %s packet, ignoring it", type.getId());
            }
        } else {
            Logger.log(LogLevel.WARNING, "Recieved unknown packet.");
        }
    }

}
