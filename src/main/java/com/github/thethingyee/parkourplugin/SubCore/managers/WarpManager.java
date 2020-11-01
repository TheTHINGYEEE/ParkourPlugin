package com.github.thethingyee.parkourplugin.SubCore.managers;

import com.github.thethingyee.parkourplugin.SubCore.SubCore;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WarpManager {

    private final SubCore subCore;

    public WarpManager(SubCore subCore) {
        this.subCore = subCore;
    }

    public void createWarp(String warpername, Location location) {
        subCore.getParkourPlugin().getWarpsConfiguration().set("warps." + warpername + ".X", location.getX());
        subCore.getParkourPlugin().getWarpsConfiguration().set("warps." + warpername + ".Y", location.getY());
        subCore.getParkourPlugin().getWarpsConfiguration().set("warps." + warpername + ".Z", location.getZ());
        subCore.getParkourPlugin().getWarpsConfiguration().set("warps." + warpername + ".WorldUUID", location.getWorld().getUID().toString());
    }
    public List<String> getWarps() {
        Set<String> warp = subCore.getParkourPlugin().getWarpsConfiguration().getConfigurationSection("warps").getKeys(false);
        List<String> warps = new ArrayList<>(warp);
        return warps;
    }
    public void linkWarps(String warper, String dest) {
        subCore.getParkourPlugin().getWarpsConfiguration().getStringList("warps.warpers." + warper).add(dest);
    }
    public List<String> getLinks(String warper) {
        return subCore.getParkourPlugin().getWarpsConfiguration().getStringList("warps.warpers." + warper);
    }
}
