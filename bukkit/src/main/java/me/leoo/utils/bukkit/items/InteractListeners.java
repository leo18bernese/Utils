package me.leoo.utils.bukkit.items;

import com.cryptomorin.xseries.XMaterial;
import me.leoo.utils.bukkit.events.Events;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractListeners implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        if (itemStack == null || itemStack.getType() == XMaterial.AIR.parseMaterial()) return;

        InteractItem item = InteractItem.getByItem(itemStack);
        if (item == null) return;

        ItemBuilder builder = item.getItem();

        if (builder.permission() != null && !player.hasPermission(builder.permission())) {
            event.setCancelled(true);
            return;
        }

        if ((builder.config() != null && builder.configPath() != null && builder.config().executeAction(builder.configPath(), player.getPlayer())) || item.getItem().interactCallback() == null) {
            event.setCancelled(true);
            return;
        }

        if (builder.interactRequirement() != null && !builder.interactRequirement().test(event)) {
            event.setCancelled(true);
            return;
        }

        builder.runSound(player);
        builder.interactCallback().accept(event);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();

        if (itemStack == null || itemStack.getType() == XMaterial.AIR.parseMaterial() ||
                itemStack.getAmount() <= 0) return;

        InteractItem item = InteractItem.getByItem(itemStack);
        if (item == null) return;

        event.setCancelled(true);
    }

    public static void register() {
        Events.register(new InteractListeners());
    }
}
