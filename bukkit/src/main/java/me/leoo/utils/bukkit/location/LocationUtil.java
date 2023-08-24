package me.leoo.utils.bukkit.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class LocationUtil {

    public static Location deserializeLocation(String string) {
        if (string == null || string.isEmpty()) return null;

        String[] split = string.split(":");
        return new Location(
                Bukkit.getWorld(split[0]),
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3]),
                Float.parseFloat(split[4]),
                Float.parseFloat(split[5])
        );
    }

    public static String serializeLocation(Location location) {
        if (location == null) return null;
        return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }

    public static String getDefaultWorldName() {
        Properties props = new Properties();

        try {
            props.load(Files.newInputStream(Paths.get("server.properties")));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return props.getProperty("level-name");
    }
}
