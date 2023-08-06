package me.leoo.utils.bukkit.items;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.Getter;
import lombok.Setter;
import me.leoo.utils.bukkit.menu.MenuBuilder;
import me.leoo.utils.bukkit.menu.MenuItem;
import me.leoo.utils.common.file.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

@Getter
public class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;
    private final Map<String, String> replacements = new HashMap<>();
    @Setter
    private int slot = -1;

    public ItemBuilder(Material material) {
        if (material.equals(Material.AIR)) material = Material.STONE;
        XMaterial xMaterial = XMaterial.matchXMaterial(material);

        itemStack = xMaterial.parseItem();
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(String material, int data) {
        Optional<XMaterial> optionalMaterial = XMaterial.matchXMaterial(material + ":" + data);
        if (!optionalMaterial.isPresent()) {
            throw new IllegalArgumentException("Material not found: " + material);
        }

        XMaterial xMaterial = optionalMaterial.get();

        itemStack = xMaterial.parseItem();
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setDisplayName(String name) {
        itemMeta.setDisplayName(name);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        itemMeta.setLore(lore);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemBuilder setData(int data) {
        getItemStack().setDurability((short) data);
        return this;
    }

    public ItemBuilder applySkin(String string) {
        SkullUtils.applySkin(itemMeta, string);
        return this;
    }

    public ItemBuilder applySkinCondition(String string, boolean condition) {
        if (condition) {
            return applySkin(string);
        }
        return this;
    }

    public ItemBuilder setOwner(String string, boolean overrideItem) {
        if (overrideItem) {
            itemStack.setType(XMaterial.PLAYER_HEAD.parseMaterial());
        }
        if (itemStack.getType().equals(XMaterial.PLAYER_HEAD.parseMaterial())) {
            SkullUtils.applySkin(itemMeta, string);
        }

        return this;
    }

    public ItemBuilder setOwner(String owner) {
        setOwner(owner, false);
        return this;
    }

    public ItemBuilder addEnchant(String enchantment, int level) {
        Enchantment xEnchantment = XEnchantment.matchXEnchantment(enchantment).orElse(XEnchantment.DURABILITY).getEnchant();

        itemMeta.addEnchant(xEnchantment, level, true);

        return this;
    }

    public ItemBuilder setEnchanted() {
        addEnchant("DURABILITY", 1);
        return this;
    }

    public ItemBuilder setEnchanted(boolean enchanted) {
        if (enchanted) {
            return setEnchanted();
        }
        return this;
    }

    public ItemBuilder addFlags(ItemFlag... itemFlags) {
        itemMeta.addItemFlags(itemFlags);
        return this;
    }

    public ItemBuilder setDefaultFlags() {
        addFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder addReplacement(String key, String value) {
        replacements.put(key, value);
        return this;
    }

    public ItemBuilder setTag(String value) {
        NBT.modify(itemStack, nbt -> {
            nbt.setString("Guilds", value);
        });

        return this;
    }

    public static String getTag(ItemStack itemStack) {
        return new NBTItem(itemStack).getString("Guilds");
    }

    public static ItemBuilder getFromConfig(String path, ConfigManager config, int data) {
        ItemBuilder builder = new ItemBuilder(config.getString(path + ".material"), data);

        builder.setDisplayName(config.getString(path + ".name"));
        builder.setLore(config.getList(path + ".lore"));
        builder.setData(data);
        if (config.getBoolean(path + ".enchanted")) {
            builder.setEnchanted();
        }

        builder.setSlot(config.getInt(path + ".slot"));

        return builder;
    }

    public static ItemBuilder getFromConfig(String path, ConfigManager config) {
        return getFromConfig(path, config, config.getInt(path + ".data"));
    }

    public ItemStack get() {
        setDefaultFlags();

        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            String displayName = itemMeta.getDisplayName();
            setDisplayName(displayName.replace(entry.getKey(), entry.getValue()));

            if (itemMeta.getLore() != null) {
                List<String> lore = itemMeta.getLore();
                lore.replaceAll(line -> line.replace(entry.getKey(), entry.getValue()));
                setLore(lore);
            }
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void setInInventory(MenuBuilder menu, int slot, MenuItem.Callback<InventoryClickEvent> eventCallBack) {
        menu.getItems().add(new MenuItem(slot, get(), eventCallBack));
    }

    public void setInInventory(Player player, MenuBuilder menu, MenuItem.Callback<InventoryClickEvent> eventCallBack) {
        if (getSlot() == -1) {
            Bukkit.getLogger().severe(
                    "Slot not set for item: " + itemMeta.getDisplayName() + " in menu: " + menu.getTitle(player) + "." +
                            "\nUsing first slot.");

            setInInventory(menu, 0, eventCallBack);
        } else {
            setInInventory(menu, getSlot(), eventCallBack);
        }
    }
}
