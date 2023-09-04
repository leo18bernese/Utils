package me.leoo.utils.bukkit.location;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

@Data
public class Region {

    private String name;
    private World world;
    private int minY;
    private int maxY;
    private int minX;
    private int maxX;
    private int minZ;
    private int maxZ;

    public Region(Location location, int radius) {
        new Region(location.clone().subtract(radius, radius, radius), location.clone().add(radius, radius, radius));
    }

    public Region(Location loc1, Location loc2) {
        world = loc1.getWorld();

        minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());

        minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());

        minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
    }

    public boolean isInRegion(Location location) {
        return (location.getBlockX() <= maxX && location.getBlockX() >= minX) &&
                (location.getY() <= maxY && location.getY() >= minY) &&
                (location.getBlockZ() <= maxZ && location.getBlockZ() >= minZ);
    }

    public List<Block> getBlocks() {
        List<Block> blocks = new ArrayList<>();

        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    blocks.add(world.getBlockAt(x, y, z));
                }
            }
        }

        return blocks;
    }

    public static boolean equalsLocation(Location loc, Location loc1, int radius) {
        for (double x = loc.getX() - radius; x < loc.getX() + radius; x++) {
            for (double y = loc.getY() - radius; y < loc.getY() + radius; y++) {
                for (double z = loc.getZ() - radius; z < loc.getZ() + radius; z++) {
                    if (loc1.getBlockX() == x && loc1.getBlockY() == y && loc1.getBlockZ() == z) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
