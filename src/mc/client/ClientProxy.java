/*
 * (C) 2016 Tomas Kraus
 */
package mc.client;

import mc.common.AuthProxy;
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
    }

}
