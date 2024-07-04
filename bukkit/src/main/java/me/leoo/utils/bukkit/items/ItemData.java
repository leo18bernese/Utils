package me.leoo.utils.bukkit.items;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@Getter
public class ItemData {

    private final List<String> enabledFunctions = new ArrayList<>();

    private static final List<String> DEFAULT_FUNCTIONS = new ArrayList<>();
    private static final Map<String, BiFunction<Player, ItemBuilder, ItemBuilder>> FUNCTIONS = new HashMap<>();

    static {
        FUNCTIONS.put("SELF_SKIN", (player, item) -> item.skinSync(player.getUniqueId().toString()));
        FUNCTIONS.put("PLACEHOLDERS", (player, item) -> item.replacePlaceholders(player));

        DEFAULT_FUNCTIONS.add("PLACEHOLDERS");
    }

    public ItemData add(String function) {
        if (FUNCTIONS.containsKey(function)) {
            enabledFunctions.add(function);
        }

        return this;
    }

    public ItemBuilder applyFunctions(Player player, ItemBuilder item) {
        enabledFunctions.addAll(DEFAULT_FUNCTIONS);

        for (String function : enabledFunctions) {
            item = FUNCTIONS.get(function).apply(player, item);
        }

        return item;
    }

    public static void registerFunction(String name, BiFunction<Player, ItemBuilder, ItemBuilder> function) {
        FUNCTIONS.put(name, function);
    }
}
