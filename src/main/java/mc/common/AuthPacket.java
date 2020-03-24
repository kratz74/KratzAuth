/*
 * (C) 2016 Tomas Kraus
 */
package mc.common;

import java.nio.charset.Charset;

import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraft.network.PacketBuffer;

/**
 * Authentication packet.
 */
public class AuthPacket {

    /** String characters set. */
    protected static final Charset CHARSET = Charset.forName("UTF-8");

    /** Player name. */
    private String name;

    /**
     * Creates an instance of authentication packet.
     */
    AuthPacket(final String name) {
        this.name = name;
    }

    /**
     * Gets player name stored in packet.
     * @return Player name stored in packet.
     */
    public String getName() {
        return name;
    }

    /**
     * Writes packet data to the buffer.
     * @param buffer Data target buffer.
     */
    public static void encode(final AuthPacket msg, PacketBuffer buffer) {
        final byte[] nameBytes = msg.name.getBytes(CHARSET);
        buffer.writeInt(nameBytes.length);
        Logger.log(LogLevel.FINE, "AuthPacket to: length: " + nameBytes.length);
        buffer.writeBytes(nameBytes);
        Logger.log(LogLevel.FINE, "AuthPacket to: name: " + msg.name);
    }

    /**
     * Reads packet data from the buffer.
     * @param buffer Data target buffer.
     */
    protected static String decodeName(PacketBuffer buffer) {
        final int len = buffer.readInt();
        Logger.log(LogLevel.FINE, "AuthPacket from: length: " + len);
        final byte[] nameBytes = new byte[len];
        buffer.readBytes(nameBytes, 0, len);
        return new String(nameBytes, CHARSET);
    }

}
