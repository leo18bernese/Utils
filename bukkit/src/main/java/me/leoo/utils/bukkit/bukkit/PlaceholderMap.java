package me.leoo.utils.bukkit.bukkit;

import me.leoo.utils.common.string.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class PlaceholderMap implements Cloneable {

    private final Map<String, Supplier<String>> replacements = new HashMap<>();

    private final Map<String, Supplier<String>> placeholders = new HashMap<>();
    private final Map<String, Supplier<List<String>>> multiLinePlaceholders = new HashMap<>();

    // Add
    public PlaceholderMap add(String key, Supplier<String> value) {
        if (!key.startsWith("{") && !key.endsWith("}")) {
            key = "{" + key + "}";
        }

        placeholders.put(key, value);
        return this;
    }

    public PlaceholderMap add(String key, String value) {
        return add(key, () -> value);
    }

    public PlaceholderMap add(String key, Number value) {
        return add(key, String.valueOf(value));
    }

    public PlaceholderMap addMultiple(String key, Supplier<List<String>> value) {
        if (!key.startsWith("{") && !key.endsWith("}")) {
            key = "{" + key + "}";
        }

        multiLinePlaceholders.put(key, value);
        return this;
    }

    public PlaceholderMap addMultiple(String key, List<String> value) {
        return addMultiple(key, () -> value);
    }

    public PlaceholderMap addMap(PlaceholderMap map) {
        placeholders.putAll(map.placeholders);
        multiLinePlaceholders.putAll(map.multiLinePlaceholders);

        return this;
    }

    // Replacements
    public PlaceholderMap addReplacement(String key, Supplier<String> value) {
        if (!key.startsWith("{") && !key.endsWith("}")) {
            key = "{" + key + "}";
        }

        replacements.put(key, value);
        return this;
    }

    // Parse
    public String parse(String text) {
        for (Map.Entry<String, Supplier<String>> entry : replacements.entrySet()) {
            text = replace(text, entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, Supplier<String>> entry : placeholders.entrySet()) {
            text = text.replace(entry.getKey(), entry.getValue().get());
        }

        return text;
    }

    public List<String> parse(List<String> text) {
        replacements.forEach((key, value) -> text.replaceAll(line -> replace(line, key, value)));
        placeholders.forEach((key, value) -> text.replaceAll(line -> replace(line, key, value)));

        for (Map.Entry<String, Supplier<List<String>>> entry : multiLinePlaceholders.entrySet()) {
            StringUtil.replaceWithList(text, entry.getKey(), entry.getValue().get());
        }

        return text;
    }

    @Override
    public PlaceholderMap clone() {
        PlaceholderMap map = new PlaceholderMap();

        map.placeholders.putAll(this.placeholders);
        map.multiLinePlaceholders.putAll(this.multiLinePlaceholders);

        return map;
    }

    // Replace method
    private String replace(String text, String key, Supplier<String> value) {
        if (value.get() != null) {
            return text.replace(key, value.get());
        }

        return text;
    }
}
