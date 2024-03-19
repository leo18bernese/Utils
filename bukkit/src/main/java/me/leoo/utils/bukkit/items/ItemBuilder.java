package me.leoo.utils.bukkit.items;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT;
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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ItemBuilder implements Cloneable {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    private final Map<String, String> replacements = new HashMap<>();
    private final List<Function<String, String>> replaceFunctions = new ArrayList<>();

    private Predicate<InventoryClickEvent> eventCallback;
    private Consumer<PlayerInteractEvent> interactCallback;
    private boolean interactRequire = true;

    private String permission;
    private String command;

    private String toSaveString;

    @Setter
    private ConfigManager config, language;
    @Setter
    private String configPath;

    private int slot = -1;

    public ItemBuilder(XMaterial xMaterial, int data) {
        this(xMaterial.parseMaterial(), data);
    }

    public ItemBuilder(Material material) {
        this(material, 0);
    }

    public ItemBuilder(Material material, int data) {
        this(material == null ? "" : material.name(), data);
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

    public ItemBuilder name(String name) {
        itemMeta.setDisplayName(name);
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        itemMeta.setLore(lore);
        return this;
    }

    public ItemBuilder lore(String... lore) {
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

    public ItemBuilder type(Material material) {
        itemStack.setType(material);
        return this;
    }

    public ItemBuilder data(int data) {
        getItemStack().setDurability((short) data);
        return this;
    }

    public ItemBuilder skin(String string) {
        SkullUtils.applySkin(itemMeta, string);
        return this;
    }

    public ItemBuilder skinCondition(String string, boolean condition) {
        if (condition) {
            return skin(string);
        }
        return this;
    }

    public ItemBuilder owner(String string, boolean overrideItem) {
        if (overrideItem) {
            itemStack.setType(XMaterial.PLAYER_HEAD.parseMaterial());
            itemStack.setDurability((short) 3);

            itemMeta = itemStack.getItemMeta();
        }

        if (itemMeta instanceof SkullMeta) {
            SkullUtils.applySkin(itemMeta, string);

            /*Tasks.runLater(() -> SkullUtils.applySkin(itemMeta, string), 5L);*/
        }

        return this;
    }

    public ItemBuilder owner(String string) {
        owner(string, false);
        return this;
    }

    public ItemBuilder amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder enchant(String enchantment, int level) {
        Enchantment xEnchantment = XEnchantment.matchXEnchantment(enchantment).orElse(XEnchantment.DURABILITY).getEnchant();

        itemMeta.addEnchant(xEnchantment, level, true);

        return this;
    }

    public ItemBuilder enchants(String... enchantments) {
        for (String enchantment : enchantments) {
            enchant(enchantment, 1);
        }

        return this;
    }

    public ItemBuilder enchants(List<String> enchantments) {
        for (String enchantment : enchantments) {
            enchant(enchantment, 1);
        }

        return this;
    }

    public ItemBuilder setEnchanted() {
        enchant(XEnchantment.DURABILITY.name(), 1);
        return this;
    }

    public ItemBuilder setEnchanted(boolean enchanted) {
        if (enchanted) return setEnchanted();

        return this;
    }

    public ItemBuilder effect(PotionEffect effect) {
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

    public ItemBuilder flag(ItemFlag... itemFlags) {
        itemMeta.addItemFlags(itemFlags);
        return this;
    }

    public ItemBuilder defaultFlags() {
        flag(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);
        return this;
    }

    public ItemBuilder addReplacement(String key, String value) {
        replacements.put(key, value);
        return this;
    }

    public ItemBuilder addReplaceFunction(Function<String, String> replaceFunction) {
        this.replaceFunctions.add(replaceFunction);
        return this;
    }

    public ItemBuilder replaceName(Function<String, String> replaceFunction) {
        name(replaceFunction.apply(itemMeta.getDisplayName()));
        return this;
    }

    public ItemBuilder replaceLore(Function<List<String>, List<String>> replaceFunction) {
        lore(replaceFunction.apply(itemMeta.getLore()));
        return this;
    }

    public ItemBuilder setTag(String value) {
        setTag(itemStack, value);
        return this;
    }

    public static ItemStack setTag(ItemStack itemStack, String value) {
        NBT.modify(itemStack, nbt -> {
            nbt.setString(Utils.getInitializedFrom().getDescription().getName(), value);
        });

        return itemStack;
    }

    public static String getTag(ItemStack itemStack) {
        return NBT.get(itemStack, (Function<ReadableItemNBT, String>) nbt -> nbt.getString(Utils.getInitializedFrom().getDescription().getName()));
    }

    public ItemBuilder setToSaveString(String toSaveString) {
        this.toSaveString = toSaveString;
        return this;
    }

    public ItemBuilder slot(int slot) {
        this.slot = slot;
        return this;
    }

    public ItemBuilder permission(String permission) {
        this.permission = permission;
        return this;
    }

    public ItemBuilder command(String command) {
        this.command = command;
        return this;
    }


    public ItemBuilder event(Predicate<InventoryClickEvent> eventCallBack) {
        this.eventCallback = eventCallBack;
        return this;
    }

    public ItemBuilder event(Consumer<InventoryClickEvent> eventConsumer) {
        event(event -> {
            eventConsumer.accept(event);
            return true;
        });

        return this;
    }


    public ItemBuilder interact(Consumer<PlayerInteractEvent> interactCallback) {
        this.interactCallback = interactCallback;
        return this;
    }

    public ItemBuilder interactRequire(boolean interactRequire) {
        this.interactRequire = interactRequire;
        return this;
    }

    public void save(String path, ConfigManager config, ConfigManager language) {
        config.add(path + ".material", itemStack.getType().name() + (toSaveString == null ? (itemStack.getDurability() == 0 ? "" : ":" + itemStack.getDurability()) : ":" + toSaveString));
        config.add(path + ".amount", itemStack.getAmount());
        config.add(path + ".enchanted", itemMeta.hasEnchant(XEnchantment.DURABILITY.getEnchant()));

        if (slot >= 0) {
            config.add(path + ".slot", slot);
        }

        if (permission != null) {
            config.add(path + ".permission", permission);
        }

        if (command != null) {
            config.add(path + ".command", command);
        }

        if (language != null) {
            language.add(path + ".name", itemMeta.getDisplayName());
            language.add(path + ".lore", itemMeta.getLore());
        }
    }

    public void save(String path, ConfigManager config) {
        save(path, config, config);
    }

    public static ItemBuilder parse(String path, ConfigManager config, ConfigManager language) {
        String[] material = config.getString(path + ".material").split(":");

        String name = material[0];

        ItemBuilder builder;

        if (name.equals("texture") || (material.length == 2 && XMaterial.matchXMaterial(name + ":" + 3).orElse(XMaterial.STONE) == XMaterial.PLAYER_HEAD)) {
            builder = new ItemBuilder(XMaterial.PLAYER_HEAD, 3);
            builder.owner(material[1]);
        } else if (name.equalsIgnoreCase("potion")) {
            builder = new ItemBuilder(XMaterial.POTION);
            builder.data(getData(material));
        } else {
            builder = new ItemBuilder(name, getData(material));
        }

        if (builder.getItemStack().getType() == XMaterial.FIREWORK_STAR.parseMaterial()) {
            if (material.length == 2) {
                int[] dataSplit = Arrays.stream(material[1].split("-")).mapToInt(NumberUtil::toInt).toArray();

                if (dataSplit.length == 3) {
                    builder.colorFirework(Color.fromRGB(dataSplit[0], dataSplit[1], dataSplit[2]));
                } else {
                    builder.colorFirework(Color.fromRGB(dataSplit[0]));
                }
            }
        }

        builder.setConfigPath(path);
        builder.setConfig(config);

        if (language != null) {
            builder.setLanguage(language);

            if (language.getYml().get(path + ".name") != null) {
                builder.name(language.getString(path + ".name"));
            }
            if (language.getYml().get(path + ".lore") != null) {
                builder.lore(language.getList(path + ".lore"));
            }
        }

        if (config.getBoolean(path + ".enchanted")) {
            builder.setEnchanted();
        }

        if (config.getYml().get(path + ".amount") != null) {
            builder.amount(config.getInt(path + ".amount"));
        }
        if (config.getYml().get(path + ".slot") != null) {
            builder.slot(config.getInt(path + ".slot"));
        }
        if (config.getYml().get(path + ".permission") != null) {
            builder.permission(config.getString(path + ".permission"));
        }

        return builder;
    }

    public static ItemBuilder parse(String path, ConfigManager config) {
        return parse(path, config, config);
    }

    private static int getData(String[] material) {
        if(material.length == 2) {
            return NumberUtil.isInt(material[1]) ? NumberUtil.toInt(material[1]) : 0;
        }

        return 0;
    }

    public ItemStack get() {
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            name(itemMeta.getDisplayName().replace(entry.getKey(), entry.getValue()));

            if (itemMeta.getLore() != null) {
                List<String> lore = itemMeta.getLore();
                lore.replaceAll(line -> line.replace(entry.getKey(), entry.getValue()));
                lore(lore);
            }
        }

        replaceFunctions.forEach(replaceFunction -> {
            name(replaceFunction.apply(itemMeta.getDisplayName()));

            if (itemMeta.getLore() != null) {
                lore(itemMeta.getLore().stream().map(replaceFunction).collect(Collectors.toList()));
            }
        });

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void setInInventory(MenuBuilder menu) {
        menu.getItems().add(this);
    }

    public void setInInventory(MenuBuilder menu, int slot) {
        this.slot = slot;

        if (slot == -1) {
            Bukkit.getLogger().severe(
                    "Slot not set for item: " + itemMeta.getDisplayName() + " in menu: " + menu.getTitle() + "." +
                            "\nUsing first slot.");
        }

        setInInventory(menu);
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
