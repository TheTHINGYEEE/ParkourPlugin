package com.github.thethingyee.parkourplugin.SubCore.managers;

import com.github.thethingyee.parkourplugin.SubCore.SubCore;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class WarpManager {

    private final SubCore subCore;

    public WarpManager(SubCore subCore) {
        this.subCore = subCore;
    }

    public List<String> getWarps() {
        List<String> converted = new ArrayList<>(subCore.getParkourPlugin().getWarpsConfiguration().getConfigurationSection("warps.warpers").getKeys(false));
        return converted;
    }
    public List<String> getWarpsOnArena(UUID arenauuid) {
        List<String> warps = new ArrayList<>();
        for(int i = 0; i < this.getWarps().size(); i++) {
            List<String> w = new ArrayList<>(this.getWarps());
            String configuuid = subCore.getParkourPlugin().getArenaConfig().getString("warps.warpers." + w.get(i) + ".Arena");
            String worlduuid = arenauuid.toString();
            if(configuuid.equals(worlduuid)) {
                warps.add(w.get(i));
            }
        }
        return warps;
    }
    public UUID getDestinationUUID(String warper) {
        return UUID.fromString(subCore.getParkourPlugin().getWarpsConfiguration().getString("warps.warpers." + warper + ".Destination"));
    }
    public UUID getWarperUUID(String dest) {
        return UUID.fromString(subCore.getParkourPlugin().getWarpsConfiguration().getString("warps.warpers." + dest + ".Warper"));
    }
    public boolean hasDestination(String warper) {
        return (subCore.getParkourPlugin().getWarpsConfiguration().getString("warps.warpers." + warper + ".Destination") != null);
    }
    public boolean hasWarper(String dest) {
        return (subCore.getParkourPlugin().getWarpsConfiguration().getString("warps.warpers." + dest + ".Warper") != null);
    }
    public boolean warperExists(String warper) {
        return (getWarps().contains(warper));
    }
}
