package com.undertale.io;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {
    private int x;
    private int y;
    private int health;
    private int maxHealth;
    private int mercy;
    private List<String> inventoryItemIds;

    public PlayerData() {
        this.inventoryItemIds = new ArrayList<>();
    }

    public PlayerData(int x, int y, int health, int maxHealth, int mercy, List<String> inventoryItemIds) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.maxHealth = maxHealth;
        this.mercy = mercy;
        this.inventoryItemIds = inventoryItemIds != null ? inventoryItemIds : new ArrayList<>();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getMercy() {
        return mercy;
    }

    public void setMercy(int mercy) {
        this.mercy = mercy;
    }

    public List<String> getInventoryItemIds() {
        return inventoryItemIds;
    }

    public void setInventoryItemIds(List<String> inventoryItemIds) {
        this.inventoryItemIds = inventoryItemIds;
    }
}
