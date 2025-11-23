package com.undertale.io;

import java.util.HashMap;
import java.util.Map;

public class WorldData {
    private int mapWidth;
    private int mapHeight;
    private Map<String, Boolean> flags; // Game progress flags
    private String currentMapId;

    public WorldData() {
        this.flags = new HashMap<>();
    }

    public WorldData(int mapWidth, int mapHeight, String currentMapId) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.currentMapId = currentMapId;
        this.flags = new HashMap<>();
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public Map<String, Boolean> getFlags() {
        return flags;
    }

    public void setFlags(Map<String, Boolean> flags) {
        this.flags = flags;
    }

    public void setFlag(String key, boolean value) {
        flags.put(key, value);
    }

    public boolean getFlag(String key) {
        return flags.getOrDefault(key, false);
    }

    public String getCurrentMapId() {
        return currentMapId;
    }

    public void setCurrentMapId(String currentMapId) {
        this.currentMapId = currentMapId;
    }
}
