package me.leoo.utils.bukkit.block;

import lombok.experimental.UtilityClass;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public class BlockUtil {

    private static final List<BlockFace> FACES = Arrays.asList(BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST);

    public static BlockFace getTargetedFace(Player player) {
        List<Block> targets = player.getLastTwoTargetBlocks(null, 100);
        if (targets.size() != 2 || !targets.get(1).getType().isOccluding()) return null;

        Block target = targets.get(1);
        Block adjacent = targets.get(0);

        return target.getFace(adjacent);
    }

    public BlockFace getYawFace(float yaw) {
        return getYawFace(yaw, false);
    }

    public BlockFace getYawFace(float yaw, boolean opposite) {
        BlockFace face = FACES.get(Math.round(yaw / 45f) & 0x7);

        return opposite ? face.getOppositeFace() : face;
    }

    public float faceToYaw(float yaw) {
        return FACES.indexOf(getYawFace(yaw)) * 45f;
    }
}
