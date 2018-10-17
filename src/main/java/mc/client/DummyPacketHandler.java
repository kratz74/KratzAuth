/*
 * (C) 2016 Tomas Kraus
 */
package mc.client;

import mc.common.AuthPacketResponse;
import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Dummy packet handler.
 */
public class DummyPacketHandler implements IMessageHandler<AuthPacketResponse, IMessage> {
    
        public DummyPacketHandler() {
        }

        @Override
        public IMessage onMessage(final AuthPacketResponse req, final MessageContext mc) {
            Logger.log(LogLevel.INFO, "Message AuthPacketResponse recieved");
            return null;
        }

}
