package me.leoo.utils.bukkit.menu.pagination;

import lombok.Getter;
import me.leoo.utils.bukkit.menu.MenuBuilder;
import me.leoo.utils.bukkit.menu.MenuItem;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class PaginatedMenuBuilder extends MenuBuilder {

    private int page = 1;

    public abstract List<Integer> getPaginatedSlots();

    public abstract String getPaginationTitle(Player player);

    public abstract List<MenuItem> getAllPageItems(Player player);

    public abstract MenuItem getNextPageItem(Player player);

    public abstract MenuItem getPreviousPageItem(Player player);

    public List<MenuItem> getGlobalItems(Player player) {
        return null;
    }

    public PaginatedMenuBuilder(int rows) {
        super(rows);
    }

    @Override
    public List<MenuItem> getItems(Player player) {
        List<MenuItem> items = new ArrayList<>();

        List<MenuItem> i = new ArrayList<>(getAllPageItems(player));

        int index = (getPage() - 1) * getPaginatedSlots().size();
        for (Integer slot : getPaginatedSlots()) {
            if (index >= i.size()) {
                continue;
            }

            MenuItem menuItem = i.get(index);

            items.add(new MenuItem(slot, menuItem.getItemStack(), menuItem.getEventCallBack()));

            index++;
        }

        if (getGlobalItems(player) != null) {
            items.addAll(getGlobalItems(player));
        }

        if (page > 1 && getPreviousPageItem(player) != null) {
            items.add(/*((pageLine * 9) - 9*/
                    new MenuItem(getPreviousPageItem(player).getSlot(),
                            getPreviousPageItem(player).getItemStack(), event -> {
                        openNewPage(player, -1);
                        return true;
                    }));
        }

        if (page < getPages(player) && getNextPageItem(player) != null) {
            items.add(/*((pageLine * 9) - 1*/
                    new MenuItem(getNextPageItem(player).getSlot(),
                            getNextPageItem(player).getItemStack(), event -> {
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
