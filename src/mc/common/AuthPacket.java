/*
 * (C) 2016 Tomas Kraus
 */
package mc.common;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import java.nio.charset.Charset;

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
        buffer.writeBytes(nameBytes);
    }

    /**
     * Reads packet data from the buffer.
     * @param buffer Data target buffer.
     */
    @Override
    public void fromBytes(ByteBuf buffer) {
        final int len = buffer.readInt();
        final byte[] nameBytes = new byte[len];
        buffer.readBytes(nameBytes);
        name = new String(nameBytes, CHARSET);
    }

}
