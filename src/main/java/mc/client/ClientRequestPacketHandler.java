/*
 * (C) 2016 Tomas Kraus
 */
package mc.client;

import mc.common.AuthPacketRequest;
import mc.common.AuthPacketResponse;
import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Packet handler on client side.
 */
public class ClientRequestPacketHandler implements IMessageHandler<AuthPacketRequest, AuthPacketResponse> {

	/** Name of property containing user name. */
	private static final String USER_PROPERTY = "mc.user";
	/** Name of property containing encrypted password. */
	private static final String PASSWORD_PROPERTY = "mc.password";

	/** User name received from property. */
	private static final String USER = System.getProperty(USER_PROPERTY);
	/** Encrypted password received from property. */
	private static final String PASSWORD = System.getProperty(PASSWORD_PROPERTY);

	/**
	 * Process incoming packet on client side.
	 *
	 * @param request Authentication request packet.
	 * @param ctx Message context.
	 * @return Authentication response packet.
	 */
	@Override
	public AuthPacketResponse onMessage(AuthPacketRequest request, MessageContext ctx) {
		Logger.log(LogLevel.INFO, Minecraft.getMinecraft().toString());
		final String requestUserName = request.getName();
		Logger.log(LogLevel.INFO, "Recieved authentication request for user %s", requestUserName);
		int count = 0;
		while (Minecraft.getMinecraft().player == null && count < 60) {
			count++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Logger.log(LogLevel.WARNING, e.getMessage());
			}
		}
		if (USER != null && USER.equals(requestUserName)) {
			Logger.log(LogLevel.INFO, "Sending auth reponse as user %s with password %s", USER, PASSWORD);
			return new AuthPacketResponse(USER, PASSWORD);
		} else {
			Logger.log(LogLevel.WARNING, "User from request does not match local user");
			return new AuthPacketResponse(USER, null);
		}
	}

}
