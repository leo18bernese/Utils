package me.leoo.utils.bukkit.items;

import lombok.Data;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Data
public class InteractItem {

    private final ItemBuilder item;

    @Getter
    private static Map<String, InteractItem> items = new HashMap<>();

    private InteractItem(ItemBuilder item, String id) {
        this.item = item.setTag("ii-" + id);

        items.put(id, this);
    }

    public static InteractItem create(ItemBuilder item, String id) {
        if (items.containsKey(id)) return items.get(id);

        return new InteractItem(item, id);
    }

    public void give(Player player) {
        give(player, item.slot());
    }

    public void give(Player player, int slot) {
        if (slot < 0) return;

        player.getInventory().setItem(slot, item.get());
    }

    public static InteractItem getByItem(ItemStack itemStack) {
        String tag = ItemBuilder.getTag(itemStack);

        if (tag == null || !tag.startsWith("ii-")) return null;

        return items.get(tag.replace("ii-", ""));
    }
}
