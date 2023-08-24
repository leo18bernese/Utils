package me.leoo.utils.bukkit.armorstand;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

@Data
public class ArmorStandBuilder {

    private final ArmorStand armorStand;
    private final Location location;

    public ArmorStandBuilder(Location location) {
        this.location = location;
        this.armorStand = location.getWorld().spawn(location, ArmorStand.class);
    }

    public ArmorStandBuilder setName(String name) {
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(name);

        return this;
    }

    public ArmorStandBuilder addDefault() {
        armorStand.setGravity(false);
        armorStand.setMarker(false);
        armorStand.setCanPickupItems(false);

        return this;
    }

    public ArmorStandBuilder makeInvisible() {
        armorStand.setVisible(false);
        armorStand.setCustomNameVisible(false);

        return this;
    }

    public ArmorStandBuilder makeNotArmorStand() {
        armorStand.setBasePlate(false);
        armorStand.setArms(false);

        return this;
    }

    public ArmorStandBuilder removeWhenFarAway(boolean value) {
        armorStand.setRemoveWhenFarAway(value);

        return this;
    }
}