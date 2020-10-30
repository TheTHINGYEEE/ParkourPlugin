package com.github.thethingyee.parkourplugin.scoreboards;

import com.github.thethingyee.parkourplugin.ParkourPlugin;

public class ParkourScoreboards {

    private final HubScoreboard hubScoreboard;
    private final ArenaScoreboard arenaScoreboard;
    private final ParkourPlugin parkourPlugin;

    public ParkourScoreboards(HubScoreboard hubScoreboard, ArenaScoreboard arenaScoreboard, ParkourPlugin parkourPlugin) {
        this.hubScoreboard = hubScoreboard;
        this.arenaScoreboard = arenaScoreboard;
        this.parkourPlugin = parkourPlugin;
    }

    public ArenaScoreboard getArenaScoreboard() {
        return arenaScoreboard;
    }
    public HubScoreboard getHubScoreboard() {
        return hubScoreboard;
    }
    public ParkourPlugin getParkourPlugin() {
        return parkourPlugin;
    }
}
