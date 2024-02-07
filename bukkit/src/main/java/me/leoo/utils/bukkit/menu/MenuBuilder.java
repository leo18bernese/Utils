package me.leoo.utils.bukkit.menu;

import lombok.Data;
import lombok.Getter;
import me.leoo.utils.bukkit.chat.CC;
import me.leoo.utils.bukkit.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

@Data
public abstract class MenuBuilder {

    @Getter
    private static Map<UUID, MenuBuilder> openedInventories = new HashMap<>();

    private final List<ItemBuilder> items = new ArrayList<>();

    private static String title;
    private final int rows;

    private boolean autoUpdate;
    private boolean updateOnClick;
    private boolean doubleClick = true;

    public abstract List<ItemBuilder> getItems(Player player);

    public abstract String getTitle(Player player);

    public Inventory get(Player player) {
        MenuListeners.register();

        Inventory inventory = Bukkit.createInventory(null, getSlots(), getTitle(player) == null ? "" : CC.color(getTitle(player)));

        updateContent(player, inventory);

        return inventory;
    }

    private void updateContent(Player player, Inventory inventory) {
        items.clear();
        items.addAll(getItems(player));
        items.forEach(itemBuilder -> {
            int slot = itemBuilder.getSlot();
            if (slot >= 0 && slot <= getSlots()) inventory.setItem(slot, itemBuilder.setDefaultFlags().get());
        });
    }

    public int getSlots() {
        return 9 * rows;
    }

    public Optional<ItemBuilder> getItem(int slot) {
        return items.stream().filter(menuItem -> menuItem.getSlot() == slot).findAny();
    }

    public void open(Player player) {
        player.openInventory(get(player));

        openedInventories.put(player.getUniqueId(), this);
    }

    public void update(Player player) {
        Inventory inventory = player.getOpenInventory().getTopInventory();
        updateContent(player, inventory);
    }

    public void close(Player player) {
        player.closeInventory();
    }

    public void onClose(Player player) {
    }
}
