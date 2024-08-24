package me.leoo.utils.bukkit.menu;

import me.leoo.utils.bukkit.Utils;
import me.leoo.utils.bukkit.task.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MenuTask {

    public MenuTask() {
        Tasks.timer(() -> MenuBuilder.getOpenedInventories().entrySet().removeIf(entry -> {
            UUID uuid = entry.getKey();
            Player player = Bukkit.getPlayer(uuid);

            if (player == null || player.getOpenInventory() == null) return true;

            MenuBuilder menu = entry.getValue();

            if (menu.isAutoUpdate()) menu.update();

            return false;
        }), 1, Utils.menuUpdate);
    }
}
