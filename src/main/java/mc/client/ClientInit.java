/*
 * (C) 2016 Tomas Kraus
 */
package mc.client;

import mc.common.AuthPacketRequest;
import mc.common.AuthPacketResponse;
import mc.common.AuthInit;
import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_CLIENT;

import java.util.Optional;

/**
 * Mod initialization on client side.
 */
public class ClientInit implements AuthInit {

    /**
     * Register server event handlers.
     */
    @Override
    public void register(SimpleChannel channel) {
        Logger.log(LogLevel.INFO, "Registering client event handlers");

		channel.registerMessage(AuthPacketRequest.ID, AuthPacketRequest.class, AuthPacketRequest::encode,
				AuthPacketRequest::decode, ClientMessagesHandler::onRequest, Optional.of(PLAY_TO_CLIENT));

		channel.registerMessage(AuthPacketResponse.ID, AuthPacketResponse.class, AuthPacketResponse::encode,
				AuthPacketResponse::decode, ClientMessagesHandler::onResponse, Optional.of(PLAY_TO_CLIENT));
	}

}
