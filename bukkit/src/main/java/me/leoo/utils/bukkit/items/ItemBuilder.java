package me.leoo.utils.bukkit.items;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.Profileable;
import de.tr7zw.changeme.nbtapi.NBT;
import dev.lone.itemsadder.api.CustomStack;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.leoo.utils.bukkit.Utils;
import me.leoo.utils.bukkit.bukkit.PlaceholderMap;
import me.leoo.utils.bukkit.chat.CC;
import me.leoo.utils.bukkit.config.ConfigSection;
import me.leoo.utils.bukkit.items.provider.ItemProvider;
import me.leoo.utils.bukkit.sound.SoundUtil;
import me.leoo.utils.common.number.NumberUtil;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.material.Colorable;
import org.bukkit.potion.PotionEffect;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true, fluent = true)
public class ItemBuilder implements Cloneable {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    private final Map<String, String> replacements = new HashMap<>();
    private List<PlaceholderMap> placeholders = new ArrayList<>();

    private Predicate<InventoryClickEvent> eventCallback;
    private Consumer<PlayerInteractEvent> interactCallback;
    private Predicate<PlayerInteractEvent> interactRequirement;

    @Setter
    private String permission;

    @Setter
    private String command;

    private String toSaveString;

    private final List<String> sounds = new ArrayList<>();
    private final Map<String, String> tags = new HashMap<>();

    @Setter
    private ConfigSection config, language;

    @Setter
    private String configPath;

    @Setter
    private int slot = -1;

