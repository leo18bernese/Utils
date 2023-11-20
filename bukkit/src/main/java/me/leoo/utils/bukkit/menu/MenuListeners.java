package me.leoo.utils.bukkit.menu;

import me.leoo.utils.bukkit.Utils;
import me.leoo.utils.bukkit.events.Events;
import me.leoo.utils.bukkit.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class MenuListeners implements Listener {

    private static MenuListeners instance;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDrag(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;

        Map<UUID, MenuBuilder> openedInventories = MenuBuilder.getOpenedInventories();

        if (openedInventories.isEmpty()) return;

        Player player = (Player) event.getWhoClicked();
        if (!openedInventories.containsKey(player.getUniqueId())) return;

        MenuBuilder gui = openedInventories.get(player.getUniqueId());
        int rawSlot = event.getRawSlot();

        if (rawSlot < gui.getSlots()) {
            int slot = event.getSlot();

            ItemBuilder item = gui.getItem(rawSlot).orElse(null);
            if (item == null) return;

            if (item.getConfig() != null && item.getConfigPath() != null && item.getConfig().executeAction(item.getConfigPath(), player.getPlayer())) {
                event.setCancelled(true);
                return;
            }

            if (item.getEventCallback() == null) {
                event.setCancelled(true);
            } else {
                event.setCancelled(item.getEventCallback().test(event));
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        MenuBuilder.getOpenedInventories().remove(event.getPlayer().getUniqueId());
    }

    public static void register() {
        if (instance == null) {
            instance = new MenuListeners();
            Events.register(instance);
        }
    }
}
