/*
 * (C) 2016 Tomas Kraus
 */
package mc.client;

import static mc.common.CMAuth.CHANNEL;

import mc.common.AuthPacketRequest;
import mc.common.AuthPacketResponse;
import mc.common.AuthProxy;
import mc.common.CMAuth;
import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Mod client proxy.
 */
public class ClientProxy implements AuthProxy {

    /**
     * Register server event handlers.
     */
    @Override
    public void register() {
        Logger.log(LogLevel.INFO, "Registering client event handlers");
        CHANNEL.registerMessage(ClientRequestPacketHandler.class, AuthPacketRequest.class, CMAuth.REQUEST_CHANNEL_ID, Side.CLIENT);
        CHANNEL.registerMessage(ClientResponsePacketHandler.class, AuthPacketResponse.class, CMAuth.RESPONSE_CHANNEL_ID, Side.CLIENT);
    }

}
