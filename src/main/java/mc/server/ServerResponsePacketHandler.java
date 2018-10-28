/*
 * (C) 2016 Tomas Kraus
 */
package mc.server;

import mc.common.AuthPacketResponse;
import mc.common.BCrypt;
import mc.log.LogLevel;
import mc.log.Logger;
import mc.utils.PasswordUtils;
import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.util.ChatComponentText;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Packet handler on server side.
 */
public class ServerResponsePacketHandler implements IMessageHandler<AuthPacketResponse, IMessage> {

    /**
     * Process incoming packet on client side.
     * @param response Authentication response packet.
     * @param ctx Message context.
     * @return Value of {@code null}.
     */
    @Override
    public IMessage onMessage(AuthPacketResponse response, MessageContext ctx) {
        final EntityPlayerMP player = ctx.getServerHandler().player;
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
             final String hash = DatabaseLookup.getHash(player.getDisplayNameString());
             passed = hash != null && BCrypt.checkpw(password, hash);
             Logger.log(LogLevel.FINE,
                     "Credentials check of user " + player.getDisplayName() + (passed ? "passed" : "failed"));
        }
        if (passed) {
            player.sendMessage(new TextComponentString(
                    ServerConfig.getWelcomeMessage() + " " + player.getDisplayNameString() + "!"));
            player.sendMessage(new TextComponentString(ServerConfig.getWelcomeInfo()));
        } else {
            player.connection.disconnect(new TextComponentString(ServerConfig.getKickMessage()));
        }
        return null;
    }

}
