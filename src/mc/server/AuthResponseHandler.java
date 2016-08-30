/*
 * (C) 2016 Tomas Kraus
 */
package mc.server;

import cpw.mods.fml.common.network.Player;
import hashtest.BCrypt;
import mc.utils.PasswordUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatMessageComponent;

/**
 * Authentication response handler.
 */
public class AuthResponseHandler {

    /** Authenticated player entity. */
    private final EntityPlayer player;

    /** Encrypted password from client. */
    private final String encryptedPassword;
    /**
     * Creates an instance of authentication request handler.
     * @param player   Authenticated player entity.
     * @param password Encrypted password from client.
     */
    public AuthResponseHandler(final Player player, final String password) {
        this.player = (EntityPlayer)player;
        this.encryptedPassword = password;
    }

    /**
     * Authentication response handling method.
     */
    public void handle() {
        final char[] dstPw = PasswordUtils.decrypt(encryptedPassword);
        final String password = new String(dstPw);
        final String hash = DatabaseLookup.getHash(player.getEntityName());
        boolean check = BCrypt.checkpw(password, hash);
        if (check) {
            player.sendChatToPlayer(ChatMessageComponent.createFromText(
                    "Welcome to Magical Minecraft " + player.getEntityName() + "!"));
        } else {
            ((EntityPlayerMP)player).playerNetServerHandler.kickPlayerFromServer("Wrong user name or password.");
        }
    }
    
}
