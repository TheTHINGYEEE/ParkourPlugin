package com.github.thethingyee.parkourplugin;

import com.github.thethingyee.parkourplugin.SubCore.GUI.GUIClickEvent;
import com.github.thethingyee.parkourplugin.SubCore.GUI.ParkourGUI;
import com.github.thethingyee.parkourplugin.SubCore.SubCore;
import com.github.thethingyee.parkourplugin.SubCore.listeners.*;
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
import java.io.FileNotFoundException;
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

    public File spawnFile;
    public YamlConfiguration spawnConfiguration;

    @Override
    public void onEnable() {
        try {

            long start = System.nanoTime();

            long configStart = System.nanoTime();
            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.GOLD + "Loading configurations...");

            // Load configs.
            this.getConfig().options().copyDefaults(true);
            saveConfig();

            loadArenaConfiguration();
            saveArenaConfig();

            loadWarpConfiguration();
            saveWarpsConfig();

            long configEnd = System.nanoTime();
            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.GREEN + "Loaded configurations! (" + ((configEnd - configStart) / 1000000) + "ms)");

            // Register classes.
            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.GOLD + "Loading classes...");
            long classesStart = System.nanoTime();

            new SubCore(new Arena(this), new ArenaManager(this), new PlayerManager(this), this);
            new ParkourGUI(new SubCore(new Arena(this), new ArenaManager(this), new PlayerManager(this), this));
            warpManager = new WarpManager(new SubCore(new Arena(this), new ArenaManager(this), new PlayerManager(this), this));
            new Arena(this);

            long classesEnd = System.nanoTime();
            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.GREEN + "Classes loaded! (" + ((classesEnd - classesStart) / 1000000) + "ms)");

            // Register commands.
            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.GOLD + "Registering commands...");
            long commandsStart = System.nanoTime();

            this.getCommand("parkour").setExecutor(new ParkourCommands(new Arena(this), new ArenaManager(this), new PlayerManager(this), this, new ParkourGUI(new SubCore(new Arena(this), new ArenaManager(this), new PlayerManager(this), this))));
            this.getCommand("findworldbyuuid").setExecutor(new FindWorldbyUUID());
            this.getCommand("unloadworld").setExecutor(new UnloadWorld());

            long commandsEnd = System.nanoTime();
            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.GREEN + "Registered commands! (" + ((classesEnd - classesStart) / 1000000) + "ms)");

            // load worlds CREATED by the plugin itself. If it exists. It loads it. If it doesn't exist, it creates a new one.
            long worldStart = System.nanoTime();
            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.GOLD + "Loading worlds...");

            try {
                for(String worlds : this.getArenaConfig().getConfigurationSection("parkourarenas.worlds").getKeys(false)) {
                    this.getServer().createWorld(new WorldCreator(worlds));
                }
            } catch(NullPointerException e) {
                Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.RED + "Error catched! No arenas found.");
            }

            long worldEnd = System.nanoTime();
            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.GREEN + "Loaded worlds! (" + ((worldEnd - worldStart) / 1000000) + "ms)");

            // Register listeners.
            long listenerStart = System.nanoTime();
            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.GOLD + "Registering listeners...");

            this.getServer().getPluginManager().registerEvents(new ParkourJoinListeners(this, new HubScoreboard(this), new ArenaScoreboard(this)), this);
            this.getServer().getPluginManager().registerEvents(new ParkourChangeWorld(new HubScoreboard(this), new ArenaScoreboard(this), this), this);
            this.getServer().getPluginManager().registerEvents(new ParkourMobSpawn(this), this);
            this.getServer().getPluginManager().registerEvents(new ParkourLeaveListeners(new HubScoreboard(this), new ArenaScoreboard(this), this), this);
            this.getServer().getPluginManager().registerEvents(new deltethis(), this);

            // Register SubCore functions listeners.
            this.getServer().getPluginManager().registerEvents(new BlockListener(new Arena(this), new ArenaManager(this), new PlayerManager(this), this), this);
            this.getServer().getPluginManager().registerEvents(new DamageListener(new Arena(this), new ArenaManager(this), new PlayerManager(this), this), this);
            this.getServer().getPluginManager().registerEvents(new GUIClickEvent(new Arena(this), new ArenaManager(this), new PlayerManager(this), this), this);
            this.getServer().getPluginManager().registerEvents(new WarpListener(new SubCore(new Arena(this), new ArenaManager(this), new PlayerManager(this), this)), this);
            this.getServer().getPluginManager().registerEvents(new CreateSpawnListener(new Arena(this), new ArenaManager(this), new PlayerManager(this), this), this);

            long listenerEnd = System.nanoTime();
            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.GREEN + "Listeners registered! (" + ((listenerEnd - listenerStart) / 1000000) + "ms)");

            final long end = System.nanoTime();
            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.GREEN + "Plugin successfully loaded! " + "(" + ((end - start) / 1000000) + "ms)");

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

    public void loadSpawnConfiguration() {
        try {
            spawnFile = new File("spawns.yml");

            if (!this.getDataFolder().exists()) {
                this.getDataFolder().mkdir();
            }
            if (!spawnFile.exists()) {
                spawnFile.createNewFile();
            }
            spawnConfiguration = new YamlConfiguration();
            spawnConfiguration.load(spawnFile);
        } catch(IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadSpawnConfig() {
        try {
            spawnConfiguration.load(spawnFile);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveSpawnConfig() {
        try {
            spawnConfiguration.save(spawnFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getSpawnConfiguration() {
        return spawnConfiguration;
    }
}
