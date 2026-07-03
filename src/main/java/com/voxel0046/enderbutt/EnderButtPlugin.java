package com.voxel0046.enderbutt;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class EnderButtPlugin extends JavaPlugin {

    private NamespacedKey itemKey;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.itemKey = new NamespacedKey(this, "enderbutt_item");

        RideListener rideListener = new RideListener(this);
        JoinListener joinListener = new JoinListener(this);
        ItemProtectListener itemProtectListener = new ItemProtectListener(this);
        EnderButtCommand command = new EnderButtCommand(this);

        Bukkit.getPluginManager().registerEvents(rideListener, this);
        Bukkit.getPluginManager().registerEvents(joinListener, this);
        Bukkit.getPluginManager().registerEvents(itemProtectListener, this);

        if (getCommand("enderbutt") != null) {
            getCommand("enderbutt").setExecutor(command);
            getCommand("enderbutt").setTabCompleter(command);
        }

        getLogger().info("EnderButt enabled.");
    }

    public NamespacedKey getItemKey() {
        return itemKey;
    }

    @Override
    public void onDisable() {
        getLogger().info("EnderButt disabled.");
    }
}
