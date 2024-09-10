package me.leoo.utils.bukkit.menu;

import lombok.Data;
import lombok.Getter;
import me.leoo.utils.bukkit.chat.CC;
import me.leoo.utils.bukkit.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Data
public abstract class MenuBuilder {

    @Getter
    private static Map<UUID, MenuBuilder> openedInventories = new HashMap<>();

    private final List<ItemBuilder> items = new ArrayList<>();

    public final Player player;

    private static String title;
    private final int rows;

    private boolean autoUpdate;
    private boolean updateOnClick;
    private boolean doubleClick = true;

    public abstract List<ItemBuilder> getItems();

    public abstract String getTitle();

    public Inventory get() {
        Inventory inventory = Bukkit.createInventory(null, getSlots(), getTitle() == null ? "" : CC.color(getTitle()));

        updateContent(inventory);

        return inventory;
    }

    private void updateContent(Inventory inventory) {
        items.clear();
        items.addAll(getItems());
        items.removeIf(Objects::isNull);

        items.forEach(itemBuilder -> {
            int slot = itemBuilder.getSlot();

            if (slot >= 0 && slot <= getSlots()) {
                ItemStack item = itemBuilder.getItemData().applyFunctions(player, itemBuilder).defaultFlags().get();
                inventory.setItem(slot, item);
            }
        });
    }

    public int getSlots() {
        return 9 * rows;
    }

    public Optional<ItemBuilder> getItem(int slot) {
        return items.stream().filter(menuItem -> menuItem.getSlot() == slot).findAny();
    }

    public void open() {
        player.openInventory(get());

        openedInventories.put(player.getUniqueId(), this);
    }

    public void update() {
        Inventory inventory = player.getOpenInventory().getTopInventory();
        updateContent(inventory);
    }

    public void close() {
        player.closeInventory();
    }

    public void onClose() {
    }
}
