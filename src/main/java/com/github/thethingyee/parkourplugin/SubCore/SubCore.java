package com.github.thethingyee.parkourplugin.SubCore;

import com.github.thethingyee.parkourplugin.ParkourPlugin;
import com.github.thethingyee.parkourplugin.SubCore.managers.Arena;
import com.github.thethingyee.parkourplugin.SubCore.managers.ArenaManager;
import com.github.thethingyee.parkourplugin.SubCore.managers.PlayerManager;
import com.github.thethingyee.parkourplugin.SubCore.managers.WarpManager;

public class SubCore {

    private final Arena arena;
    private final ArenaManager arenaManager;
    private final PlayerManager playerManager;
    private final ParkourPlugin parkourPlugin;

    public SubCore(Arena arena, ArenaManager arenaManager, PlayerManager playerManager, ParkourPlugin parkourPlugin) {
        this.arena = arena;
        this.arenaManager = arenaManager;
        this.playerManager = playerManager;
        this.parkourPlugin = parkourPlugin;
    }

    public Arena getArena() {
        return arena;
    }
    public ArenaManager getArenaManager() {
        return arenaManager;
    }
    public PlayerManager getPlayerManager() {
        return playerManager;
    }
    public ParkourPlugin getParkourPlugin() {
        return parkourPlugin;
    }
}
