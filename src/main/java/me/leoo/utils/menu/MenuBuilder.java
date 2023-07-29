package me.leoo.utils.menu;

import lombok.Data;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Data
public abstract class MenuBuilder {

    @Getter
    private static Map<UUID, MenuBuilder> openedInventories = new ConcurrentHashMap<>();
    private final List<MenuItem> items = new ArrayList<>();

    private static String title;
    private final int rows;

    public abstract List<MenuItem> getItems(Player player);

    public abstract String getTitle(Player player);

    public Inventory get(Player player) {
        Inventory inventory = Bukkit.createInventory(null, getSlots(), getTitle(player));

        getItems().clear();
        getItems().addAll(getItems(player));

        getItems().forEach(item -> {
            int slot = item.getSlot();
            if (slot < 0 || slot > getSlots()) {
                slot = 0;
            }

            inventory.setItem(slot, item.getItemStack());

        });

        return inventory;
    }

    public int getSlots() {
        return 9 * getRows();
    }

    public Optional<MenuItem> getItem(int slot) {
        return items.stream().filter(menuItem -> menuItem.getSlot() == slot).findAny();
    }

    public void open(Player player) {
        player.openInventory(get(player));
        openedInventories.put(player.getUniqueId(), this);
    }

    public void update(Player player) {
        player.getOpenInventory().getTopInventory().setContents(get(player).getContents());
    }
}
