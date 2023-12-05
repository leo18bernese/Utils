package me.leoo.utils.bukkit.items;

import lombok.Data;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Data
public class InteractItem {

    private final ItemBuilder item;
    private final Player player;

    @Getter
    private static List<InteractItem> items = new ArrayList<>();

    public InteractItem(ItemBuilder item, Player player) {
        InteractListeners.register();

        this.item = item;
        this.player = player;

        if (items.stream().noneMatch(interact -> interact == this)) {
            items.add(this);
        }
    }

    public void give() {
        if (item.getSlot() < 0) return;

        player.getInventory().setItem(item.getSlot(), item.get());
    }

    public static InteractItem getByItem(ItemStack itemStack) {
        return InteractItem.getItems().stream()
                .filter(item -> item.getItem().get().getType() == itemStack.getType())
                .filter(item -> item.getItem().get().getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName()))
                .filter(item -> item.getItem().isInteractRequire())
                .findAny().orElse(null);
    }
}
