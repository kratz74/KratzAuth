/*
 * (C) 2016 Tomas Kraus
 */
package mc.client;

import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import mc.common.AuthPacketRequest;
import mc.common.AuthPacketResponse;
import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Packet handler on client side.
 */
public class ClientPacketHandler implements IMessageHandler<AuthPacketRequest, AuthPacketResponse> {

    /** Name of property containing encrypted password. */
    private static final String PASSWORD_PROPERTY = "mc.password";

    /** Encrypted password received from property. */
    private static final String PASSWORD = System.getProperty(PASSWORD_PROPERTY);

    /**
     * Process incoming packet on client side.
     * @param request Authentication request packet.
     * @param ctx Message context.
     * @return Authentication response packet.
     */
    @Override
    public AuthPacketResponse onMessage(AuthPacketRequest request, MessageContext ctx) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        final String localUserName = player.getDisplayName();
        final String requestUserName = request.getName();
        Logger.log(LogLevel.INFO, "Recieved authentication request for user %s", requestUserName);
        if (localUserName != null && localUserName.equals(requestUserName)) {
            Logger.log(LogLevel.INFO, "Sending auth reponse as user %s with password %s", localUserName, PASSWORD);
            return new AuthPacketResponse(localUserName, PASSWORD);
        } else {
            Logger.log(LogLevel.WARNING, "User from request does not match local user");
            return new AuthPacketResponse(localUserName, null);
        }
    }

}
