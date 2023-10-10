package me.leoo.utils.bukkit.items;

import lombok.Data;
import lombok.Getter;
import org.bukkit.entity.Player;

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
        player.getInventory().setItem(item.getSlot(), item.get());
    }
}
