package com.undertale.combat;

import com.undertale.entity.Player;
import com.undertale.entity.Enemy;

public class CombatContext {
    private PlayerSnapshot playerSnapshot;
    private EnemySnapshot enemySnapshot;

    public CombatContext(Player player, Enemy enemy) {
        this.playerSnapshot = new PlayerSnapshot(player);
        this.enemySnapshot = new EnemySnapshot(enemy);
    }

    public PlayerSnapshot getPlayerSnapshot() {
        return playerSnapshot;
    }

    public EnemySnapshot getEnemySnapshot() {
        return enemySnapshot;
    }

    public static class PlayerSnapshot {
        private int health;
        private int maxHealth;
        private int mercy;

        public PlayerSnapshot(Player player) {
            this.health = player.getHealth();
            this.maxHealth = player.getMaxHealth();
            this.mercy = player.getMercy();
        }

        public int getHealth() {
            return health;
        }

        public int getMaxHealth() {
            return maxHealth;
        }

        public int getMercy() {
            return mercy;
        }
    }

    public static class EnemySnapshot {
        private int hp;
        private int maxHp;
        private int attackPower;
        private String name;

        public EnemySnapshot(Enemy enemy) {
            this.hp = enemy.getHp();
            this.maxHp = enemy.getMaxHp();
            this.attackPower = enemy.getAttackPower();
            this.name = enemy.getName();
        }

        public int getHp() {
            return hp;
        }

        public int getMaxHp() {
            return maxHp;
        }

        public int getAttackPower() {
            return attackPower;
        }

        public String getName() {
            return name;
        }
    }
}
