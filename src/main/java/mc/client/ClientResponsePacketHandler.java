package mc.client;

import mc.common.AuthPacketResponse;
import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientResponsePacketHandler implements IMessageHandler<AuthPacketResponse, IMessage> {

	/**
     * Process incoming packet on server side.
     * @param response Authentication response packet.
     * @param ctx Message context.
     * @return Value of {@code null}.
     */
	@Override
	public IMessage onMessage(AuthPacketResponse request, MessageContext ctx) {
		final String userName = request.getName();
		Logger.log(LogLevel.INFO, "[ERROR] Recieved authentication response for user %s", userName);
		return null;
	}

}
