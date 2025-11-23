package com.undertale.world;

public class TileMap {
    private int width;
    private int height;
    private Tile[][] tiles;

    public TileMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width][height];

        // Initialize with default walkable tiles
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile(true, 0);
            }
        }
    }

    public Tile getTile(int x, int y) {
        if (isValidPosition(x, y)) {
            return tiles[x][y];
        }
        return null;
    }

    public void setTile(int x, int y, Tile tile) {
        if (isValidPosition(x, y)) {
            tiles[x][y] = tile;
        }
    }

    public boolean isWalkable(int x, int y) {
        if (!isValidPosition(x, y)) {
            return false;
        }
        Tile tile = tiles[x][y];
        return tile != null && tile.isWalkable();
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
