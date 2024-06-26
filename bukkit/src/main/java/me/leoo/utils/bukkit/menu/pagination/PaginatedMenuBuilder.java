package me.leoo.utils.bukkit.menu.pagination;

import lombok.Getter;
import me.leoo.utils.bukkit.items.ItemBuilder;
import me.leoo.utils.bukkit.menu.MenuBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class PaginatedMenuBuilder extends MenuBuilder {

    private int page = 1;

    public abstract List<Integer> getPaginatedSlots();

    public abstract String getPaginationTitle();

    public abstract List<ItemBuilder> getAllPageItems();

    public abstract ItemBuilder getNextPageItem();

    public abstract ItemBuilder getPreviousPageItem();

    public List<ItemBuilder> getGlobalItems() {
        return null;
    }

    public PaginatedMenuBuilder(Player player, int rows) {
        super(player, rows);
    }

    @Override
    public List<ItemBuilder> getItems() {
        List<ItemBuilder> items = new ArrayList<>();
        List<ItemBuilder> builders = new ArrayList<>(getAllPageItems());

        int index = (getPage() - 1) * getPaginatedSlots().size();
        for (Integer slot : getPaginatedSlots()) {
            if (index >= builders.size()) continue;

            items.add(builders.get(index).slot(slot));

            index++;
        }

        if (getGlobalItems() != null) {
            items.addAll(getGlobalItems());
        }

        if (page > 1 && getPreviousPageItem() != null) {
            items.add(getPreviousPageItem().event(event -> {
                openNewPage(-1);
                return true;
            }));
        }

        if (page < getPages() && getNextPageItem() != null) {
            items.add(getNextPageItem().event(event -> {
                openNewPage(+1);
                return true;
            }));
        }

        return items;
    }

    @Override
    public String getTitle() {
        return getPaginationTitle() + " #" + getPage();
    }

    public void openNewPage(int amount) {
        openPage(page + amount);
    }

    public void openPage(int page) {
        this.page = page;
        getItems().clear();
        open();
    }

    public int getPages() {
        int itemsAmount = this.getAllPageItems().size();
        return itemsAmount == 0 ? 1 : (int) Math.ceil((double) itemsAmount / (double) this.getPaginatedSlots().size());
    }

}
