package me.leoo.utils.bukkit.items;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.annotations.Beta;
import me.leoo.utils.bukkit.events.Events;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

public class InteractListeners implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        if (itemStack == null || itemStack.getType() == XMaterial.AIR.parseMaterial()) return;

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

        if (itemStack == null || itemStack.getType() == XMaterial.AIR.parseMaterial()) return;

        InteractItem item = InteractItem.getByItem(itemStack);
        if (item == null) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        InteractItem.getItems().stream().filter(item -> item.getPlayer().equals(player)).collect(Collectors.toList()).clear();
    }

    public static void register() {
        Events.register(new InteractListeners());
    }
}
