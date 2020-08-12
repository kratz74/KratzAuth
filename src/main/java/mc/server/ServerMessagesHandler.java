/*
 * (C) 2020 Tomas Kraus
 */
package mc.server;

import java.util.function.Supplier;

import mc.common.AuthPacketRequest;
import mc.common.AuthPacketResponse;
import mc.common.BCrypt;
import mc.common.CMAuth;
import mc.log.LogLevel;
import mc.log.Logger;
import mc.utils.PasswordUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Messages handler on client side.
 */
public class ServerMessagesHandler {

	public static boolean checkProtocol(String protocolVersion) {
		return CMAuth.CHANNEL_PROTOCOL_VERSION.equals(protocolVersion);
	}

	/**
     * Process incoming packet on server side.
     * @param response Authentication response packet.
     * @param ctx Message context.
     * @return Value of {@code null}.
     */
    public static void onRequest(AuthPacketRequest request, Supplier<NetworkEvent.Context> ctxSupplier) {
        final String userName = request.getName();
        Logger.log(LogLevel.INFO,
                "[ERROR] Recieved authentication request for user %s", userName);
    }

    /**
     * Process incoming packet on client side.
     * @param response Authentication response packet.
     * @param ctx Message context.
     * @return Value of {@code null}.
     */
	public static void onResponse(AuthPacketResponse response, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		final ServerPlayerEntity player = ctx.getSender();
		final String userName = response.getName();
		final String encryptedPassword = response.getPassword();
		final String playerName = player.getDisplayName().getString();
		boolean passed = false;
		Logger.log(LogLevel.FINEST, "Recieved authentication response for user %s", userName);
		if (!ServerConfig.isEnabled()) {
			Logger.log(LogLevel.FINE, "Skipping authentization of " + player.getDisplayName());
			passed = true;
		} else {
			if (encryptedPassword != null && encryptedPassword.length() > 0) {
				try {
				    final String password = new String(PasswordUtils.decrypt(encryptedPassword));
				    final String hash = DatabaseLookup.getHash(playerName);
				    passed = hash != null && BCrypt.checkpw(password, hash);
				    Logger.log(LogLevel.FINE,
				    		"Credentials check of user " + player.getDisplayName() + (passed ? "passed" : "failed"));
				} catch (Throwable t) {
					Logger.log(LogLevel.WARNING, "Password verification failed: %s", t.getMessage());
				}
			} else {
				Logger.log(LogLevel.WARNING, "Password verification failed: missing password from client!");
			}
		}
		if (passed) {
			player.sendMessage(new StringTextComponent(ServerConfig.getWelcomeMessage() + " " + playerName + "!"));
			player.sendMessage(new StringTextComponent(ServerConfig.getWelcomeInfo()));
		} else {
			player.connection.disconnect(new StringTextComponent(ServerConfig.getKickMessage()));
		}
	}

}
