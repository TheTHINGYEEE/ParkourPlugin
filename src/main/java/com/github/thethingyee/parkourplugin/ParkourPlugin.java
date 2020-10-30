package com.github.thethingyee.parkourplugin;

import com.github.thethingyee.parkourplugin.SubCore.SubCore;
import com.github.thethingyee.parkourplugin.SubCore.listeners.BlockListener;
import com.github.thethingyee.parkourplugin.SubCore.listeners.DamageListener;
import com.github.thethingyee.parkourplugin.SubCore.managers.Arena;
import com.github.thethingyee.parkourplugin.SubCore.managers.ArenaManager;
import com.github.thethingyee.parkourplugin.SubCore.managers.PlayerManager;
import com.github.thethingyee.parkourplugin.apis.Title;
import com.github.thethingyee.parkourplugin.commands.FindWorldbyUUID;
import com.github.thethingyee.parkourplugin.commands.ParkourCommands;
import com.github.thethingyee.parkourplugin.commands.UnloadWorld;
import com.github.thethingyee.parkourplugin.listeners.ParkourChangeWorld;
import com.github.thethingyee.parkourplugin.listeners.ParkourJoinListeners;
import com.github.thethingyee.parkourplugin.listeners.ParkourLeaveListeners;
import com.github.thethingyee.parkourplugin.listeners.ParkourMobSpawn;
import com.github.thethingyee.parkourplugin.scoreboards.ArenaScoreboard;
import com.github.thethingyee.parkourplugin.scoreboards.HubScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public final class ParkourPlugin extends JavaPlugin {

    public String PREFIX = ChatColor.GRAY + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "Parkour" + ChatColor.GRAY + "] " + ChatColor.GRAY;
    private Title title = new Title();

    @Override
    public void onEnable() {

        try {

            // Load config.
            this.getConfig().options().copyDefaults(true);
            saveConfig();

            // Register classes.
            new SubCore(new Arena(this), new ArenaManager(this), new PlayerManager(this), this);
            new Arena(this);


            // Register commands.
            this.getCommand("parkour").setExecutor(new ParkourCommands(new Arena(this), new ArenaManager(this), new PlayerManager(this), this));
            this.getCommand("findworldbyuuid").setExecutor(new FindWorldbyUUID());
            this.getCommand("unloadworld").setExecutor(new UnloadWorld());

            // load worlds CREATED by the plugin itself. If it exists. It loads it. If it doesn't exist, it creates a new one.
            try {
                for(String worlds : this.getConfig().getConfigurationSection("parkourarenas.worlds").getKeys(false)) {
                    this.getServer().createWorld(new WorldCreator(worlds));
                }
            } catch(NullPointerException e) {
                Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.RED + "Error catched! No arenas found.");
            }

            // Register listeners.
            this.getServer().getPluginManager().registerEvents(new ParkourJoinListeners(this, new HubScoreboard(this), new ArenaScoreboard(this)), this);
            this.getServer().getPluginManager().registerEvents(new ParkourChangeWorld(new HubScoreboard(this), new ArenaScoreboard(this), this), this);
            this.getServer().getPluginManager().registerEvents(new ParkourMobSpawn(this), this);
            this.getServer().getPluginManager().registerEvents(new ParkourLeaveListeners(new HubScoreboard(this), new ArenaScoreboard(this), this), this);

            // Register SubCore functions listeners.
            this.getServer().getPluginManager().registerEvents(new BlockListener(new Arena(this), new ArenaManager(this), new PlayerManager(this), this), this);
            this.getServer().getPluginManager().registerEvents(new DamageListener(new Arena(this), new ArenaManager(this), new PlayerManager(this), this), this);


            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.GREEN + "Plugin successfully loaded!");

        } catch(Exception e) {
            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.RED + "Plugin failed to initialize! Error: " + e.getMessage());
            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.RED + "Shutting plugin down.");
            Bukkit.shutdown();
        }
    }

    @Override
    public void onDisable() {
        saveConfig();
        Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.RED + "Plugin successfully disabled.");
    }

    public Title getTitle() {
        return title;
    }
    public Set<String> getStringWorlds() { return this.getConfig().getConfigurationSection("parkourarenas.worlds").getKeys(false); }
}
