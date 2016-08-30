/*
 * (C) 2016 Tomas Kraus
 */
package mc.client;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mc.common.Packet;
import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

/**
 * Authentication request handler.
 */
public class AuthRequestHandler {

    /** Name of property containing encrypted password. */
    private static final String PASSWORD_PROPERTY = "mc.password";

    /** Encrypted password received from property. */
    private static final String PASSWORD = System.getProperty(PASSWORD_PROPERTY);

    /** User name from request. */
    private final String requestUserName;

    /** Local player entity. */
    private final EntityPlayer player;

    /**
     * Creates an instance of authentication request handler.
     * @param player          Local player entity.
     * @param requestUserName User name from request.
     */
    public AuthRequestHandler(final Player player, final String requestUserName) {
        this.requestUserName = requestUserName;
        this.player = (EntityPlayer)player;
    }

    /**
     * Authentication request handling method.
     */
    public void handle() {
        final String localUserName = player != null ?  player.getEntityName() : null;
        if (localUserName != null && localUserName.equals(requestUserName)) {
            Logger.log(LogLevel.INFO, "Sending auth reponse as user %s with password %s", localUserName, PASSWORD);
            Packet250CustomPayload packet = Packet.response(requestUserName, PASSWORD);
            PacketDispatcher.sendPacketToServer(packet);
        } else {
            Logger.log(LogLevel.WARNING, "User from request does not match local user");
            Packet250CustomPayload packet = Packet.response(requestUserName, null);
            PacketDispatcher.sendPacketToServer(packet);
        }
    }

}
