package me.leoo.utils.velocity.utils;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.experimental.UtilityClass;
import me.leoo.utils.velocity.Utils;

import java.util.UUID;

@UtilityClass
public class VelocityUtils {

    public Player getPlayerByUuid(UUID uuid) {
        return Utils.getInstance().getProxy().getPlayer(uuid).orElse(null);
    }

    public Player getPlayerByUuid(String uuid) {
        return getPlayerByUuid(UUID.fromString(uuid));
    }

    public Player getPlayerByName(String name) {
        return Utils.getInstance().getProxy().getPlayer(name).orElse(null);
    }

    public ServerConnection getPlayerServer(Player player) {
        return player.getCurrentServer().orElse(null);
    }

    public RegisteredServer getServer(String serverName) {
        return Utils.getInstance().getProxy().getServer(serverName).orElse(null);
    }
}
