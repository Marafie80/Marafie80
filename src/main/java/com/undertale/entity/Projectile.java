package com.undertale.entity;

public class Projectile extends Entity {
    private int vx; // Velocity X
    private int vy; // Velocity Y
    private int damage;
    private boolean active;
    private String ownerId; // ID of entity that created this projectile

    public Projectile(String id, int x, int y, int vx, int vy, int damage, String ownerId) {
        super(id, x, y);
        this.vx = vx;
        this.vy = vy;
        this.damage = damage;
        this.ownerId = ownerId;
        this.active = true;
    }

    @Override
    public void update(float dt) {
        if (active) {
            x += (int)(vx * dt);
            y += (int)(vy * dt);

            // Deactivate if out of bounds (assuming screen size 640x480)
            if (x < 0 || x > 640 || y < 0 || y > 480) {
                active = false;
            }
        }
    }

    @Override
    public void onCollide(Entity e) {
        // Don't collide with owner
        if (e.getId().equals(ownerId)) {
            return;
        }

        if (e instanceof Player) {
            ((Player) e).takeDamage(damage);
            active = false;
        }
    }

    public int getVx() {
        return vx;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public int getVy() {
        return vy;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getOwnerId() {
        return ownerId;
    }
}
