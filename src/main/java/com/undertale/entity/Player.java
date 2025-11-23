package com.undertale.entity;

import com.undertale.engine.InputManager;
import com.undertale.engine.KeyCode;
import com.undertale.combat.CombatManager;

public class Player extends Entity {
    private Inventory inventory;
    private int health;
    private int maxHealth;
    private int mercy;
    private InputManager input;
    private int speed;

    public Player(String id, int x, int y, InputManager input) {
        super(id, x, y);
        this.input = input;
        this.inventory = new Inventory(8); // Undertale has 8 item slots
        this.maxHealth = 20;
        this.health = maxHealth;
        this.mercy = 0;
        this.speed = 4;
    }

    @Override
    public void update(float dt) {
        // Handle movement based on input
        float horizontalAxis = input.getAxis("horizontal");
        float verticalAxis = input.getAxis("vertical");

        if (horizontalAxis != 0 || verticalAxis != 0) {
            x += (int)(horizontalAxis * speed);
            y += (int)(verticalAxis * speed);
        }
    }

    public void move(Direction direction) {
        x += direction.getDx() * speed;
        y += direction.getDy() * speed;
    }

    public void interact() {
        // Interact with nearby entities/objects
        // This would be implemented based on the game's specific needs
    }

    public void startCombat(Enemy enemy, CombatManager combatManager) {
        if (combatManager != null) {
            combatManager.startBattle(this, enemy);
        }
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }

    public void heal(int amount) {
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        if (this.health > maxHealth) {
            this.health = maxHealth;
        }
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

    public void addMercy(int amount) {
        this.mercy += amount;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isAlive() {
        return health > 0;
    }
}
