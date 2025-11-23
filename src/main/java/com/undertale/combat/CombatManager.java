package com.undertale.combat;

import com.undertale.entity.Player;
import com.undertale.entity.Enemy;
import com.undertale.entity.Projectile;
import java.util.List;
import java.util.ArrayList;

public class CombatManager {
    private Player player;
    private Enemy enemy;
    private BattleState state;
    private List<Projectile> activeProjectiles;
    private boolean battleActive;
    private BattleResult result;

    public enum BattleResult {
        ONGOING,
        PLAYER_VICTORY,
        PLAYER_DEFEATED,
        ENEMY_SPARED,
        PLAYER_FLED
    }

    public CombatManager() {
        this.activeProjectiles = new ArrayList<>();
        this.battleActive = false;
        this.result = BattleResult.ONGOING;
    }

    public void startBattle(Player p, Enemy e) {
        this.player = p;
        this.enemy = e;
        this.state = new BattleState();
        this.battleActive = true;
        this.result = BattleResult.ONGOING;
        this.activeProjectiles.clear();

        // Transition to player turn
        state.transitionTo(CombatPhase.PLAYER_TURN);
    }

    public void update(float dt) {
        if (!battleActive) {
            return;
        }

        switch (state.getPhase()) {
            case PLAYER_TURN:
                // Waiting for player input
                break;

            case ENEMY_TURN:
                updateEnemyTurn(dt);
                break;

            case RESOLUTION:
                checkBattleEnd();
                break;
        }
    }

    private void updateEnemyTurn(float dt) {
        // Update all active projectiles
        for (int i = activeProjectiles.size() - 1; i >= 0; i--) {
            Projectile proj = activeProjectiles.get(i);
            proj.update(dt);

            if (!proj.isActive()) {
                activeProjectiles.remove(i);
            }
        }

        // If all projectiles are gone, move to resolution
        if (activeProjectiles.isEmpty()) {
            state.transitionTo(CombatPhase.RESOLUTION);
        }
    }

    public void playerAct(Action action) {
        if (!state.isPlayerTurn()) {
            return;
        }

        switch (action) {
            case ATTACK:
                performPlayerAttack();
                break;

            case ACT:
                performPlayerAct();
                break;

            case MERCY:
                performPlayerMercy();
                break;

            case ITEM:
                // Item usage would be handled separately with item selection
                break;
        }

        // Move to enemy turn if battle is still ongoing
        if (battleActive && result == BattleResult.ONGOING) {
            state.transitionTo(CombatPhase.ENEMY_TURN);
            enemyAct();
        }
    }

    private void performPlayerAttack() {
        // In Undertale, this would involve a timing minigame
        // For now, we'll just deal damage
        int damage = 10; // Base damage
        enemy.takeDamage(damage);
    }

    private void performPlayerAct() {
        // Increase mercy/spare chance
        player.addMercy(10);
    }

    private void performPlayerMercy() {
        // Try to spare the enemy
        if (player.getMercy() >= 100 || enemy.getHp() <= 0) {
            result = BattleResult.ENEMY_SPARED;
            battleActive = false;
        }
    }

    public void enemyAct() {
        if (enemy == null || !enemy.isAlive()) {
            return;
        }

        // Get enemy's action decision
        CombatContext context = new CombatContext(player, enemy);
        Action enemyAction = enemy.performTurn(context);

        // Generate attack pattern
        if (enemyAction == Action.ATTACK && enemy.getAi() != null) {
            List<Projectile> pattern = enemy.getAi().generateAttackPattern(320, 240);
            activeProjectiles.addAll(pattern);
        }
    }

    private void checkBattleEnd() {
        if (!player.isAlive()) {
            result = BattleResult.PLAYER_DEFEATED;
            battleActive = false;
        } else if (!enemy.isAlive()) {
            result = BattleResult.PLAYER_VICTORY;
            battleActive = false;
        } else {
            // Continue battle - back to player turn
            state.transitionTo(CombatPhase.PLAYER_TURN);
        }
    }

    public void endBattle() {
        battleActive = false;
        activeProjectiles.clear();
    }

    public Player getPlayer() {
        return player;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public BattleState getState() {
        return state;
    }

    public List<Projectile> getActiveProjectiles() {
        return new ArrayList<>(activeProjectiles);
    }

    public boolean isBattleActive() {
        return battleActive;
    }

    public BattleResult getResult() {
        return result;
    }
}
