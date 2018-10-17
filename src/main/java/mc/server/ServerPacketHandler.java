/*
 * (C) 2016 Tomas Kraus
 */
package mc.server;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import mc.common.BCrypt;
import mc.common.AuthPacketResponse;
import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.util.ChatComponentText;
import net.minecraft.util.text.TextComponentString;

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
        final EntityPlayerMP player = ctx.getServerHandler().player;
        final String userName = response.getName();
        final String password = response.getPassword();
        Logger.log(LogLevel.INFO,
                "Recieved authentication response for user %s with password %s", userName, password);
        final String hash = DatabaseLookup.getHash(player.getDisplayNameString());
        boolean check = hash != null && BCrypt.checkpw(password, hash);
        if (check) {
            player.sendMessage(new TextComponentString("Welcome to the Lord of the Rings Minecraft " + player.getDisplayName() + "!"));
        } else {
            player.connection.disconnect(new TextComponentString("Wrong user name or password."));
        }
        return null;
    }

}
