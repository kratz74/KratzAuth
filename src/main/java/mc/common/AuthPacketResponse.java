/*
 * (C) 2016 Tomas Kraus
 */
package mc.common;

import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraft.network.PacketBuffer;

/**
 * Authentication packet response.
 */
public class AuthPacketResponse extends AuthPacket {

	/** Channel Message ID. */
	public static final byte ID = 0x02;

	/** Player encrypted password. */
    private String password;

    /**
     * Creates an enpty instance of authentication packet response.
     */
    public AuthPacketResponse() {
        super(null);
    }

    /**
     * Creates an instance of authentication packet response.
     */
    public AuthPacketResponse(final String name, final String password) {
        super(name);
        this.password = password;
    }

    /**
     * Gets player password stored in packet.
     * @return Player password stored in packet.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Writes packet data to the buffer.
     * @param buffer Data target buffer.
     */
    public static void encode(final AuthPacketResponse msg, PacketBuffer buffer) {
    	AuthPacket.encode(msg, buffer);
        final byte[] nameBytes = msg.password != null ? msg.password.getBytes(CHARSET) : null;
        buffer.writeInt(nameBytes != null ? nameBytes.length : 0);
        Logger.log(LogLevel.FINE, "AuthPacketResponse to: length: " + (nameBytes != null ? nameBytes.length : 0));
        if (msg.password != null) {
            buffer.writeBytes(nameBytes);
            Logger.log(LogLevel.FINE, "AuthPacketResponse to: password bytes: " + nameBytes.length);
        }
    }

    /**
     * Reads packet data from the buffer.
     * @param buffer Data target buffer.
     */
    public static AuthPacketResponse decode(PacketBuffer buffer) {
    	final String name = decodeName(buffer);
        final int len = buffer.readInt();
        Logger.log(LogLevel.FINE, "AuthPacketResponse from: length: " + len);
        final String password;
        if (len > 0) {
            final byte[] nameBytes = new byte[len];
            buffer.readBytes(nameBytes);
            password = new String(nameBytes, CHARSET);
        } else {
            password = null;
        }
        return new AuthPacketResponse(name, password);
    }

}
