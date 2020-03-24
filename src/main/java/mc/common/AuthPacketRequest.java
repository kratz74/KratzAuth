/*
 * (C) 2016 Tomas Kraus
 */
package mc.common;

import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraft.network.PacketBuffer;

/**
 * Authentication packet request.
 */
public class AuthPacketRequest extends AuthPacket {

	/** Channel Message ID. */
	public static final byte ID = 0x01;

	/**
     * Creates an empty instance of authentication packet request.
     */
    public AuthPacketRequest() {
        super(null);
    }

    /**
     * Creates an instance of authentication packet request.
     */
    public AuthPacketRequest(final String name) {
        super(name);
    }

    /**
     * Reads packet data from the buffer.
     * @param buffer Data target buffer.
     */
    public static AuthPacketRequest decode(PacketBuffer buffer) {
        return new AuthPacketRequest(decodeName(buffer));
    }

}
