package me.leoo.utils.bukkit.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

@Getter
@AllArgsConstructor
public class ConfigWrapper extends ConfigSection {

    private final ConfigurationSection section;
    private final String name;

    @Override
    public YamlConfiguration getYml() {
        if (section instanceof YamlConfiguration) {
            return (YamlConfiguration) section;
        }

        // Create a temporary YamlConfiguration with the same values
        YamlConfiguration yaml = new YamlConfiguration();
        for (String key : section.getKeys(true)) {
            yaml.set(key, section.get(key));
        }
        return yaml;
    }
}
