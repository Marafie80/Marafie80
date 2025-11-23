package com.undertale.world;

public class Tile {
    private boolean walkable;
    private int spriteId;

    public Tile(boolean walkable, int spriteId) {
        this.walkable = walkable;
        this.spriteId = spriteId;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public int getSpriteId() {
        return spriteId;
    }

    public void setSpriteId(int spriteId) {
        this.spriteId = spriteId;
    }
}
