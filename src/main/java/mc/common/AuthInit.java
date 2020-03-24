/*
 * (C) 2016 Tomas Kraus
 */
package mc.common;

import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * Mod proxy interface.
 */
public interface AuthInit {

    /**
     * Register event handlers.
     */
    public void register(SimpleChannel channel);

}
