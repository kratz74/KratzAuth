/*
 * (C) 2016 Tomas Kraus
 */
package mc.common;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Authentication packet.
 */
public class AuthPacket implements IMessage {

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
    @Override
    public void toBytes(ByteBuf buffer) {
        final byte[] nameBytes = name.getBytes(CHARSET);
        buffer.writeInt(nameBytes.length);
        Logger.log(LogLevel.FINE, "AuthPacket to: length: " + nameBytes.length);
        buffer.writeBytes(nameBytes);
        Logger.log(LogLevel.FINE, "AuthPacket to: name: " + name);
    }

    /**
     * Reads packet data from the buffer.
     * @param buffer Data target buffer.
     */
    @Override
    public void fromBytes(ByteBuf buffer) {
        final int len = buffer.readInt();
        Logger.log(LogLevel.FINE, "AuthPacket from: length: " + len);
        final byte[] nameBytes = new byte[len];
        buffer.readBytes(nameBytes, 0, len);
        name = new String(nameBytes, CHARSET);
    }

}
