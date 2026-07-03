package com.voxel0046.enderbutt;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public final class ItemFactory {

    private ItemFactory() {}

    public static ItemStack createEnderButtItem(FileConfiguration cfg, NamespacedKey key) {
        ItemStack item = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        String name = cfg.getString("item.name", "&#b084ffEnderButt");
        List<String> loreRaw = cfg.getStringList("item.lore");

        meta.setDisplayName(ColorUtil.color(name));

        List<String> lore = new ArrayList<>();
        for (String line : loreRaw) lore.add(ColorUtil.color(line));
        meta.setLore(lore);

        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);

        if (cfg.getBoolean("item.hide-enchants", false)) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(meta);
        return item;
    }

    public static boolean isEnderButtItem(ItemStack item, NamespacedKey key) {
        if (item == null || item.getType() != Material.ENDER_PEARL) return false;
        if (!item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        Byte value = meta.getPersistentDataContainer().get(key, PersistentDataType.BYTE);
        return value != null && value == (byte) 1;
    }
}
