package me.leoo.utils.bukkit.bukkit;

import lombok.experimental.UtilityClass;
import me.leoo.utils.bukkit.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@UtilityClass
public class BukkitUtils {

    public static final int VERSION = Integer.parseInt(Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1]);

    public boolean supports(int version) {
        return VERSION >= version;
    }

    public List<ItemStack> getInventoryAndArmor(Player player) {
        List<ItemStack> items = new ArrayList<>(Arrays.asList(player.getInventory().getContents()));
        items.addAll(Arrays.asList(player.getInventory().getArmorContents()));

        return items;
    }

    public Firework sendFireworks(Location location, int power, Color... colors) {
        Firework firework = location.getWorld().spawn(location, Firework.class);

        FireworkMeta meta = firework.getFireworkMeta();
        meta.addEffect(FireworkEffect.builder().withColor(colors).build());
        meta.setPower(power);
        firework.setFireworkMeta(meta);

        return firework;
    }

    public String getRgbString(Color color) {
        return color.getRed() + "-" + color.getGreen() + "-" + color.getBlue();
    }


    public void repeat(Runnable task, long delay, long interval, int times) {
        new BukkitRunnable() {
            int remaining = times;

            @Override
            public void run() {
                task.run();

                if (--remaining == 0) {
                    cancel();
                }
            }
        }.runTaskTimer(Utils.get(), delay, interval);
    }

    public void repeat(Runnable task, long delay, long interval, int times, Runnable onEnd) {
        new BukkitRunnable() {
            int remaining = times;

            @Override
            public void run() {
                task.run();

                if (--remaining == 0) {
                    onEnd.run();

                    cancel();
                }
            }
        }.runTaskTimer(Utils.get(), delay, interval);
    }

    public void repeat(Runnable task, long delay, long interval, Supplier<Boolean> condition) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!condition.get()) {
                    cancel();
                    return;
                }
                task.run();
            }
        }.runTaskTimer(Utils.get(), delay, interval);
    }

    public void repeat(Runnable task, long delay, long interval, Supplier<Boolean> condition, Runnable onEnd) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!condition.get()) {
                    onEnd.run();

                    cancel();
                    return;
                }
                task.run();
            }
        }.runTaskTimer(Utils.get(), delay, interval);
    }

    public List<String> addColor(String color, List<String> list) {
        return list.stream().map(line -> color + line).collect(Collectors.toList());
    }

    public String[] addColor(String color, String... list) {
        return Arrays.stream(list).map(line -> color + line).toArray(String[]::new);
    }

    // Messages methods
    public void sendMessage(List<Player> players, String message) {
        players.forEach(player -> player.sendMessage(message));
    }

    public void broadcast(String message, String permission) {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission(permission))
                .forEach(player -> player.sendMessage(message));
    }
}
