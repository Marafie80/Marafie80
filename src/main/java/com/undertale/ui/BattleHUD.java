package com.undertale.ui;

import com.undertale.engine.Renderer;
import com.undertale.combat.CombatManager;
import com.undertale.combat.Action;
import com.undertale.engine.InputEvent;
import com.undertale.engine.KeyCode;

public class BattleHUD extends UIElement {
    private int playerHP;
    private int playerMaxHP;
    private int enemyHP;
    private int enemyMaxHP;
    private String enemyName;
    private Action selectedAction;
    private int actionIndex;
    private CombatManager combatManager;

    private static final Action[] ACTIONS = {Action.ATTACK, Action.ACT, Action.MERCY, Action.ITEM};

    public BattleHUD(int x, int y, CombatManager combatManager) {
        super(x, y);
        this.combatManager = combatManager;
        this.selectedAction = Action.ATTACK;
        this.actionIndex = 0;
    }

    @Override
    public void render(Renderer renderer) {
        if (!visible) {
            return;
        }

        // Draw player HP bar
        int hpBarY = y + 20;
        renderer.drawText("HP: " + playerHP + "/" + playerMaxHP, x, hpBarY);
        renderer.drawRect(x + 100, hpBarY, 200, 20);

        // Draw HP fill
        int hpFillWidth = (int) ((float) playerHP / playerMaxHP * 200);
        renderer.drawFilledRect(x + 100, hpBarY, hpFillWidth, 20);

        // Draw enemy name and HP
        if (enemyName != null) {
            renderer.drawText(enemyName, x, y + 60);
            renderer.drawText("HP: " + enemyHP + "/" + enemyMaxHP, x, y + 80);
        }

        // Draw action menu
        int menuY = y + 120;
        for (int i = 0; i < ACTIONS.length; i++) {
            String prefix = (i == actionIndex) ? "> " : "  ";
            renderer.drawText(prefix + ACTIONS[i].name(), x + (i * 100), menuY);
        }
    }

    @Override
    public void update(float dt) {
        if (combatManager != null) {
            updateStats(
                combatManager.getPlayer().getHealth(),
                combatManager.getPlayer().getMaxHealth(),
                combatManager.getEnemy().getHp(),
                combatManager.getEnemy().getMaxHp(),
                combatManager.getEnemy().getName()
            );
        }
    }

    @Override
    public void handleInput(InputEvent event) {
        if (!active || event.getType() != InputEvent.EventType.KEY_PRESSED) {
            return;
        }

        switch (event.getKeyCode()) {
            case LEFT:
                actionIndex = (actionIndex - 1 + ACTIONS.length) % ACTIONS.length;
                selectedAction = ACTIONS[actionIndex];
                break;

            case RIGHT:
                actionIndex = (actionIndex + 1) % ACTIONS.length;
                selectedAction = ACTIONS[actionIndex];
                break;

            case Z:
            case ENTER:
                if (combatManager != null) {
                    combatManager.playerAct(selectedAction);
                }
                break;
        }
    }

    public void updateStats(int playerHP, int playerMaxHP, int enemyHP, int enemyMaxHP, String enemyName) {
        this.playerHP = playerHP;
        this.playerMaxHP = playerMaxHP;
        this.enemyHP = enemyHP;
        this.enemyMaxHP = enemyMaxHP;
        this.enemyName = enemyName;
    }

    public Action getSelectedAction() {
        return selectedAction;
    }
}
