package me.leoo.utils.bukkit.menu.pagination;

import lombok.Getter;
import me.leoo.utils.bukkit.items.ItemBuilder;
import me.leoo.utils.bukkit.menu.MenuBuilder;
import me.leoo.utils.common.number.NumberUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Getter
public abstract class DirectionMenuBuilder<T> extends MenuBuilder {

    private int page = 1;

    public abstract List<Integer> getPaginatedSlots();

    public abstract String getPaginationTitle();

    public abstract List<T> getGenericValues();

    public abstract List<ItemBuilder> getLineItems(T item);

    public abstract int getHighestTier(T item);

    public abstract ItemBuilder getNextPageItem();

    public abstract ItemBuilder getPreviousPageItem();

    public abstract boolean vertical();

    public List<ItemBuilder> getGlobalItems() {
        return new ArrayList<>();
    }

    private static final List<Integer>[] VERTICAL = new List[]{
            Arrays.asList(36, 27, 18, 9, 0),
            Arrays.asList(37, 28, 19, 10, 1),
            Arrays.asList(38, 29, 20, 11, 2),
            Arrays.asList(39, 30, 21, 12, 3),
            Arrays.asList(40, 31, 22, 13, 4),
            Arrays.asList(41, 32, 23, 14, 5),
            Arrays.asList(42, 33, 24, 15, 6),
            Arrays.asList(43, 34, 25, 16, 7),
            Arrays.asList(44, 35, 26, 17, 8)
    };

    private static final List<Integer>[] HORIZONTAL = new List[]{
            Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8),
            Arrays.asList(9, 10, 11, 12, 13, 14, 15, 16, 17),
            Arrays.asList(18, 19, 20, 21, 22, 23, 24, 25, 26),
            Arrays.asList(27, 28, 29, 30, 31, 32, 33, 34, 35),
            Arrays.asList(36, 37, 38, 39, 40, 41, 42, 43, 44)
    };

    public DirectionMenuBuilder(Player player, int rows) {
        super(player, rows);
    }

    @Override
    public List<ItemBuilder> getItems() {
        List<ItemBuilder> items = new ArrayList<>(getGlobalItems());
        List<T> builders = new ArrayList<>(getGenericValues());

        // Remove the items in the previous and next page slots
        if (getPreviousPageItem() != null) {
            items.removeIf(item -> item.getSlot() == getPreviousPageItem().getSlot());
        }

        if (getNextPageItem() != null) {
            items.removeIf(item -> item.getSlot() == getNextPageItem().getSlot());
        }


        // Added paginated items
        List<Integer>[] direction = vertical() ? VERTICAL : HORIZONTAL;

        int index = (page - 1) * direction.length;
        Iterator<T> iterator = builders.stream().skip(index).iterator();

        for (List<Integer> slots : direction) {
            if (!iterator.hasNext()) break;

            T item = iterator.next();

            List<ItemBuilder> line = getLineItems(item);

            int from;
            int to;

            if (slots.size() >= line.size()) {
                from = 0;
                to = line.size();
            } else {
                from = Math.min(getHighestTier(item) - 1, Math.abs(line.size() - slots.size()));
                to = Math.min(from + slots.size() + 1, line.size());
            }

            Iterator<ItemBuilder> lineIterator = line.subList(from, to).iterator();

            for (int slot : slots) {
                if (!lineIterator.hasNext()) {
                    break;
                }

                ItemBuilder itemBuilder = lineIterator.next();

                items.add(itemBuilder.slot(slot));
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
        int size = vertical() ? VERTICAL.length : HORIZONTAL.length;

        return NumberUtil.getTotalPages(getGenericValues().size(), size);
    }


}
