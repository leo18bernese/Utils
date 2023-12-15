package me.leoo.utils.bukkit.items;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBT;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.leoo.utils.bukkit.Utils;
import me.leoo.utils.bukkit.config.ConfigManager;
import me.leoo.utils.bukkit.menu.MenuBuilder;
import me.leoo.utils.common.number.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class ItemBuilder implements Cloneable {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    private Map<String, String> replacements = new HashMap<>();
    private Function<String, String> replaceFunction;

    private Predicate<InventoryClickEvent> eventCallback;
    private Consumer<PlayerInteractEvent> interactCallback;
    private boolean interactRequire = true;

    private String permission;
    private String command;

    private String toSaveString;
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

    public ItemBuilder removeLore() {
        itemMeta.setLore(null);
        return this;
    }

    public ItemBuilder setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public ItemBuilder setType(Material material) {
        itemStack.setType(material);
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
            itemStack.setDurability((short) 3);
        }

        if (itemStack.getType().equals(XMaterial.PLAYER_HEAD.parseMaterial())) {
            SkullUtils.applySkin(itemMeta, string);

            /*Tasks.runLater(() -> SkullUtils.applySkin(itemMeta, string), 5L);*/
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

    public ItemBuilder colorFirework(Color color) {
        ((FireworkEffectMeta) itemMeta).setEffect(FireworkEffect.builder().withColor(color).build());
        return this;
    }

    public ItemBuilder addFlags(ItemFlag... itemFlags) {
        itemMeta.addItemFlags(itemFlags);
        return this;
    }

    public ItemBuilder setDefaultFlags() {
        addFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);
        return this;
    }

    public ItemBuilder addReplacement(String key, String value) {
        replacements.put(key, value);
        return this;
    }

    public ItemBuilder setReplaceFunction(Function<String, String> replaceFunction) {
        this.replaceFunction = replaceFunction;
        return this;
    }

    public ItemBuilder replaceLore(Function<List<String>, List<String>> replaceFunction) {
        setLore(replaceFunction.apply(itemMeta.getLore()));
        return this;
    }

    public ItemBuilder setTag(String value) {
        setTag(itemStack, value);
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

    public ItemBuilder setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public ItemBuilder setCommand(String command) {
        this.command = command;
        return this;
    }

    public ItemBuilder setEventCallback(Predicate<InventoryClickEvent> eventCallBack) {
        this.eventCallback = eventCallBack;
        return this;
    }

    public ItemBuilder setEventCallback(Consumer<InventoryClickEvent> eventConsumer) {
        this.eventCallback = event -> {
            eventConsumer.accept(event);
            return true;
        };

        return this;
    }


    public ItemBuilder setInteractCallback(Consumer<PlayerInteractEvent> interactCallback) {
        this.interactCallback = interactCallback;
        return this;
    }

    public ItemBuilder setInteractRequire(boolean interactRequire) {
        this.interactRequire = interactRequire;
        return this;
    }

    public void saveIntoConfig(String path, ConfigManager config, ConfigManager language) {
        YamlConfiguration yml = config.getYml();

        yml.addDefault(path + ".material", itemStack.getType().name() + (toSaveString == null ? (itemStack.getDurability() == 0 ? "" : ":" + itemStack.getDurability()) : ":" + toSaveString));
        yml.addDefault(path + ".amount", itemStack.getAmount());
        yml.addDefault(path + ".enchanted", itemMeta.hasEnchant(XEnchantment.DURABILITY.getEnchant()));

        if (slot >= 0) {
            yml.addDefault(path + ".slot", slot);
        }

        if (permission != null) {
            yml.addDefault(path + ".permission", permission);
        }

        if (command != null) {
            yml.addDefault(path + ".command", command);
        }

        if (language != null) {
            YamlConfiguration languageYml = language.getYml();

            languageYml.addDefault(path + ".name", itemMeta.getDisplayName());
            languageYml.addDefault(path + ".lore", itemMeta.getLore());
        }
    }

    public void saveIntoConfig(String path, ConfigManager config) {
        saveIntoConfig(path, config, config);
    }

    public static ItemBuilder parseFromConfig(String path, ConfigManager config, ConfigManager language) {
        String[] material = config.getString(path + ".material").split(":");

        String name = material[0];
        int data = material.length == 2 ? NumberUtil.toInt(material[1]) : 0;

        ItemBuilder builder;

        if (name.equals("texture") || (material.length == 2 && name.equals(XMaterial.PLAYER_HEAD.parseMaterial().name()))) {
            builder = new ItemBuilder(Material.SKULL_ITEM, 3);
            builder.setData(3);
            builder.setOwner(material[1]);
            System.out.println("settings skull item " + material[1]);
        } else if (name.equalsIgnoreCase("potion")) {
            builder = new ItemBuilder(new ItemStack(Material.POTION, 1, (short) data));
        } else {
            builder = new ItemBuilder(name, data);
        }

        if (builder.getItemStack().getType() == XMaterial.FIREWORK_STAR.parseMaterial()) {
            if (material.length == 2) builder.colorFirework(Color.fromRGB(data));
        }

        builder.setConfigPath(path);
        builder.setConfig(config);

        if (language != null) {
            builder.setLanguage(language);

            if (language.getYml().get(path + ".name") != null) {
                builder.setName(language.getString(path + ".name"));
            }
            if (language.getYml().get(path + ".lore") != null) {
                builder.setLore(language.getList(path + ".lore"));
            }
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
        if (config.getYml().get(path + ".permission") != null) {
            builder.setPermission(config.getString(path + ".permission"));
        }

        return builder;
    }

    public static ItemBuilder parseFromConfig(String path, ConfigManager config) {
        return parseFromConfig(path, config, config);
    }

    public ItemStack get() {
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            setName(itemMeta.getDisplayName().replace(entry.getKey(), entry.getValue()));

            if (itemMeta.getLore() != null) {
                List<String> lore = itemMeta.getLore();
                lore.replaceAll(line -> line.replace(entry.getKey(), entry.getValue()));
                setLore(lore);
            }
        }

        if (replaceFunction != null) {
            setName(replaceFunction.apply(itemMeta.getDisplayName()));

            if (itemMeta.getLore() != null)
                setLore(itemMeta.getLore().stream().map(string -> replaceFunction.apply(string)).collect(Collectors.toList()));
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
        } catch (CloneNotSupportedException exception) {
            exception.printStackTrace();
        }

        return null;
    }
}
