package com.github.thethingyee.parkourplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnloadWorld implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Bukkit.unloadWorld(args[0], false);
        sender.sendMessage(ChatColor.GREEN + "World '" + args[0] + "' has been successfully unloaded!");
        return true;
    }
}
