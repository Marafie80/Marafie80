package com.undertale.combat;

public enum CombatPhase {
    PLAYER_TURN,    // Player selects action
    ENEMY_TURN,     // Enemy attacks with bullet patterns
    RESOLUTION      // Process results and check for battle end
}
