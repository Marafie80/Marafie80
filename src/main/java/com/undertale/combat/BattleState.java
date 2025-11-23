package com.undertale.combat;

public class BattleState {
    private CombatPhase phase;
    private int turnCount;

    public BattleState() {
        this.phase = CombatPhase.PLAYER_TURN;
        this.turnCount = 0;
    }

    public void transitionTo(CombatPhase newPhase) {
        this.phase = newPhase;

        if (newPhase == CombatPhase.PLAYER_TURN) {
            turnCount++;
        }
    }

    public CombatPhase getPhase() {
        return phase;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public boolean isPlayerTurn() {
        return phase == CombatPhase.PLAYER_TURN;
    }

    public boolean isEnemyTurn() {
        return phase == CombatPhase.ENEMY_TURN;
    }

    public boolean isResolution() {
        return phase == CombatPhase.RESOLUTION;
    }
}
