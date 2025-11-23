package com.undertale.combat;

import com.undertale.entity.Projectile;
import java.util.List;
import java.util.ArrayList;

public class CombatAI {
    private String enemyId;

    public CombatAI(String enemyId) {
        this.enemyId = enemyId;
    }

    public Action decideAction(CombatContext context) {
        // Default AI always attacks
        // More sophisticated AIs could analyze player state and choose different actions
        return Action.ATTACK;
    }

    public List<Projectile> generateAttackPattern(int centerX, int centerY) {
        // Generate a simple attack pattern
        // More sophisticated patterns would be implemented in subclasses
        List<Projectile> projectiles = new ArrayList<>();

        // Simple pattern: shoot projectiles in 4 directions
        projectiles.add(new Projectile("proj_1", centerX, centerY, 2, 0, 1, enemyId));
        projectiles.add(new Projectile("proj_2", centerX, centerY, -2, 0, 1, enemyId));
        projectiles.add(new Projectile("proj_3", centerX, centerY, 0, 2, 1, enemyId));
        projectiles.add(new Projectile("proj_4", centerX, centerY, 0, -2, 1, enemyId));

        return projectiles;
    }

    public String getEnemyId() {
        return enemyId;
    }
}
