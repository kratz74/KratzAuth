/*
 * (C) 2020 Tomas Kraus
 */
package mc.client;

import static mc.common.CMAuth.CHANNEL;

import java.util.function.Supplier;

import mc.common.AuthPacketRequest;
import mc.common.AuthPacketResponse;
import mc.common.CMAuth;
import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Messages handler on client side.
 */
public class ClientMessagesHandler {

	/** Name of property containing user name. */
	private static final String USER_PROPERTY = "mc.user";
	/** Name of property containing encrypted password. */
	private static final String PASSWORD_PROPERTY = "mc.password";

	/** User name received from property. */
	private static final String USER = System.getProperty(USER_PROPERTY);
	/** Encrypted password received from property. */
	private static final String PASSWORD = System.getProperty(PASSWORD_PROPERTY);

	public static boolean checkProtocol(String protocolVersion) {
		return CMAuth.CHANNEL_PROTOCOL_VERSION.equals(protocolVersion);
	}


	/**
	 * Process incoming request packet on client side.
	 *
	 * @param request Authentication request packet.
	 * @param ctx Message context.
	 * @return Authentication response packet.
	 */
	public static void onRequest(final AuthPacketRequest request, Supplier<NetworkEvent.Context> ctxSupplier) {
		final String requestUserName = request.getName();
		Logger.log(LogLevel.INFO, "Recieved authentication request for user %s", requestUserName);
		int count = 0;
		while (Minecraft.getInstance().player == null && count < 60) {
			count++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Logger.log(LogLevel.WARNING, e.getMessage());
			}
		}
		if (USER != null && USER.equals(requestUserName)) {
			Logger.log(LogLevel.INFO, "Sending auth reponse as user %s with password %s", USER, PASSWORD);
			CHANNEL.sendToServer(new AuthPacketResponse(USER, PASSWORD));
		} else {
			Logger.log(LogLevel.WARNING, "User from request does not match local user");
			CHANNEL.sendToServer(new AuthPacketResponse(USER, null));
		}
	}

	/**
     * Process incoming response packet on server side.
     * @param response Authentication response packet.
     * @param ctx Message context.
     * @return Value of {@code null}.
     */
	public static void onResponse(AuthPacketResponse request, Supplier<NetworkEvent.Context> ctxSupplier) {
		final String userName = request.getName();
		Logger.log(LogLevel.INFO, "[ERROR] Recieved authentication response for user %s", userName);
	}

}
