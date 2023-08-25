package me.leoo.utils.bukkit.items;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.Getter;
import lombok.Setter;
import me.leoo.utils.bukkit.Utils;
import me.leoo.utils.bukkit.config.ConfigManager;
import me.leoo.utils.bukkit.menu.MenuBuilder;
import me.leoo.utils.bukkit.task.Tasks;
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

    private Callback<InventoryClickEvent> eventCallBack;

    @Setter
    private ConfigManager config;
    @Setter
    private ConfigManager language;
    @Setter
    private String configPath;

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

    public ItemBuilder setName(String name) {
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
            Tasks.runAsync(() -> SkullUtils.applySkin(itemMeta, string));
        }

        return this;
    }

    public ItemBuilder setOwner(String owner) {
        setOwner(owner, false);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder addEnchant(String enchantment, int level) {
        Enchantment xEnchantment = XEnchantment.matchXEnchantment(enchantment).orElse(XEnchantment.DURABILITY).getEnchant();

        itemMeta.addEnchant(xEnchantment, level, true);

        return this;
    }

    public ItemBuilder setEnchanted() {
        addEnchant(XEnchantment.DURABILITY.name(), 1);
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
            nbt.setString(Utils.getInitializedFrom().getDescription().getName(), value);
        });

        return this;
    }

    public static String getTag(ItemStack itemStack) {
        return new NBTItem(itemStack).getString(Utils.getInitializedFrom().getDescription().getName());
    }

    public ItemBuilder setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public ItemBuilder setEventCallBack(Callback<InventoryClickEvent> eventCallBack) {
        this.eventCallBack = eventCallBack;
        return this;
    }

    public void saveIntoConfig(String path, ConfigManager config, ConfigManager language) {
        config.set(path + ".material", itemStack.getType().name() + (itemStack.getDurability() == 0 ? "" : ":" + itemStack.getDurability()));
        config.set(path + ".amount", itemStack.getAmount());
        config.set(path + ".enchanted", itemMeta.hasEnchant(XEnchantment.DURABILITY.getEnchant()));

        language.set(path + ".name", itemMeta.getDisplayName());
        language.set(path + ".lore", itemMeta.getLore());
    }

    public void saveIntoConfig(String path, ConfigManager config) {
        saveIntoConfig(path, config, config);
    }

    public static ItemBuilder parseFromConfig(String path, ConfigManager config, ConfigManager language) {
        String[] material = config.getString(path + ".material").split(":");
        int materialData = material.length == 2 ? Integer.parseInt(material[1]) : 0;

        ItemBuilder builder;
        if (material[0].equals("texture")) {
            builder = new ItemBuilder(Material.SKULL_ITEM.name(), 3);
            builder.setData(3);
            builder.applySkin(material[1]);
        } else {
            builder = new ItemBuilder(material[0], materialData);
        }

        builder.setConfigPath(path);
        builder.setConfig(config);
        builder.setLanguage(language);

        if (language.getYml().get(path + ".name") != null) {
            builder.setName(language.getString(path + ".name"));
        }
        if (language.getYml().get(path + ".lore") != null) {
            builder.setLore(language.getList(path + ".lore"));
        }

        builder.setData(materialData);

        if (config.getBoolean(path + ".enchanted")) {
            builder.setEnchanted();
        }

        if (config.getYml().get(path + ".amount") != null) {
            builder.setAmount(config.getInt(path + ".amount"));
        }
        if (config.getYml().get(path + ".slot") != null) {
            builder.setSlot(config.getInt(path + ".slot"));
        }

        return builder;
    }

    public static ItemBuilder parseFromConfig(String path, ConfigManager config) {
        return parseFromConfig(path, config, config);
    }

    public static ItemBuilder getFromConfigOld(String path, ConfigManager config, int data) {
        ItemBuilder builder = new ItemBuilder(config.getString(path + ".material"), data);

        builder.setConfigPath(path);
        builder.setConfig(config);

        builder.setName(config.getString(path + ".name"));
        builder.setLore(config.getList(path + ".lore"));
        builder.setData(data);
        if (config.getBoolean(path + ".enchanted")) {
            builder.setEnchanted();
        }

        builder.setSlot(config.getInt(path + ".slot"));

        return builder;
    }

    public static ItemBuilder getFromConfigOld(String path, ConfigManager config) {
        return getFromConfigOld(path, config, config.getInt(path + ".data"));
    }

    public ItemStack get() {
        setDefaultFlags();

        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            String displayName = itemMeta.getDisplayName();
            setName(displayName.replace(entry.getKey(), entry.getValue()));

            if (itemMeta.getLore() != null) {
                List<String> lore = itemMeta.getLore();
                lore.replaceAll(line -> line.replace(entry.getKey(), entry.getValue()));
                setLore(lore);
            }
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void setInInventory(MenuBuilder menu) {
        menu.getItems().add(this);
    }

    public void setInInventory(Player player, MenuBuilder menu, int slot) {
        setSlot(slot);

        if (getSlot() == -1) {
            Bukkit.getLogger().severe(
                    "Slot not set for item: " + itemMeta.getDisplayName() + " in menu: " + menu.getTitle(player) + "." +
                            "\nUsing first slot.");

            setInInventory(menu);
        } else {
            setInInventory(menu);
        }
    }


    public interface Callback<T> {
        boolean accept(T t);
    }
}
