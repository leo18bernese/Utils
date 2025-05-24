package me.leoo.utils.bukkit.potion;

import lombok.experimental.UtilityClass;
import me.leoo.utils.common.number.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class PotionUtils {

    /**
     * Format: type:duration:amplifier
     * Amplifier can be omitted, default is 0.
     */
    public PotionEffect parseEffect(String effect) {
        String[] parts = effect.split(":");
        if (parts.length < 2) return null;

        PotionEffectType type = PotionEffectType.getByName(parts[0]);
        if (type == null) {
            Bukkit.getLogger().warning("Invalid potion effect type: " + parts[0]);
            return null;
        }

        int duration = NumberUtil.toInt(parts[1]);
        int amplifier = parts.length > 2 ? NumberUtil.toInt(parts[2]) : 0;

        return new PotionEffect(type, duration, amplifier);
    }

    public List<PotionEffectType> getPlayerEffects(Player player) {
        return player.getActivePotionEffects().stream()
                .map(PotionEffect::getType)
                .collect(Collectors.toList());
    }

    public boolean hasEffect(Player player, PotionEffectType type) {
        return player.getActivePotionEffects().stream()
                .anyMatch(effect -> effect.getType() == type);
    }

    public boolean matchEffects(Player player, PotionEffect effect) {
        PotionEffect playerEffect = player.getPotionEffect(effect.getType());
        if (playerEffect == null) return false;

        return playerEffect.getAmplifier() >= effect.getAmplifier() &&
                playerEffect.getDuration() >= effect.getDuration();
    }
}
