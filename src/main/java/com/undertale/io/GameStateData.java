package com.undertale.io;

public class GameStateData {
    private PlayerData player;
    private WorldData world;
    private long saveTime;
    private String saveVersion;

    public GameStateData() {
        this.player = new PlayerData();
        this.world = new WorldData();
        this.saveTime = System.currentTimeMillis();
        this.saveVersion = "1.0.0";
    }

    public GameStateData(PlayerData player, WorldData world) {
        this.player = player;
        this.world = world;
        this.saveTime = System.currentTimeMillis();
        this.saveVersion = "1.0.0";
    }

    public PlayerData getPlayer() {
        return player;
    }

    public void setPlayer(PlayerData player) {
        this.player = player;
    }

    public WorldData getWorld() {
        return world;
    }

    public void setWorld(WorldData world) {
        this.world = world;
    }

    public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    public String getSaveVersion() {
        return saveVersion;
    }

    public void setSaveVersion(String saveVersion) {
        this.saveVersion = saveVersion;
    }
}
