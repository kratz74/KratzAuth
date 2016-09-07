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
        final String password = response.getPassword();
        Logger.log(LogLevel.INFO,
                "Recieved authentication response for user %s with password %s", userName, password);
        final String hash = DatabaseLookup.getHash(player.getDisplayName());
        boolean check = hash != null && BCrypt.checkpw(password, hash);
        if (check) {
            player.addChatMessage(new ChatComponentText("Welcome to the Lord of the Rings Minecraft " + player.getDisplayName() + "!"));
        } else {
            player.playerNetServerHandler.kickPlayerFromServer("Wrong user name or password.");
        }
        return null;
    }

}
