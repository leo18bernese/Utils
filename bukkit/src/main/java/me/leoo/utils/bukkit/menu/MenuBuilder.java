package me.leoo.utils.bukkit.menu;

import lombok.Data;
import lombok.Getter;
import me.leoo.utils.bukkit.chat.CC;
import me.leoo.utils.bukkit.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Predicate;

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
            int slot = itemBuilder.slot();

            if (slot < 0) return;

            if (slot >= getSlots()) {
                Bukkit.getLogger().warning("Cannot add item " + itemBuilder.itemMeta().getDisplayName() + " to slot " + slot + " in " + getTitle() + " menu. Slot is out of bounds.");
                return;
            }

            ItemStack item = itemBuilder.itemData().applyFunctions(player, itemBuilder).defaultFlags().get();
            inventory.setItem(slot, item);
        });
    }

    public int getSlots() {
        return 9 * rows;
    }

    public Optional<ItemBuilder> getItem(int slot) {
        return items.stream().filter(menuItem -> menuItem.slot() == slot).findAny();
    }

    public void open() {
        Predicate<Player> require = getOpenCondition();
        if (require != null && !require.test(player)) return;

        player.openInventory(get());

        openedInventories.put(player.getUniqueId(), this);
    }

    public void update() {
        InventoryView view = player.getOpenInventory();

        updateContent(view.getTopInventory());
    }

    public void updateAll() {
        openedInventories.values().stream().filter(m -> m.equals(this)).forEach(MenuBuilder::update);
    }

    public void close() {
        player.closeInventory();
    }

    public void onClose() {
    }

    public Predicate<Player> getOpenCondition() {
        return null;
    }
}
