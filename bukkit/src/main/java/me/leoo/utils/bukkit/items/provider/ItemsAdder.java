package me.leoo.utils.bukkit.items.provider;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.inventory.ItemStack;

public class ItemsAdder extends ItemProvider {

    @Override
    public String[] getIds() {
        return new String[]{
                "itemsadder", "ia"
        };
    }

    @Override
    public String getPlugin() {
        return "ItemsAdder";
    }

    @Override
    public ItemStack getItem(String item) {
        CustomStack stack = CustomStack.getInstance(item);
        return stack == null ? null : stack.getItemStack();
    }

    @Override
    public String getItemKey(ItemStack item) {
        CustomStack stack = CustomStack.byItemStack(item);
        return stack == null ? null : stack.getId();
    }
}
