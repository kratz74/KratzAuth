/*
 * (C) 2016 Tomas Kraus
 */
package mc.server;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import mc.common.BCrypt;
import mc.common.AuthPacketResponse;
import mc.log.LogLevel;
import mc.log.Logger;
import mc.utils.PasswordUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

/**
 * Packet handler on server side.
 */
public class ServerPacketHandler implements IMessageHandler<AuthPacketResponse, IMessage> {

    /**
     * Process incoming packet on client side.
     * @param response Authentication response packet.
     * @param ctx Message context.
     * @return Value of {@code null}.
     */
    @Override
    public IMessage onMessage(AuthPacketResponse response, MessageContext ctx) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        final String userName = response.getName();
        final String encryptedPassword = response.getPassword();
        final String password = new String(PasswordUtils.decrypt(encryptedPassword));
        boolean passed = false;
        Logger.log(LogLevel.FINEST,
                "Recieved authentication response for user %s with password %s", userName, password);
        if (!ServerConfig.isEnabled()) {
            Logger.log(LogLevel.FINE, "Skipping authentization of " + player.getDisplayName());
            passed = true;
        } else {
             final String hash = DatabaseLookup.getHash(player.getDisplayName());
             passed = hash != null && BCrypt.checkpw(password, hash);
             Logger.log(LogLevel.FINE,
                     "Credentials check of user " + player.getDisplayName() + (passed ? "passed" : "failed"));
        }
        final String hash = DatabaseLookup.getHash(player.getDisplayName());
        if (passed) {
            player.addChatMessage(new ChatComponentText(
                    ServerConfig.getWelcomeMessage() + " " + player.getDisplayName() + "!"));
            player.addChatMessage(new ChatComponentText(ServerConfig.getWelcomeInfo()));
        } else {
            player.playerNetServerHandler.kickPlayerFromServer(ServerConfig.getKickMessage());
        }
        return null;
    }

}
