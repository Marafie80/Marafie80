package com.undertale.ui;

import com.undertale.engine.Renderer;
import com.undertale.engine.InputEvent;
import com.undertale.engine.InputManager;
import com.undertale.entity.Projectile;

public class SoulController extends UIElement {
    private Vector2 position;
    private Vector2 velocity;
    private float speed;
    private int boxLeft, boxTop, boxRight, boxBottom; // Battle box boundaries
    private InputManager inputManager;

    public SoulController(int x, int y, InputManager inputManager) {
        super(x, y);
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 0);
        this.speed = 3.0f;
        this.inputManager = inputManager;

        // Default battle box (can be customized)
        this.boxLeft = 220;
        this.boxTop = 280;
        this.boxRight = 420;
        this.boxBottom = 380;
    }

    @Override
    public void render(Renderer renderer) {
        if (!visible) {
            return;
        }

        // Draw battle box
        renderer.drawRect(boxLeft, boxTop, boxRight - boxLeft, boxBottom - boxTop);

        // Draw soul (red heart)
        renderer.drawFilledRect((int) position.x - 5, (int) position.y - 5, 10, 10);
    }

    @Override
    public void update(float dt) {
        if (!active) {
            return;
        }

        // Get input
        float horizontalAxis = inputManager.getAxis("horizontal");
        float verticalAxis = inputManager.getAxis("vertical");

        // Update velocity
        velocity.x = horizontalAxis * speed;
        velocity.y = verticalAxis * speed;

        // Update position
        position.x += velocity.x;
        position.y += velocity.y;

        // Constrain to battle box
        if (position.x < boxLeft) position.x = boxLeft;
        if (position.x > boxRight) position.x = boxRight;
        if (position.y < boxTop) position.y = boxTop;
        if (position.y > boxBottom) position.y = boxBottom;

        // Update UI element position
        x = (int) position.x;
        y = (int) position.y;
    }

    public boolean collideWith(Projectile projectile) {
        if (projectile == null || !projectile.isActive()) {
            return false;
        }

        // Simple circle collision (soul radius ~5, projectile assumed ~5)
        float dx = position.x - projectile.getX();
        float dy = position.y - projectile.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        return distance < 10; // Combined radius
    }

    public Vector2 getPosition() {
        return position.copy();
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
        this.x = (int) x;
        this.y = (int) y;
    }

    public void setBattleBox(int left, int top, int right, int bottom) {
        this.boxLeft = left;
        this.boxTop = top;
        this.boxRight = right;
        this.boxBottom = bottom;
    }

    public Vector2 getVelocity() {
        return velocity.copy();
    }
}
