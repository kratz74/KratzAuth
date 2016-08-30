/*
 * (C) 2016 Tomas Kraus
 */
package mc.common;

import java.util.HashMap;
import java.util.Map;
import mc.log.LogLevel;
import mc.log.Logger;
import net.minecraft.network.packet.Packet250CustomPayload;

/**
 * Packet types and handling.
 */
public enum Packet {

    AUTH_REQEST("REQ"),
    AUTH_RESPONSE("RSP");

    /** Authentication module packet channel name. */
    public static final String CHANNEL = "MCAuth";

    /** Packet fields separator. */
    public static final char SEPARATOR = ':';

    /** {@link Packet} enumeration length. */
    public static final int LENGTH = Packet.values().length;

    private static final Map<String, Packet> idToPacket = initIdToPacket();

    /**
     * Initialize ID to Packet convertor.
     * Static initialization helper.
     * @return ID to Packet convertor {@link Map}.
     */
    private static final Map<String, Packet> initIdToPacket() {
        Map<String, Packet> map = new HashMap<String, Packet>(LENGTH);
        for (Packet p : Packet.values()) {
            map.put(p.id.toUpperCase(), p);
        }
        return map;
    }

    /**
     * Get {@link Packet} instance corresponding to provided ID.
     * @param id {@link Packet} ID as {@link String}.
     * @return {@link Packet} instance corresponding to provided ID
     *         or {@code null} if ID was not recognized.
     */
    public static Packet get(final String id) {
        return id != null ? idToPacket.get(id.toUpperCase()) : null;
    }

    /**
     * Builds Minecraft packet for password request.<br>
     * Packet content: {@code "<ID>:<player_name>"}.
     * @param playerName Name of player being authorized.
     * @return Minecraft packet with packet type content.
     */
    public static Packet250CustomPayload request(final String playerName) {
        final int playerNameLen = playerName != null ? playerName.length() : 0;
        StringBuilder sb = new StringBuilder(
                AUTH_REQEST.id.length() + playerNameLen + 1);
        sb.append(AUTH_REQEST.id);
        sb.append(SEPARATOR);
        if (playerNameLen > 0) {
            sb.append(playerName);
        }
        String packet = sb.toString();
        Logger.log(LogLevel.INFO, "Request packet content: \"%s\"", packet);
        return build(packet.getBytes());
    }

    /**
     * Builds Minecraft packet for password response.<br>
     * Packet content: {@code "<ID>:<player_name>:<encrypted_password>"}.
     * @param playerName Name of player being authorized.
     * @return Minecraft packet with packet type content.
     */
    public static Packet250CustomPayload response(final String playerName, final String password) {
        final int playerNameLen = playerName != null ? playerName.length() : 0;
        final int passwordLen = password != null ? password.length() : 0;
        StringBuilder sb = new StringBuilder(AUTH_RESPONSE.id.length()
                + playerNameLen + passwordLen + 2);
        sb.append(AUTH_RESPONSE.id);
        sb.append(SEPARATOR);
        if (playerNameLen > 0) {
            sb.append(playerName);
        }
        sb.append(SEPARATOR);
        if (passwordLen > 0) {
            sb.append(password);
        }
        String packet = sb.toString();
        Logger.log(LogLevel.INFO, "Response packet content: \"%s\"", packet);
         return build(packet.getBytes());
    }

    /**
     * Builds Minecraft packet with specified content.
     * @param data Minecraft packet content.
     * @return Minecraft packet with content from {@code build} argument.
     */
    private static Packet250CustomPayload build(final byte[] data) {
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = CHANNEL;
        packet.data = data;
        packet.length = data.length;
        return packet;
    }

    /** Packet {@link String} representation (packet ID). */
    private final String id;

    /**
     * Creates an instance of packet type.
     */
    private Packet(final String id) {
        this.id = id;
    }

    /**
     * Get packet {@link String} representation (packet ID).
     * @return Packet {@link String} representation (packet type).
     */
    public String getId() {
        return id;
    }

}
