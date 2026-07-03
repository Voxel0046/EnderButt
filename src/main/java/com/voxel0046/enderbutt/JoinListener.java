package com.voxel0046.enderbutt;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final EnderButtPlugin plugin;

    public JoinListener(EnderButtPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        FileConfiguration cfg = plugin.getConfig();
        if (!cfg.getBoolean("join-item.enabled", true)) return;

        Player player = event.getPlayer();
        if (!player.hasPermission("enderbutt.joinitem")) return;

        int slot = cfg.getInt("join-item.slot", 1);
        if (slot < 1) slot = 1;
        if (slot > 9) slot = 9;

        int hotbarIndex = slot - 1;
        player.getInventory().setItem(hotbarIndex, ItemFactory.createEnderButtItem(cfg, plugin.getItemKey()));
    }
}
