/*
 * (C) 2016 Tomas Kraus
 */
package mc.common;

import java.util.LinkedList;

/**
 * Parses incoming packet data.
 */
public class PacketContent {

    /**
     * Parse packet data and return an array of {@link String} containing
     * individual packet fields.
     * @param data Packet data to be parsed.
     * @return An array of {@link String} containing individual packet fields.
     */
    private static String[] parse(final byte[] data) {
        int dataLen = data != null ? data.length : 0;
        final LinkedList<String> fields = new LinkedList<String>();
        int beg = 0;
        for (int i = 0; i < dataLen; i++) {
            if (data[i] == Packet.SEPARATOR) {
                if (i > beg) {
                    fields.add(new String(data, beg, i - beg));
                } else {
                    fields.add(null);
                }
                beg = i + 1;
            }
        }
        if (beg < dataLen) {
            fields.add(new String(data, beg, dataLen - beg));
        }
        String[] content = new String[fields.size()];
        int i = 0;
        for (String field : fields) {
            content[i++] = field;
        }
        return content;
    }

    /** Packet data fields. */
    private final String[] fields;

    /** Packet type. */
    private final Packet type;

    /**
     * Creates an instance of packet content.
     * @param data Packet data to be parsed.
     */
    public PacketContent(final byte[] data) {
        fields = parse(data);
        type = fields.length > 0 ? Packet.get(fields[0]) : null;
    }

    /**
     * Get packet type.
     * @return Packet type or {@code null} if packet type was not recognized.
     */
    public Packet getType() {
        return type;
    }

    /**
     * Get packet field at the specified index.
     * <ul>
     *   <li>Value at index = 0 is packet ID</li>
     *   <li>Values at index > 0 depends on packet type.
     * </ul>
     * @param index Index of packet field to be returned.
     * @return Value of packet field at the specified index or {@code null}
     *         if there is no value at the specified index.
     */
    public String getField(final int index) {
        return index < fields.length ?fields[index] : null;
    }

}
