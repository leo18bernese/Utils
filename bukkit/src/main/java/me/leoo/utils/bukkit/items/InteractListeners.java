package me.leoo.utils.bukkit.items;

import com.google.common.annotations.Beta;
import me.leoo.utils.bukkit.events.Events;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@Beta
public class InteractListeners implements Listener {

    private static InteractListeners instance;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        if (itemStack == null || itemStack.getType() == Material.AIR) return;

        InteractItem item = InteractItem.getByItem(itemStack);
        if (item == null) return;

        ItemBuilder builder = item.getItem();

        if (builder.getPermission() != null && !player.hasPermission(builder.getPermission())) {
            event.setCancelled(true);
            return;
        }

        if ((builder.getConfig() != null && builder.getConfigPath() != null && builder.getConfig().executeAction(builder.getConfigPath(), player.getPlayer())) || item.getItem().getInteractCallback() == null) {
            event.setCancelled(true);
            return;
        }

        item.getItem().getInteractCallback().accept(event);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();

        if (itemStack == null || itemStack.getType() == Material.AIR) return;

        InteractItem item = InteractItem.getByItem(itemStack);
        if (item == null) return;

        event.setCancelled(true);
    }

    public static void register() {
        if (instance == null) {
            instance = new InteractListeners();
            Events.register(instance);
        }
    }
}
