package com.voxel0046.enderbutt;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.List;

public class EnderButtCommand implements CommandExecutor, TabCompleter {

    private final EnderButtPlugin plugin;

    public EnderButtCommand(EnderButtPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("enderbutt.admin")) {
            sender.sendMessage(ColorUtil.color("&cNo permission."));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(ColorUtil.color("&#55ff99EnderButt config reloaded."));
            return true;
        }

        sender.sendMessage(ColorUtil.color("&eUsage: /enderbutt reload"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) return Collections.singletonList("reload");
        return Collections.emptyList();
    }
}
