package me.leoo.utils.bukkit.items.provider;

import me.leoo.utils.bukkit.items.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class ItemProvider {

    private static final Map<String, ItemProvider> PROVIDERS = new HashMap<>();

    static {
        register(new ItemsAdder());
    }

    public static void register(ItemProvider provider) {
        for (String id : provider.getIds()) {
            PROVIDERS.put(id, provider);
        }
    }

    public static ItemBuilder apply(String[] material) {
        if (material.length == 0) {
            return null;
        }

        ItemProvider provider = PROVIDERS.get(material[0].toLowerCase());

        if (provider == null) {
            return null;
        }

        ItemStack item = provider.getItem(material[1]);

        if (item == null) {
            return null;
        }

        return new ItemBuilder(item);
    }

    public static boolean hasProvider(String id) {
        return PROVIDERS.containsKey(id.toLowerCase());
    }


    public abstract String[] getIds();

    public abstract String getPlugin();

    public abstract ItemStack getItem(String id);

    public abstract String getItemKey(ItemStack item);
}
