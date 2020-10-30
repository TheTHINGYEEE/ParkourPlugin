package com.github.thethingyee.parkourplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class FindWorldbyUUID implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1) {
            World world = Bukkit.getWorld(UUID.fromString(args[0]));
            if(world != null) {
                sender.sendMessage("World by Bukkit: " + ChatColor.GREEN + world.toString());
                sender.sendMessage("World by name: " + ChatColor.GREEN + world.getName());
                sender.sendMessage("World by UUID: " + ChatColor.GREEN + world.getUID().toString());
            } else {
                sender.sendMessage(ChatColor.RED + "That world doesn't exist!");
            }
        }
        return true;
    }
}