    private ItemData itemData = new ItemData();


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
        itemStack.setDurability((short) data);
        return this;
    }

    public ItemBuilder itemsadder(String namespacedId) {
        CustomStack customStack = CustomStack.getInstance(namespacedId);
        if (customStack != null) {
            this.itemStack = customStack.getItemStack();
            this.itemMeta = this.itemStack.getItemMeta();
        }
        return this;
    }

    public ItemBuilder skin(String string) {
        if (itemMeta instanceof SkullMeta) {
            XSkull.of(itemMeta).profile(Profileable.detect(string)).applyAsync()
                    .exceptionally(throwable -> {
                        throwable.printStackTrace();
                        return null;
                    });
        }

        return this;
    }

    public ItemBuilder skinSync(String string) {
        if (itemMeta instanceof SkullMeta) {
            XSkull.of(itemMeta).lenient().profile(Profileable.detect(string)).apply();
        }

        return this;
    }

    public ItemBuilder skinCondition(String string, boolean condition) {
        if (condition) {
            return skin(string);
        }

        return this;
    }

    public ItemBuilder amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder enchant(String enchantName, int level) {
        Enchantment enchant = XEnchantment.matchXEnchantment(enchantName).orElse(XEnchantment.UNBREAKING).getEnchant();
        if (enchant == null) return this;

        itemMeta.addEnchant(enchant, level, true);

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
        enchant(XEnchantment.UNBREAKING.name(), 1);
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

    public ItemBuilder colorItem(Color color) {
        ((Colorable) itemStack.getData()).setColor(DyeColor.getByColor(color));
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

    public ItemBuilder replacement(String key, String value) {
        replacements.put(key, value);
        return this;
    }

    public ItemBuilder replacement(String key, Number value) {
        replacements.put(key, String.valueOf(value));
        return this;
    }

    public ItemBuilder placeholderMap(PlaceholderMap map) {
        this.placeholders.add(map);
        return this;
    }

    public ItemBuilder replacePlaceholders(Player player) {
        if (itemMeta.hasDisplayName()) name(CC.color(itemMeta.getDisplayName(), player));
        if (itemMeta.hasLore()) lore(CC.color(itemMeta.getLore(), player));

        return this;
    }

    //todo maybe to remove
    public ItemBuilder replaceName(Function<String, String> replaceFunction) {
        name(replaceFunction.apply(itemMeta.getDisplayName()));
        return this;
    }

    public ItemBuilder replaceLore(Function<List<String>, List<String>> replaceFunction) {
        lore(replaceFunction.apply(itemMeta.getLore()));
        return this;
    }

    public ItemBuilder setTag(String value) {
        tags.put(Utils.getInitializedFrom().getDescription().getName(), value);
        return this;
    }

    public static ItemStack setTag(ItemStack itemStack, String value) {
        NBT.modify(itemStack, nbt -> {
            nbt.setString(Utils.getInitializedFrom().getDescription().getName(), value);
        });

        return itemStack;
    }

    public static String getTag(ItemStack itemStack) {
        return NBT.get(itemStack, nbt -> {
            return nbt.getString(Utils.getInitializedFrom().getDescription().getName());
        });
    }

    public ItemBuilder sound(String sound) {
        this.sounds.add(sound);
        return this;
    }

    public ItemBuilder event(Predicate<InventoryClickEvent> event) {
        this.eventCallback = event;
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

    public ItemBuilder applyToMeta(String path, ConfigSection config) {
        if (config.contains(path + ".name")) name(config.getString(path + ".name"));
        if (config.contains(path + ".lore")) lore(config.getList(path + ".lore"));

        return this;
    }

    public boolean executeItemAction(Player player) {
        if (config == null || configPath == null) return false;

        return config.executeAction(configPath, player);
    }

    public ItemBuilder addDataFunction(String function) {
        itemData.add(function.toUpperCase());
        return this;
    }

    public void save(String path, ConfigSection config, ConfigSection language) {
        config.add(path + ".material", itemStack.getType().name() + (toSaveString == null ? (itemStack.getDurability() == 0 ? "" : ":" + itemStack.getDurability()) : ":" + toSaveString));

        if (itemStack.getAmount() > 1) config.add(path + ".amount", itemStack.getAmount());
        if (itemMeta.hasEnchants()) config.add(path + ".enchanted", itemMeta.hasEnchants());

        if (slot >= 0) config.add(path + ".slot", slot);
        if (permission != null) config.add(path + ".permission", permission);
        if (command != null) config.add(path + ".command", command);

        if (language != null) {
            if (itemMeta.hasDisplayName()) language.add(path + ".name", itemMeta.getDisplayName());
            if (itemMeta.hasLore()) language.add(path + ".lore", itemMeta.getLore());
        }
    }

    public void save(String path, ConfigSection config) {
        save(path, config, Utils.getLanguage(config));
    }

    public static ItemBuilder parse(String path, ConfigSection config, ConfigSection language) {
        String[] material = config.getString(path + ".material").split(":");
        String name = material[0];

        ItemBuilder builder;

        if (name.equals("texture") || (material.length == 2 && XMaterial.matchXMaterial(name + ":" + 3).orElse(XMaterial.STONE) == XMaterial.PLAYER_HEAD)) {
            builder = new ItemBuilder(XMaterial.PLAYER_HEAD, 3);

            String value = material[1];
            if (value.length() > 1) {
                builder.skin(value);
            }

        } else if (name.equalsIgnoreCase("potion")) {
            builder = new ItemBuilder(XMaterial.POTION);
            builder.data(getData(material));
        } else if (ItemProvider.hasProvider(name)) {
            builder = ItemProvider.apply(material);
        } else {
            builder = new ItemBuilder(name, getData(material));
        }

        if (XMaterial.FIREWORK_STAR.isSimilar(builder.itemStack())) {
            if (material.length == 2) {
                int[] dataSplit = Arrays.stream(material[1].split("-")).mapToInt(NumberUtil::toInt).toArray();

                if (dataSplit.length == 3) {
                    builder.colorFirework(Color.fromRGB(dataSplit[0], dataSplit[1], dataSplit[2]));
                } else {
                    builder.colorFirework(Color.fromRGB(dataSplit[0]));
                }
            }
        }

        builder.configPath(path);
        builder.config(config);

        if (language != null) {
            builder.language(language);

            if (language.contains(path + ".name")) builder.name(language.getString(path + ".name"));
            if (language.contains(path + ".lore")) builder.lore(language.getList(path + ".lore"));
        }

        builder.setEnchanted(config.getBoolean(path + ".enchanted"));
        builder.permission(config.getYml().getString(path + ".permission"));

        builder.amount(config.getYml().getInt(path + ".amount", 1));
        builder.slot(config.getYml().getInt(path + ".slot", -1));

        if (config.contains(path + ".sound")) {
            for (String sound : config.getList(path + ".sound")) {
                builder.sound(sound);
            }
        }

        if (config.contains(path + ".functions")) {
            for (String function : config.getList(path + ".functions")) {
                builder.addDataFunction(function);
            }
        }

        return builder;
    }

    public static ItemBuilder parse(String path, ConfigSection config) {
        return parse(path, config, Utils.getLanguage(config));
    }

    private static int getData(String[] material) {
        if (material.length == 2) {
            return NumberUtil.isInt(material[1]) ? NumberUtil.toInt(material[1]) : 0;
        }

        return 0;
    }

    public ItemStack get() {
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            if (entry.getValue() == null) continue;

            if (itemMeta.hasDisplayName()) name(itemMeta.getDisplayName().replace(entry.getKey(), entry.getValue()));

            if (itemMeta.hasLore()) {
                List<String> lore = itemMeta.getLore();
                lore.replaceAll(line -> line.replace(entry.getKey(), entry.getValue()));
                lore(lore);
            }
        }

        placeholders.forEach(map -> {
            if (itemMeta.hasDisplayName()) name(map.parse(itemMeta.getDisplayName()));
            if (itemMeta.hasLore()) lore(map.parse(itemMeta.getLore()));
        });

        if (itemMeta.hasDisplayName()) name(CC.color(itemMeta.getDisplayName()));
        if (itemMeta.hasLore()) lore(CC.color(itemMeta.getLore()));

        itemStack.setItemMeta(itemMeta);

        // Apply NBT Tags
        applyTags();

        return itemStack;
    }

    public void applyTags() {
        for (Map.Entry<String, String> entry : tags.entrySet()) {
            NBT.modify(itemStack, nbt -> {
                nbt.setString(entry.getKey(), entry.getValue());
            });
        }
    }

    public void runSound(Player player) {
        sounds.forEach(sound -> SoundUtil.play(player, sound));
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

    protected void replaceName(String string) {
        if (itemMeta.hasDisplayName() && string != null) {
            name(string);
        }
    }
}
