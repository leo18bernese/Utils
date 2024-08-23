package me.leoo.utils.bukkit.bukkit;

import lombok.RequiredArgsConstructor;
import me.leoo.utils.bukkit.Utils;
import me.leoo.utils.bukkit.events.Events;
import me.leoo.utils.common.http.HttpUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class BukkitUpdateCheck {

    private final String id;
    private final Source source;

    private String currentVersion;
    private String latestVersion;

    public void init() {
        this.currentVersion = Utils.getInitializedFrom().getDescription().getVersion();

        this.latestVersion = HttpUtil.get(source.getUrl(id)).thenApply(response -> {
            if (!response.success()) {
                return null;
            }

            return response.text();
        }).join();

        Events.subscribe(PlayerJoinEvent.class, event -> send(event.getPlayer()));
    }

    public void send(Player player) {
        if (isAvailable() && player.isOp() || player.hasPermission("*")) {
            player.sendMessage("§a§m---------------------------------");
            player.sendMessage("§7An update is available for §a" + Utils.getInitializedFrom().getDescription().getName() + "!");
            player.sendMessage("§7Current version: §e" + currentVersion);
            player.sendMessage("§7Latest version: §e" + latestVersion);
            player.sendMessage("§a§m---------------------------------");
        }
    }

    public boolean isAvailable() {
        String currentVersion = Utils.getInitializedFrom().getDescription().getVersion();

        return latestVersion != null && !currentVersion.equals(latestVersion);
    }

    @RequiredArgsConstructor
    public enum Source {
        SPIGOT("https://api.spigotmc.org/legacy/update.php?resource=%s"),
        POLYMART("https://api.polymart.org/v1/getResourceInfoSimple/?resource_id=%s&key=version");

        private final String url;

        private String getUrl(String id) {
            return String.format(url, id);
        }
    }
}
