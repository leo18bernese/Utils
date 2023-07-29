package me.leoo.utils.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MenuListeners implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDrag(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;

        Map<UUID, MenuBuilder> openedInventories = new HashMap<>(MenuBuilder.getOpenedInventories());

        if(openedInventories.isEmpty())
            return;

        Player whoClicked = (Player) event.getWhoClicked();
        if(!openedInventories.containsKey(whoClicked.getUniqueId())){
            return;
        }

        MenuBuilder gui = openedInventories.get(whoClicked.getUniqueId());
        int rawSlot = event.getRawSlot();

        if(rawSlot < gui.getSlots()){
            int slot = event.getSlot();
            Optional<MenuItem> item = gui.getItem(slot);
            item.ifPresent(menuItem -> event.setCancelled(menuItem.getEventCallBack().accept(event)));
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        MenuBuilder.getOpenedInventories().remove(event.getPlayer().getUniqueId());
    }
}
