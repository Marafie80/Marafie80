package com.undertale.entity;

import com.undertale.engine.Renderer;
import com.undertale.engine.Sprite;

public abstract class Entity {
    protected String id;
    protected int x;
    protected int y;
    protected Sprite sprite;

    public Entity(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public abstract void update(float dt);

    public void render(Renderer r) {
        if (sprite != null) {
            r.drawImage(sprite, x, y);
        }
    }

    public void onCollide(Entity e) {
        // Default implementation - can be overridden
    }

    public String getId() {
        return id;
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

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
