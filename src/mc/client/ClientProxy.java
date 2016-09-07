/*
 * (C) 2016 Tomas Kraus
 */
package mc.client;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import mc.common.AuthPacketRequest;
import mc.common.AuthPacketResponse;
import mc.common.AuthProxy;
import static mc.common.CMAuth.CHANNEL;
import mc.log.LogLevel;
import mc.log.Logger;

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
        CHANNEL.registerMessage(ClientPacketHandler.class, AuthPacketRequest.class, 0, Side.CLIENT);
        //CHANNEL.registerMessage(DummyPacketHandler.class, AuthPacketResponse.class, 0, Side.CLIENT);
    }

}
