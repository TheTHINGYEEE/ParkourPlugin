package com.github.thethingyee.parkourplugin.SubCore.managers;

import com.github.thethingyee.parkourplugin.ParkourPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerManager extends ArenaManager {

    public PlayerManager(ParkourPlugin parkourPlugin) {
        super(parkourPlugin);
    }

    public String getUUIDofArenaOwner(String arenaname) {
        return this.getMainClass().getArenaConfig().getString("parkourarenas.worlds." + arenaname + ".owneruuid");
    }
    public String getNameofArenaOwner(String arenaname) {
        return this.getMainClass().getArenaConfig().getString("parkourarenas.worlds." + arenaname + ".ownername");
    }
    public List<Player> getPlayersOnArena(String worlduuid) {
        World world = Bukkit.getWorld(UUID.fromString(worlduuid));
        return world.getPlayers();
    }
    public int getNumberOnlinePlayersOnArena(String worlduuid) {
        World world = Bukkit.getWorld(UUID.fromString(worlduuid));
        return world.getPlayers().size();
    }
    public int getNumberGlobalOnlinePlayers() {
        return Bukkit.getOnlinePlayers().size();
    }
    public Collection<? extends Player> getGlobalOnlinePlayers() {
        return Bukkit.getOnlinePlayers();
    }
    public boolean onHub(Player player) {
        UUID worlduuid = UUID.fromString(this.getMainClass().getConfig().getString("hubspawn.World"));
        return player.getWorld().getUID().equals(worlduuid);
    }
    public List<String> getArenasOfPlayer(Player player) {
        List<String> arenas = new ArrayList<>();
        for(int i = 0; i < this.getArenas().size(); i++) {
            List<String> arena = new ArrayList<>(this.getArenas());
            String configuuid = this.getMainClass().getArenaConfig().getString("parkourarenas.worlds." + arena.get(i) + ".owneruuid");
            String playeruuid = player.getUniqueId().toString();
            if(configuuid.equals(playeruuid)) {
                arenas.add(arena.get(i));
            }
        }
        return arenas;
    }
}
