/*
 * (C) 2018 Tomas Kraus
 */
package mc.server;

import mc.common.AuthPacketRequest;
import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerRequestPacketHandler implements IMessageHandler<AuthPacketRequest, IMessage> {

	/**
     * Process incoming packet on server side.
     * @param response Authentication response packet.
     * @param ctx Message context.
     * @return Value of {@code null}.
     */
    @Override
    public IMessage onMessage(AuthPacketRequest request, MessageContext ctx) {
        final String userName = request.getName();
        Logger.log(LogLevel.INFO,
                "[ERROR] Recieved authentication request for user %s", userName);
        return null;
    }

}
