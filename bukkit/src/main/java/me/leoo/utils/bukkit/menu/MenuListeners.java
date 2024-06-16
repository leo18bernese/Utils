package me.leoo.utils.bukkit.menu;

import me.leoo.utils.bukkit.events.Events;
import me.leoo.utils.bukkit.items.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Map;
import java.util.UUID;

public class MenuListeners implements Listener {

    private static final Map<UUID, MenuBuilder> OPENED_INVENTORIES = MenuBuilder.getOpenedInventories();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;

        Player player = (Player) event.getWhoClicked();

        MenuBuilder menu = OPENED_INVENTORIES.get(player.getUniqueId());
        if (menu == null) return;

        int rawSlot = event.getRawSlot();

        if (rawSlot >= menu.getSlots()) {
            event.setCancelled(true);
            return;
        }

        ItemBuilder item = menu.getItem(rawSlot).orElse(null);
        if (item == null || item.getItemStack().getType() != event.getCurrentItem().getType()) {
            event.setCancelled(true);
            return;
        }

        if (item.getPermission() != null && !player.hasPermission(item.getPermission())) {
            event.setCancelled(true);
            return;
        }

        if (event.getClick() == ClickType.DOUBLE_CLICK && !menu.isDoubleClick()) {
            event.setCancelled(true);
            return;
        }

        if (item.getConfig() != null && item.getConfigPath() != null && item.getConfig().executeAction(item.getConfigPath(), player.getPlayer())) {
            event.setCancelled(true);
            return;
        }

        if (item.getEventCallback() == null) {
            event.setCancelled(true);
        } else {
            event.setCancelled(item.getEventCallback().test(event));
        }

        if (menu.isUpdateOnClick()) menu.update();
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (!MenuBuilder.getOpenedInventories().containsKey(player.getUniqueId())) return;

        MenuBuilder menu = MenuBuilder.getOpenedInventories().get(player.getUniqueId());
        if (menu == null) return;

        menu.onClose();

        MenuBuilder.getOpenedInventories().remove(player.getUniqueId());
    }

    public static void register() {
        Events.register(new MenuListeners());
    }
}
