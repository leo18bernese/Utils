package me.leoo.utils.bukkit.menu.pagination;

import lombok.Getter;
import me.leoo.utils.bukkit.items.ItemBuilder;
import me.leoo.utils.bukkit.menu.MenuBuilder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class PaginatedMenuBuilder extends MenuBuilder {

    private int page = 1;

    public abstract List<Integer> getPaginatedSlots();

    public abstract String getPaginationTitle(Player player);

    public abstract List<ItemBuilder> getAllPageItems(Player player);

    public abstract ItemBuilder getNextPageItem(Player player);

    public abstract ItemBuilder getPreviousPageItem(Player player);

    public List<ItemBuilder> getGlobalItems(Player player) {
        return null;
    }

    public PaginatedMenuBuilder(int rows) {
        super(rows);
    }

    @Override
    public List<ItemBuilder> getItems(Player player) {
        List<ItemBuilder> items = new ArrayList<>();
        List<ItemBuilder> builders = new ArrayList<>(getAllPageItems(player));

        int index = (getPage() - 1) * getPaginatedSlots().size();
        for (Integer slot : getPaginatedSlots()) {
            if (index >= builders.size()) {
                continue;
            }

            items.add(builders.get(index).setSlot(slot));

            index++;
        }

        if (getGlobalItems(player) != null) {
            items.addAll(getGlobalItems(player));
        }

        if (page > 1 && getPreviousPageItem(player) != null) {
            items.add(getPreviousPageItem(player).setEvent(event -> {
                openNewPage(player, -1);
                return true;
            }));
        }

        if (page < getPages(player) && getNextPageItem(player) != null) {
            items.add(getNextPageItem(player).setEvent(event -> {
                openNewPage(player, +1);
                return true;
            }));
        }

        return items;
    }

    @Override
    public String getTitle(Player player) {
        return getPaginationTitle(player) + " #" + getPage();
    }

    public void openNewPage(Player player, int amount) {
        this.page += amount;
        getItems(player).clear();
        open(player);
    }

    public int getPages(Player player) {
        int itemsAmount = this.getAllPageItems(player).size();
        return itemsAmount == 0 ? 1 : (int) Math.ceil((double) itemsAmount / (double) this.getPaginatedSlots().size());
    }

}
