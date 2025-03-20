package com.jamesdpeters.minecraft.chests.menus;

import com.jamesdpeters.minecraft.chests.ChestsPlusPlus;
import com.jamesdpeters.minecraft.chests.misc.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class PlayerSelectorMenu implements InventoryProvider {

    private final SmartInventory menu;
    BiConsumer<OfflinePlayer, SmartInventory> onPlayerSelect;
    BiFunction<OfflinePlayer, ItemStack, ItemStack> onPlayerHeadCreation;
    private List<OfflinePlayer> players;
    private int lastPage; // Store the last page the player was on.
    private SmartInventory previousInv;

    private PlayerSelectorMenu(String title) {
        menu = SmartInventory.builder()
                .id("partyMenu")
                .title(title)
                .provider(this)
                .manager(ChestsPlusPlus.INVENTORY_MANAGER)
                .size(6, 9)
                .build();
        //menu.setInsertable(true);
    }

    public static void open(Player player, String title, SmartInventory previousInv, List<OfflinePlayer> players, BiFunction<OfflinePlayer, ItemStack, ItemStack> onPlayerHeadCreation, BiConsumer<OfflinePlayer, SmartInventory> onPlayerSelect) {
        PlayerSelectorMenu playerSelectorMenu = new PlayerSelectorMenu(title);
        playerSelectorMenu.players = players;
        playerSelectorMenu.onPlayerHeadCreation = onPlayerHeadCreation;
        playerSelectorMenu.onPlayerSelect = onPlayerSelect;
        playerSelectorMenu.previousInv = previousInv;
        playerSelectorMenu.menu.open(player);
    }



    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        List<ClickableItem> itemList = new ArrayList<>();
        for (OfflinePlayer offlinePlayer : players) {
            // Generate player heads
            ItemStack skull = ItemBuilder.getPlayerHead(offlinePlayer).setName(offlinePlayer.getName()).get();
            skull = onPlayerHeadCreation.apply(offlinePlayer, skull);

            // Pass through click to the menus onPlayerSelect function.
            ClickableItem clickableItem = ClickableItem.from(skull, itemClickData -> onPlayerSelect.accept(offlinePlayer, menu));
            itemList.add(clickableItem);
        }

        pagination.setItems(itemList.toArray(new ClickableItem[0]));
        pagination.setItemsPerPage(28);

        ItemStack border = ItemBuilder.getInstance(Material.GRAY_STAINED_GLASS_PANE).setName(" ").get();
        contents.fillBorders(ClickableItem.empty(border));
        for (ClickableItem item : pagination.getPageItems()) {
            contents.add(item);
        }

        ItemStack previous = ItemBuilder.getInstance(Material.ARROW).setName("Previous").get();
        contents.set(5, 2, ClickableItem.from(previous,
                e -> {
                    lastPage = pagination.previous().getPage();
                    menu.open(player, lastPage);
                }));

        ItemStack next = ItemBuilder.getInstance(Material.ARROW).setName("Next").get();
        contents.set(5, 6, ClickableItem.from(next,
                e -> {
                    lastPage = pagination.next().getPage();
                    menu.open(player, lastPage);
                }));

        ItemStack ret = ItemBuilder.getInstance(Material.BARRIER).setName("Return").get();
        contents.set(5, 4, ClickableItem.from(ret, itemClickData -> {
            if (previousInv != null) {
                previousInv.open(player);
            }
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public SmartInventory getMenu() {
        return menu;
    }
}
