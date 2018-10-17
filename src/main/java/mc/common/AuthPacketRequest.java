/*
 * (C) 2016 Tomas Kraus
 */
package mc.common;

/**
 * Authentication packet request.
 */
public class AuthPacketRequest extends AuthPacket {

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

}
