package me.leoo.utils.bukkit.menu.pagination;

import lombok.Getter;
import me.leoo.utils.bukkit.items.ItemBuilder;
import me.leoo.utils.bukkit.menu.MenuBuilder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class SortedMenuBuilder extends MenuBuilder {

    private int page = 1;

    public abstract Sort getSort();

    public abstract List<Integer> getSortedRows();

    public abstract List<Integer> getSortedColumns();

    public abstract String getPaginationTitle();

    public abstract Map<Integer, List<ItemBuilder>> getAllPageItems();

    public abstract ItemBuilder getNextPageItem();

    public abstract ItemBuilder getPreviousPageItem();

    public List<ItemBuilder> getGlobalItems() {
        return new ArrayList<>();
    }

    public SortedMenuBuilder(Player player, int rows) {
        super(player, rows);
    }

    @Override
    public List<ItemBuilder> getItems() {
        List<ItemBuilder> items = new ArrayList<>(getGlobalItems());
        Map<Integer, List<ItemBuilder>> builders = new HashMap<>(getAllPageItems());

        // Remove the items in the previous and next page slots
        if (getPreviousPageItem() != null) {
            items.removeIf(item -> item.getSlot() == getPreviousPageItem().getSlot());
        }

        if (getNextPageItem() != null) {
            items.removeIf(item -> item.getSlot() == getNextPageItem().getSlot());
        }

        // Added paginated items
        Sort sort = getSort();

        if (sort == Sort.HORIZONTAL_TO_RIGHT || sort == Sort.HORIZONTAL_TO_LEFT) {

            for (int row = 0; row < getSortedRows().size(); row++) {
                if (!builders.containsKey(row)) continue;

                List<ItemBuilder> rowItems = new ArrayList<>(builders.get(row));

                if (rowItems.size() > 9) {
                    rowItems = rowItems.subList(rowItems.size() - 9, rowItems.size());
                }

                for (int item = 0; item < rowItems.size(); item++) {
                    int slot = (getSortedRows().get(row) * 9) + item;

                    items.add(rowItems.get(item).slot(slot));
                }
            }
        }

        if (sort == Sort.VERTICAL_TO_BOTTOM || sort == Sort.VERTICAL_TO_TOP) {


            for (int column = 0; column < getSortedColumns().size(); column++) {

                int columnIndex = column * page;

                if (!builders.containsKey(columnIndex)) continue;

                List<ItemBuilder> columnItems = new ArrayList<>(builders.get(columnIndex));

                if (sort == Sort.VERTICAL_TO_BOTTOM) {
                    for (int row = getSortedRows().size(); row > 0; row--) {
                        int roww = getSortedRows().get(row);

                        if (columnItems.size() < row) continue;

                        items.add(columnItems.get(row).slot(roww));
                    }
                }
            }
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
        return 0;
       /* int itemsAmount = this.getAllPageItems().size();
        return itemsAmount == 0 ? 1 : (int) Math.ceil((double) itemsAmount / (double) this.getPaginatedSlots().size());
   */
    }

    public static enum Sort {
        HORIZONTAL_TO_LEFT,
        HORIZONTAL_TO_RIGHT,
        VERTICAL_TO_BOTTOM,
        VERTICAL_TO_TOP
    }
}
