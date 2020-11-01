package com.github.thethingyee.parkourplugin;

import com.github.thethingyee.parkourplugin.SubCore.GUI.GUIClickEvent;
import com.github.thethingyee.parkourplugin.SubCore.GUI.ParkourGUI;
import com.github.thethingyee.parkourplugin.SubCore.SubCore;
import com.github.thethingyee.parkourplugin.SubCore.listeners.BlockListener;
import com.github.thethingyee.parkourplugin.SubCore.listeners.DamageListener;
import com.github.thethingyee.parkourplugin.SubCore.listeners.InteractNCWarps;
import com.github.thethingyee.parkourplugin.SubCore.managers.Arena;
import com.github.thethingyee.parkourplugin.SubCore.managers.ArenaManager;
import com.github.thethingyee.parkourplugin.SubCore.managers.PlayerManager;
import com.github.thethingyee.parkourplugin.SubCore.managers.WarpManager;
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
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public final class ParkourPlugin extends JavaPlugin {

    public String PREFIX = ChatColor.GRAY + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "Parkour" + ChatColor.GRAY + "] " + ChatColor.GRAY;

    private Title title = new Title();
    private WarpManager warpManager;

    public File arenafile;
    public YamlConfiguration arenaconfiguration;

    public File warpsfile;
    public YamlConfiguration warpsconfiguration;

    @Override
    public void onEnable() {

        try {

            // Load configs.
            this.getConfig().options().copyDefaults(true);
            saveConfig();

            loadArenaConfiguration();
            saveArenaConfig();

            loadWarpConfiguration();
            saveWarpsConfig();


            // Register classes.
            new SubCore(new Arena(this), new ArenaManager(this), new PlayerManager(this), this);
            new ParkourGUI(new SubCore(new Arena(this), new ArenaManager(this), new PlayerManager(this), this));
            warpManager = new WarpManager(new SubCore(new Arena(this), new ArenaManager(this), new PlayerManager(this), this));
            new Arena(this);

            // Register commands.
            this.getCommand("parkour").setExecutor(new ParkourCommands(new Arena(this), new ArenaManager(this), new PlayerManager(this), this, new ParkourGUI(new SubCore(new Arena(this), new ArenaManager(this), new PlayerManager(this), this))));
            this.getCommand("findworldbyuuid").setExecutor(new FindWorldbyUUID());
            this.getCommand("unloadworld").setExecutor(new UnloadWorld());

            // load worlds CREATED by the plugin itself. If it exists. It loads it. If it doesn't exist, it creates a new one.
            try {
                for(String worlds : this.getArenaConfig().getConfigurationSection("parkourarenas.worlds").getKeys(false)) {
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
            this.getServer().getPluginManager().registerEvents(new GUIClickEvent(new Arena(this), new ArenaManager(this), new PlayerManager(this), this), this);
            this.getServer().getPluginManager().registerEvents(new InteractNCWarps(new Arena(this), new ArenaManager(this), new PlayerManager(this), new ParkourGUI(new SubCore(new Arena(this), new ArenaManager(this), new PlayerManager(this), this)), this), this);

            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.GREEN + "Plugin successfully loaded!");

        } catch(Exception e) {
            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.RED + "Plugin failed to initialize! Error: " + e.getMessage());
            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.RED + "Shutting plugin down.");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
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
    public Set<String> getStringWorlds() { return this.getArenaConfig().getConfigurationSection("parkourarenas.worlds").getKeys(false); }
    public void loadArenaConfiguration() {
        try {
            arenafile = new File(this.getDataFolder(), "arenas.yml");
            if(!this.getDataFolder().exists()) {
                this.getDataFolder().mkdir();
            }
            if(!arenafile.exists()) {
                arenafile.createNewFile();
            }
            arenaconfiguration = new YamlConfiguration();
            arenaconfiguration.load(arenafile);
        } catch(IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadArenaConfig() {
        try {
            arenaconfiguration.load(arenafile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void saveArenaConfig() {
        try {
            arenaconfiguration.save(arenafile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public YamlConfiguration getArenaConfig() {
        return arenaconfiguration;
    }
    public void loadWarpConfiguration() {
        try {
            warpsfile = new File(this.getDataFolder(), "warps.yml");

            if (!this.getDataFolder().exists()) {
                this.getDataFolder().mkdir();
            }
            if (!warpsfile.exists()) {
                warpsfile.createNewFile();
            }

            warpsconfiguration = new YamlConfiguration();
            warpsconfiguration.load(warpsfile);
        } catch(IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void saveWarpsConfig() {
        try {
            warpsconfiguration.save(warpsfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void reloadWarpsConfig() {
        try {
            warpsconfiguration.load(warpsfile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public YamlConfiguration getWarpsConfiguration() { return warpsconfiguration; }

    public WarpManager getWarpManager() {
        return warpManager;
    }
}
