/*
 * (C) 2016 Tomas Kraus
 */
package mc.common;

import io.netty.buffer.ByteBuf;
import mc.log.LogLevel;
import mc.log.Logger;

/**
 * Authentication packet response.
 */
public class AuthPacketResponse extends AuthPacket {

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
    @Override
    public void toBytes(ByteBuf buffer) {
        super.toBytes(buffer);
        final byte[] nameBytes = password != null ? password.getBytes(CHARSET) : null;
        buffer.writeInt(nameBytes != null ? nameBytes.length : 0);
        Logger.log(LogLevel.FINE, "AuthPacketResponse to: length: " + (nameBytes != null ? nameBytes.length : 0));
        if (password != null) {
            buffer.writeBytes(nameBytes);
            Logger.log(LogLevel.FINE, "AuthPacketResponse to: password bytes: " + nameBytes.length);
        }
    }

    /**
     * Reads packet data from the buffer.
     * @param buffer Data target buffer.
     */
    @Override
    public void fromBytes(ByteBuf buffer) {
        super.fromBytes(buffer);
        final int len = buffer.readInt();
        Logger.log(LogLevel.FINE, "AuthPacketResponse from: length: " + len);
        if (len > 0) {
            final byte[] nameBytes = new byte[len];
            buffer.readBytes(nameBytes);
            password = new String(nameBytes, CHARSET);
        } else {
            password = null;
        }
    }

}
