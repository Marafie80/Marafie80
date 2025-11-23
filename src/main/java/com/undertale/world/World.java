package com.undertale.world;

import com.undertale.entity.Entity;
import com.undertale.engine.Renderer;

import java.util.ArrayList;
import java.util.List;

public class World {
    private TileMap tileMap;
    private List<Entity> entities;

    public World(int width, int height) {
        this.tileMap = new TileMap(width, height);
        this.entities = new ArrayList<>();
    }

    public World(TileMap tileMap) {
        this.tileMap = tileMap;
        this.entities = new ArrayList<>();
    }

    public void update(float dt) {
        // Update all entities
        for (Entity entity : entities) {
            entity.update(dt);
        }

        // Check for collisions
        checkCollisions();
    }

    private void checkCollisions() {
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                Entity e1 = entities.get(i);
                Entity e2 = entities.get(j);

                if (isColliding(e1, e2)) {
                    e1.onCollide(e2);
                    e2.onCollide(e1);
                }
            }
        }
    }

    private boolean isColliding(Entity e1, Entity e2) {
        // Simple bounding box collision
        int e1Left = e1.getX();
        int e1Right = e1.getX() + 32; // Assuming 32x32 sprites
        int e1Top = e1.getY();
        int e1Bottom = e1.getY() + 32;

        int e2Left = e2.getX();
        int e2Right = e2.getX() + 32;
        int e2Top = e2.getY();
        int e2Bottom = e2.getY() + 32;

        return e1Left < e2Right && e1Right > e2Left &&
               e1Top < e2Bottom && e1Bottom > e2Top;
    }

    public void render(Renderer renderer) {
        // Render tile map
        renderTileMap(renderer);

        // Render all entities
        for (Entity entity : entities) {
            entity.render(renderer);
        }
    }

    private void renderTileMap(Renderer renderer) {
        // Render tiles (simplified - would need sprite lookup in real implementation)
        for (int x = 0; x < tileMap.getWidth(); x++) {
            for (int y = 0; y < tileMap.getHeight(); y++) {
                // Tile rendering logic would go here
            }
        }
    }

    public void addEntity(Entity entity) {
        if (entity != null && !entities.contains(entity)) {
            entities.add(entity);
        }
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public List<Entity> getEntities() {
        return new ArrayList<>(entities);
    }

    public Entity getEntityById(String id) {
        for (Entity entity : entities) {
            if (entity.getId().equals(id)) {
                return entity;
            }
        }
        return null;
    }
}
