package me.leoo.utils.bukkit.location;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@UtilityClass
public class LocationUtil {

    public boolean compareLocations(Location location1, Location location2, boolean block) {
        if (block) {
            return location1.getBlockX() == location2.getBlockX() &&
                    location1.getBlockY() == location2.getBlockY() &&
                    location1.getBlockZ() == location2.getBlockZ();
        } else {
            return location1.getX() == location2.getX() &&
                    location1.getY() == location2.getY() &&
                    location1.getZ() == location2.getZ();
        }
    }

    public Location deserializeLocation(String string) {
        if (string == null || string.isEmpty()) return null;
        if (!string.contains(":")) return null;

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

    public String serializeLocation(Location location) {
        if (location == null) return null;
        return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }

    public String getDefaultWorldName() {
        Properties props = new Properties();

        try {
            props.load(Files.newInputStream(Paths.get("server.properties")));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return props.getProperty("level-name");
    }
}
