package me.leoo.utils.bukkit.menu.pagination;

import lombok.Getter;
import me.leoo.utils.bukkit.Utils;
import me.leoo.utils.bukkit.items.ItemBuilder;
import me.leoo.utils.bukkit.menu.MenuBuilder;
import me.leoo.utils.common.number.NumberUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class PaginatedMenuBuilder extends MenuBuilder {

    private int page = 1;
    private int itemsCount = 0;

    public abstract List<Integer> getPaginatedSlots();

    public abstract String getPaginationTitle();

    public abstract List<ItemBuilder> getAllPageItems();

    public abstract ItemBuilder getNextPageItem();

    public abstract ItemBuilder getPreviousPageItem();

    public List<ItemBuilder> getGlobalItems() {
        return new ArrayList<>();
    }

    public PaginatedMenuBuilder(Player player, int rows) {
        super(player, rows);
    }

    @Override
    public List<ItemBuilder> getItems() {
        List<ItemBuilder> items = new ArrayList<>(getGlobalItems());
        List<ItemBuilder> builders = new ArrayList<>(getAllPageItems());

        itemsCount = builders.size();

        // Remove the items in the previous and next page slots
        if (getPreviousPageItem() != null) {
            items.removeIf(item -> item.slot() == getPreviousPageItem().slot());
        }

        if (getNextPageItem() != null) {
            items.removeIf(item -> item.slot() == getNextPageItem().slot());
        }

        // Added paginated items
        int index = (getPage() - 1) * getPaginatedSlots().size();
        for (Integer slot : getPaginatedSlots()) {
            if (index >= builders.size()) continue;

            items.add(builders.get(index).slot(slot));

            index++;
        }

        // Add the previous and next page items
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
        if (!Utils.setNumberInventoryTitle) {
            return getPaginationTitle();
        }

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
        return NumberUtil.getTotalPages(itemsCount, getPaginatedSlots().size());
    }

}
