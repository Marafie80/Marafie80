package com.undertale.entity;

import com.undertale.combat.CombatAI;
import com.undertale.combat.CombatContext;
import com.undertale.combat.Action;

public class Enemy extends Entity {
    private CombatAI ai;
    private int hp;
    private int maxHp;
    private int attackPower;
    private String name;
    private boolean defeated;

    public Enemy(String id, int x, int y, String name, int maxHp, int attackPower) {
        super(id, x, y);
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attackPower = attackPower;
        this.defeated = false;
    }

    @Override
    public void update(float dt) {
        // Enemy behavior in the overworld
        if (!defeated) {
            // Could have patrol patterns or chase behavior
        }
    }

    public Action performTurn(CombatContext context) {
        if (ai != null) {
            return ai.decideAction(context);
        }
        return Action.ATTACK; // Default to attacking
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) {
            hp = 0;
        }
        if (hp == 0) {
            onDefeated();
        }
    }

    public void onDefeated() {
        defeated = true;
        // Could drop items, give experience, etc.
    }

    @Override
    public void onCollide(Entity e) {
        if (e instanceof Player && !defeated) {
            // Could trigger combat
        }
    }

    public CombatAI getAi() {
        return ai;
    }

    public void setAi(CombatAI ai) {
        this.ai = ai;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDefeated() {
        return defeated;
    }

    public boolean isAlive() {
        return hp > 0 && !defeated;
    }
}
