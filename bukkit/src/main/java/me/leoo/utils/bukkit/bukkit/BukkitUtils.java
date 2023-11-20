package me.leoo.utils.bukkit.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BukkitUtils {
    public static final String VERSION_STRING = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    public static final int VERSION = Integer.parseInt(VERSION_STRING.split("_")[1]);

    public static boolean supports(int version) {
        return VERSION >= version;
    }

    public static List<ItemStack> getInventoryAndArmor(Player player) {
        List<ItemStack> items = new ArrayList<>(Arrays.asList(player.getInventory().getContents()));
        items.addAll(Arrays.asList(player.getInventory().getArmorContents()));

        return items;
    }
}
