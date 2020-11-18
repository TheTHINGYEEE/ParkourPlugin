package com.github.thethingyee.parkourplugin.commands;

import com.github.thethingyee.parkourplugin.ParkourPlugin;
import com.github.thethingyee.parkourplugin.SubCore.GUI.ParkourGUI;
import com.github.thethingyee.parkourplugin.SubCore.SubCore;
import com.github.thethingyee.parkourplugin.SubCore.managers.Arena;
import com.github.thethingyee.parkourplugin.SubCore.managers.ArenaManager;
import com.github.thethingyee.parkourplugin.SubCore.managers.PlayerManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class ParkourCommands extends SubCore implements CommandExecutor {

    private final ParkourGUI parkourGUI;

    public ParkourCommands(Arena arena, ArenaManager arenaManager, PlayerManager playerManager, ParkourPlugin parkourPlugin, ParkourGUI parkourGUI) {
        super(arena, arenaManager, playerManager, parkourPlugin);
        this.parkourGUI = parkourGUI;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(args[0].equalsIgnoreCase("createarena")) {
                if(args.length == 2) {
                    if(this.getParkourPlugin().getArenaConfig().getConfigurationSection("parkourarenas.worlds." + args[1]) == null) {
                        player.sendMessage(this.getParkourPlugin().PREFIX + "(" + args[1] + ") " + ChatColor.GRAY + "Creating world...");
                        WorldCreator world = new WorldCreator(args[1]);

                        world.environment(World.Environment.NORMAL);
                        world.type(WorldType.FLAT);
                        world.generateStructures(false);

                        World fworld = world.createWorld();
                        fworld.setDifficulty(Difficulty.PEACEFUL);
                        player.sendMessage(this.getParkourPlugin().PREFIX + "(" + args[1] + ") " + ChatColor.GRAY + "Complete.");
                        this.getParkourPlugin().getArenaConfig().set("parkourarenas.worlds." + fworld.getName() + ".owneruuid", player.getUniqueId().toString());
                        this.getParkourPlugin().getArenaConfig().set("parkourarenas.worlds." + fworld.getName() + ".ownername", player.getName());
                        this.getParkourPlugin().saveArenaConfig();

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
                        if(this.getParkourPlugin().getArenaConfig().getConfigurationSection("parkourarenas.worlds." + world.getName()) == null) {
                            if(target != null) {
                                this.getParkourPlugin().getArenaConfig().set("parkourarenas.worlds." + world.getName() + ".owneruuid", target.getUniqueId().toString());
                                this.getParkourPlugin().getArenaConfig().set("parkourarenas.worlds." + world.getName() + ".ownername", target.getName());
                                this.getParkourPlugin().saveArenaConfig();
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
                    if(world != null) {
                        this.getArenaManager().joinArena(player, world.getUID().toString());
                    } else {
                        player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "That arena/world doesn't exist!");
                    }
                }
            }
            if(args[0].equalsIgnoreCase("visitworldbyuuid")) {
                if(args.length == 2) {
                    try {
                        this.getArenaManager().joinArena(player, args[1]);
                    } catch(NullPointerException e) {
                        Bukkit.getConsoleSender().sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "Invalid uuid or world-uuid!");
                    }
                }
            }
            if(args[0].equalsIgnoreCase("sethub")) {
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
            if(args[0].equalsIgnoreCase("hub")) {
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
                    this.getParkourPlugin().reloadArenaConfig();
                    this.getParkourPlugin().reloadWarpsConfig();
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
            if(args[0].equalsIgnoreCase("arenas") || args[0].equalsIgnoreCase("parkourarenas")) {
                parkourGUI.loadArenaGraphicalUserInterface(player);
                player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.GREEN + "Successfully opened Parkour Arenas.");
            }
            if(args[0].equalsIgnoreCase("setspawn")) {
                if(args.length == 2) {
                    if(this.getArenaManager().arenaInDatabase(args[1])) {
                        if(this.getPlayerManager().onArena(player)) {
                            if(UUID.fromString(this.getPlayerManager().getUUIDofArenaOwner(args[1])).equals(player.getUniqueId())) {
                                if(this.getPlayerManager().getArenasOfPlayer(player).contains(args[1])) {
                                    this.getParkourPlugin().getArenaConfig().set("parkourarenas.worlds." + args[1] + ".X", player.getLocation().getX());
                                    this.getParkourPlugin().getArenaConfig().set("parkourarenas.worlds." + args[1] + ".Y", player.getLocation().getY());
                                    this.getParkourPlugin().getArenaConfig().set("parkourarenas.worlds." + args[1] + ".Z", player.getLocation().getZ());
                                    this.getParkourPlugin().getArenaConfig().set("parkourarenas.worlds." + args[1] + ".WorldUUID", player.getLocation().getWorld().getUID().toString());
                                    this.getParkourPlugin().saveArenaConfig();
                                    player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.GREEN + "Successfully set the spawn for: " + args[1]);
                                } else {
                                    player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "Sorry, but that is not your arena.");
                                }
                            } else {
                                player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "Sorry, only the owner is allowed to do this.");
                            }
                        } else {
                            player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "You need to be in an arena to do this.");
                        }
                    } else {
                        player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "Sorry, but this world is not on the database.");
                    }
                }
            }
            if(args[0].equalsIgnoreCase("spawn")) {
                if(args.length == 1) {
                    String worldname = player.getWorld().getName();
                    if(this.getPlayerManager().onArena(player)) {
                        try {
                            double x = this.getParkourPlugin().getArenaConfig().getDouble("parkourarenas.worlds." + worldname + ".X");
                            double y = this.getParkourPlugin().getArenaConfig().getDouble("parkourarenas.worlds." + worldname + ".Y");
                            double z = this.getParkourPlugin().getArenaConfig().getDouble("parkourarenas.worlds." + worldname + ".Z");
                            World world = Bukkit.getWorld(UUID.fromString(this.getParkourPlugin().getArenaConfig().getString("parkourarenas.worlds." + worldname + ".WorldUUID")));

                            player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.GRAY + "Sending you to: " + ChatColor.GOLD + "" + ChatColor.BOLD + world.getName() + " Spawn");
                            player.teleport(new Location(world, x, y, z));
                        } catch(NullPointerException e) {
                            player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "Spawn isn't setupped on this arena yet.");
                        }
                    }
                }
            }
            if(args[0].equalsIgnoreCase("createwarper")) {
                if(args.length == 2) {
                    if(!this.getParkourPlugin().getWarpManager().warperExists(args[1])) {

                        List<Entity> entityList = player.getNearbyEntities(2, 2, 2);
                        if(entityList.isEmpty()) {
                            String name = args[1];

                            Location playerloc = player.getLocation();
                            playerloc.subtract(0, 1, 0);
                            Block block = playerloc.getBlock();
                            block.setType(Material.ENDER_PORTAL_FRAME);

                            ArmorStand holo = (ArmorStand) player.getWorld().spawnEntity(block.getLocation().add(.5, 0, .5).subtract(0, 0.5, 0), EntityType.ARMOR_STAND);
                            holo.setCustomName(ChatColor.RED + "Warp not connected.");
                            holo.setGravity(false);
                            holo.setCustomNameVisible(true);
                            holo.setCollidable(true);
                            holo.setVisible(false);

                            this.getParkourPlugin().getWarpsConfiguration().set("warps.warpers." + name + ".UUID", holo.getUniqueId().toString());
                            this.getParkourPlugin().getWarpsConfiguration().set("warps.warpers." + name + ".Arena", holo.getWorld().getUID().toString());
                            this.getParkourPlugin().saveWarpsConfig();

                            player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.GREEN + "Successfully created a new warper!");
                        }
                        for(Entity entity : entityList) {
                            if(entity instanceof ArmorStand) {
                                player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "Sorry, but there is a warper here.");
                            }
                        }
                    } else {
                        player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "Sorry, but that warper name is already taken!");
                        player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "Individual arena warps will be added soon!");
                    }
                }
            }
            if(args[0].equalsIgnoreCase("connectwarp")) {
                String warper = args[1];
                String dest = args[2];

                List<String> warps = this.getParkourPlugin().getWarpManager().getWarps();
                if(this.getPlayerManager().getUUIDofArenaOwner(player.getWorld().getName()).equals(player.getUniqueId().toString())) {
                    if(warps.contains(warper) && warps.contains(dest)) {
                        if(this.getParkourPlugin().getWarpsConfiguration().getString("warps.warpers." + warper + ".Destination") == null) {
                            if(this.getParkourPlugin().getWarpsConfiguration().getString("warps.warpers." + dest + ".Warper") == null) {
                                this.getParkourPlugin().getWarpsConfiguration().set("warps.warpers." + warper + ".Destination", this.getParkourPlugin().getWarpsConfiguration().getString("warps.warpers." + dest + ".UUID"));
                                this.getParkourPlugin().getWarpsConfiguration().set("warps.warpers." + dest + ".Warper", this.getParkourPlugin().getWarpsConfiguration().getString("warps.warpers." + warper + ".UUID"));
                                this.getParkourPlugin().saveWarpsConfig();

                                player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.GREEN + "Successfully saved the configuration.");

                                String warperuuid = this.getParkourPlugin().getWarpsConfiguration().getString("warps.warpers." + warper + ".UUID");
                                String destuuid = this.getParkourPlugin().getWarpsConfiguration().getString("warps.warpers." + dest + ".UUID");

                                if(Bukkit.getEntity(UUID.fromString(warperuuid)) instanceof ArmorStand) {
                                    if(Bukkit.getEntity(UUID.fromString(destuuid)) instanceof ArmorStand) {
                                        ArmorStand warperEntity = (ArmorStand) Bukkit.getEntity(UUID.fromString(warperuuid));
                                        ArmorStand destEntity = (ArmorStand) Bukkit.getEntity(UUID.fromString(destuuid));

                                        warperEntity.setCustomName(ChatColor.GREEN + "Warper connected to " + ChatColor.GOLD + dest);
                                        destEntity.setCustomName(ChatColor.GREEN + "Destination connected to " + ChatColor.GOLD + warper);

                                        player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.GREEN + "Successfully set the hologram message.");
                                    }
                                }
                                player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.GREEN + "Successfully connected " + ChatColor.GOLD + warper + ChatColor.GREEN + " to " + ChatColor.GOLD + dest + ChatColor.GREEN + "!");
                            } else {
                                player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "That destination specified is already connected with another warper.");
                            }
                        } else {
                            player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "That warper specified is already connected with another destination.");
                        }
                    } else {
                        player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "Sorry, but neither the Warper is missing or the Destination.");
                    }
                } else {
                    player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "Sorry, but the owner of this arena could only do that.");
                }
            }
            if(args[0].equalsIgnoreCase("deletewarp")) {
                if(this.getParkourPlugin().getWarpManager().warperExists(args[1])) {
                    if(this.getParkourPlugin().getWarpManager().hasDestination(args[1])) {
                        ArmorStand as = (ArmorStand) Bukkit.getEntity(UUID.fromString(this.getParkourPlugin().getWarpsConfiguration().getString("warps.warpers." + args[1] + ".UUID")));
                        as.getLocation().add(0, 2, 0).getBlock().setType(Material.REDSTONE_BLOCK);
                        as.remove();

                        ArmorStand dest = (ArmorStand) Bukkit.getEntity(UUID.fromString(this.getParkourPlugin().getWarpsConfiguration().getString("warps.warpers." + args[1] + ".Destination")));
                        dest.setCustomName(ChatColor.RED + "Warp not connected.");
                        
                        this.getParkourPlugin().getWarpsConfiguration().set("warps.warpers." + args[1], null);
                        this.getParkourPlugin().saveWarpsConfig();
                        player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.GREEN + "Deleted warp '" + args[1] + "'");
                    } else if(this.getParkourPlugin().getWarpManager().hasWarper(args[1])) {
                        ArmorStand as = (ArmorStand) Bukkit.getEntity(UUID.fromString(this.getParkourPlugin().getWarpsConfiguration().getString("warps.warpers." + args[1] + ".UUID")));
                        as.getLocation().add(0, 2, 0).getBlock().setType(Material.REDSTONE_BLOCK);
                        as.remove();

                        ArmorStand dest = (ArmorStand) Bukkit.getEntity(UUID.fromString(this.getParkourPlugin().getWarpsConfiguration().getString("warps.warpers." + args[1] + ".Warper")));
                        dest.setCustomName(ChatColor.RED + "Warp not connected.");

                        this.getParkourPlugin().getWarpsConfiguration().set("warps.warpers." + args[1], null);
                        this.getParkourPlugin().saveWarpsConfig();
                        player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.GREEN + "Deleted warp '" + args[1] + "'");
                    }
                } else {
                    player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "That warper doesn't exist.");
                }
            }
        }
        return true;
    }
}
