package com.voxel0046.enderbutt;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class ItemProtectListener implements Listener {

    private final EnderButtPlugin plugin;

    public ItemProtectListener(EnderButtPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!plugin.getConfig().getBoolean("item.lock-in-inventory", true)) return;
        if (ItemFactory.isEnderButtItem(event.getItemDrop().getItemStack(), plugin.getItemKey())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!plugin.getConfig().getBoolean("item.lock-in-inventory", true)) return;

        ItemStack current = event.getCurrentItem();
        ItemStack cursor = event.getCursor();

        if (ItemFactory.isEnderButtItem(current, plugin.getItemKey())
                || ItemFactory.isEnderButtItem(cursor, plugin.getItemKey())) {
            event.setCancelled(true);
            return;
        }

        if (event.getClick() == ClickType.SWAP_OFFHAND
                && ItemFactory.isEnderButtItem(event.getWhoClicked().getInventory().getItemInOffHand(), plugin.getItemKey())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!plugin.getConfig().getBoolean("item.lock-in-inventory", true)) return;
        if (ItemFactory.isEnderButtItem(event.getOldCursor(), plugin.getItemKey())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMoveItem(InventoryMoveItemEvent event) {
        if (!plugin.getConfig().getBoolean("item.lock-in-inventory", true)) return;
        if (ItemFactory.isEnderButtItem(event.getItem(), plugin.getItemKey())) {
            event.setCancelled(true);
        }
    }
}
