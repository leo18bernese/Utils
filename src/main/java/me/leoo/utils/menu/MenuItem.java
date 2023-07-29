package me.leoo.utils.menu;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.leoo.utils.file.ConfigManager;
import me.leoo.utils.items.ItemBuilder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@Data
@RequiredArgsConstructor
public class MenuItem {
    private final int slot;
    private final ItemStack itemStack;
    private final Callback<InventoryClickEvent> eventCallBack;

    public interface Callback<T> {
        boolean accept(T t);
    }

    public static MenuItem getByConfig(String path, ConfigManager config, Callback<InventoryClickEvent> event) {
        return new MenuItem(config.getInt(path + ".slot"), ItemBuilder.getFromConfig(path, config).get(), event);
    }
}