package com.github.thethingyee.parkourplugin.commands;

import com.github.thethingyee.parkourplugin.ParkourPlugin;
import com.github.thethingyee.parkourplugin.SubCore.SubCore;
import com.github.thethingyee.parkourplugin.SubCore.managers.Arena;
import com.github.thethingyee.parkourplugin.SubCore.managers.ArenaManager;
import com.github.thethingyee.parkourplugin.SubCore.managers.PlayerManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ParkourCommands extends SubCore implements CommandExecutor {

    public ParkourCommands(Arena arena, ArenaManager arenaManager, PlayerManager playerManager, ParkourPlugin parkourPlugin) {
        super(arena, arenaManager, playerManager, parkourPlugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(args[0].equalsIgnoreCase("makeworld")) {
                if(args.length == 2) {
                    if(this.getParkourPlugin().getConfig().getConfigurationSection("parkourarenas.worlds." + args[1]) == null) {
                        player.sendMessage(this.getParkourPlugin().PREFIX + "(" + args[1] + ") " + ChatColor.GRAY + "Creating world...");
                        WorldCreator world = new WorldCreator(args[1]);

                        world.environment(World.Environment.NORMAL);
                        world.type(WorldType.FLAT);
                        world.generateStructures(false);

                        World fworld = world.createWorld();
                        fworld.setDifficulty(Difficulty.PEACEFUL);
                        player.sendMessage(this.getParkourPlugin().PREFIX + "(" + args[1] + ") " + ChatColor.GRAY + "Complete.");
                        this.getParkourPlugin().getConfig().set("parkourarenas.worlds." + fworld.getName() + ".owner", player.getUniqueId().toString());
                        this.getParkourPlugin().saveConfig();

                    } else {
                        player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "That world has been already added!");
                    }
                } else if(args.length == 1) {
                    player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "Missing arguments!");
                    return true;
                }
            }
            if(args[0].equalsIgnoreCase("addworld")) {
                if(args.length == 3) {
                    World world = Bukkit.getWorld(args[1]);
                    Player target = Bukkit.getPlayer(args[2]);
                    if(world != null) {
                        world.setDifficulty(Difficulty.PEACEFUL);
                        if(this.getParkourPlugin().getConfig().getConfigurationSection("parkourarenas.worlds." + world.getName()) == null) {
                            if(target != null) {
                                this.getParkourPlugin().getConfig().set("parkourarenas.worlds." + world.getName() + ".owner", target.getUniqueId().toString());
                                this.getParkourPlugin().saveConfig();
                                player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.GRAY + "Successfully added world '" + ChatColor.GOLD + world.getName() + ChatColor.GRAY + "'!");
                            } else {
                                player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "That player isn't online or doesn't exist.");
                            }
                        } else {
                            player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "That world has been already added!");
                        }
                    } else {
                        player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "That world doesn't exist!");
                    }
                } else {
                    player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "Argument error!");
                }
            }
            if(args[0].equalsIgnoreCase("visitworldbyname")) {
                if(args.length == 2) {
                    World world = Bukkit.getServer().getWorld(args[1]);
                    this.getArenaManager().joinArena(player, world.getUID().toString());
                }
            }
            if(args[0].equalsIgnoreCase("visitworldbyuuid")) {
                if(args.length == 2) {
                    this.getArenaManager().joinArena(player, args[1]);
                }
            }
            if(args[0].equalsIgnoreCase("setspawn")) {
                if(args.length == 1) {

                    double x = player.getLocation().getX();
                    double y = player.getLocation().getY();
                    double z = player.getLocation().getZ();
                    World world = player.getLocation().getWorld();

                    this.getParkourPlugin().getConfig().set("hubspawn.X", x);
                    this.getParkourPlugin().getConfig().set("hubspawn.Y", y);
                    this.getParkourPlugin().getConfig().set("hubspawn.Z", z);
                    this.getParkourPlugin().getConfig().set("hubspawn.World", world.getUID().toString());

                    this.getParkourPlugin().saveConfig();
                    player.sendMessage(this.getParkourPlugin().PREFIX + "Successfully set the hub spawn!");
                }
            }
            if(args[0].equalsIgnoreCase("spawn")) {
                double x = this.getParkourPlugin().getConfig().getDouble("hubspawn.X");
                double y = this.getParkourPlugin().getConfig().getDouble("hubspawn.Y");
                double z = this.getParkourPlugin().getConfig().getDouble("hubspawn.Z");
                World world = Bukkit.getWorld(UUID.fromString(this.getParkourPlugin().getConfig().getString("hubspawn.World")));

                Location location = new Location(world, x, y, z);
                player.sendMessage(this.getParkourPlugin().PREFIX + "Sending you to: " + ChatColor.GOLD + "" + ChatColor.BOLD + "Spawn");
                player.teleport(location);
            }
            if(args[0].equalsIgnoreCase("reload")) {
                if(args.length <= 1) {
                    this.getParkourPlugin().reloadConfig();
                    player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.GREEN + "Successfully reloaded configurations!");
                }
            }
            if(args[0].equalsIgnoreCase("joinarena")) {
                if(args.length == 2) {
                    World world = Bukkit.getWorld(args[1]);
                    String uuid = world.getUID().toString();
                    this.getArenaManager().joinArena(player, uuid);
                } else {
                    player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "Argument error!");
                }
            }
        }
        return true;
    }
}
