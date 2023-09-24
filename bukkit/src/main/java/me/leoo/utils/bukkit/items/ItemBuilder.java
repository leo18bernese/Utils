package me.leoo.utils.bukkit.items;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBT;
import lombok.Getter;
import lombok.Setter;
import me.leoo.utils.bukkit.Utils;
import me.leoo.utils.bukkit.config.ConfigManager;
import me.leoo.utils.bukkit.menu.MenuBuilder;
import me.leoo.utils.bukkit.task.Tasks;
import me.leoo.utils.common.number.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ItemBuilder implements Cloneable {

    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private final Map<String, String> replacements = new HashMap<>();

    private String toSaveString;
    private Callback<InventoryClickEvent> eventCallBack;
    private ConfigManager config;
    private ConfigManager language;
    private String configPath;

    private int slot = -1;

    public ItemBuilder(XMaterial xMaterial, int data) {
        this(xMaterial.name(), data);
    }

    public ItemBuilder(Material material) {
        this(material, 0);
    }

    public ItemBuilder(Material material, int data) {
        this(material.name(), data);
    }


    public ItemBuilder(String material, int data) {
        this(XMaterial.matchXMaterial(material + ":" + data).orElse(XMaterial.STONE));
    }

    public ItemBuilder(XMaterial xMaterial) {
        this(xMaterial.parseItem());
    }

    public ItemBuilder(ItemStack stack) {
        this(stack, stack.getItemMeta());
    }

    public ItemBuilder(ItemStack stack, ItemMeta meta) {
        itemStack = stack;
        itemMeta = meta;
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

    public ItemBuilder addEnchants(String... enchantments) {
        for (String enchantment : enchantments) {
            addEnchant(enchantment, 1);
        }

        return this;
    }

    public ItemBuilder addEnchants(List<String> enchantments) {
        for (String enchantment : enchantments) {
            addEnchant(enchantment, 1);
        }

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

    public ItemBuilder addEffects(PotionEffect effect) {
        ((PotionMeta) itemMeta).addCustomEffect(effect, true);
        return this;
    }

    public ItemBuilder colorArmor(Color color) {
        ((LeatherArmorMeta) itemMeta).setColor(color);
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

    public static void setTag(ItemStack itemStack, String value) {
        NBT.modify(itemStack, nbt -> {
            nbt.setString(Utils.getInitializedFrom().getDescription().getName(), value);
        });
    }

    public static String getTag(ItemStack itemStack) {
        return NBT.get(itemStack, nbt -> nbt.getString(Utils.getInitializedFrom().getDescription().getName()));
    }

    public ItemBuilder setToSaveString(String toSaveString) {
        this.toSaveString = toSaveString;
        return this;
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
        YamlConfiguration yml = config.getYml();
        YamlConfiguration languageYml = language.getYml();

        yml.addDefault(path + ".material", itemStack.getType().name() + (toSaveString == null ? (itemStack.getDurability() == 0 ? "" : ":" + itemStack.getDurability()) : ":" + toSaveString));
        yml.addDefault(path + ".amount", itemStack.getAmount());
        yml.addDefault(path + ".enchanted", itemMeta.hasEnchant(XEnchantment.DURABILITY.getEnchant()));

        if (slot >= 0) {
            yml.addDefault(path + ".slot", slot);
        }

        languageYml.addDefault(path + ".name", itemMeta.getDisplayName());
        languageYml.addDefault(path + ".lore", itemMeta.getLore());
    }

    public void saveIntoConfig(String path, ConfigManager config) {
        saveIntoConfig(path, config, config);
    }

    public static ItemBuilder parseFromConfig(String path, ConfigManager config, ConfigManager language) {
        String[] material = config.getString(path + ".material").split(":");

        String name = material[0];
        int data = material.length == 2 ? NumberUtil.toInt(material[1]) : 0;

        ItemBuilder builder;

        if (name.equals("texture")) {
            builder = new ItemBuilder(Material.SKULL_ITEM, 3);
            builder.setData(3);
            builder.applySkin(material[1]);
        } else if (name.equalsIgnoreCase("potion")) {
            builder = new ItemBuilder(new ItemStack(Material.POTION, 1, (short) data));
        } else {
            builder = new ItemBuilder(name, data);
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

        builder.setData(data);

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

    @Deprecated
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

    @Deprecated
    public static ItemBuilder getFromConfigOld(String path, ConfigManager config) {
        return getFromConfigOld(path, config, config.getInt(path + ".data"));
    }

    public ItemStack get() {
        setDefaultFlags();

        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            setName(itemMeta.getDisplayName().replace(entry.getKey(), entry.getValue()));

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

    @Override
    public ItemBuilder clone() {
        try {
            return (ItemBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }


    public interface Callback<T> {
        boolean accept(T t);
    }
}
