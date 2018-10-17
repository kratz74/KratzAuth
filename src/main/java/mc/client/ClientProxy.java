/*
 * (C) 2016 Tomas Kraus
 */
package mc.client;

import static mc.common.CMAuth.CHANNEL;

import mc.common.AuthPacketRequest;
import mc.common.AuthProxy;
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
        CHANNEL.registerMessage(ClientPacketHandler.class, AuthPacketRequest.class, 0, Side.CLIENT);
        //CHANNEL.registerMessage(DummyPacketHandler.class, AuthPacketResponse.class, 0, Side.CLIENT);
    }

}
