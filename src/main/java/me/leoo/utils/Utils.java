package me.leoo.utils;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Utils extends JavaPlugin {

    @Getter
    private static Utils instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    public static Utils get() {
        return instance;
    }
}