package me.leoo.utils.velocity.utils;

import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.experimental.UtilityClass;
import me.leoo.utils.velocity.Utils;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

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

    // Player
    public boolean isConnected(Player player, String serverName) {
        return player.getCurrentServer().isPresent() &&
                player.getCurrentServer().get().getServerInfo().getName().equals(serverName);
    }

    public boolean isConnected(Player player, RegisteredServer server) {
        return player.getCurrentServer().isPresent() &&
                player.getCurrentServer().get().getServerInfo().equals(server.getServerInfo());
    }

    public void tryConnect(Player player, RegisteredServer server, Consumer<ConnectionRequestBuilder.Result> consumer, Consumer<Throwable> throwableConsumer) {
        CompletableFuture<ConnectionRequestBuilder.Result> future = player.createConnectionRequest(server).connect();

        future.thenAccept(consumer).exceptionally(throwable -> {
            throwable.printStackTrace();
            throwableConsumer.accept(throwable);
            return null;
        });
    }
}
