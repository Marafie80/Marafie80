package com.undertale.ui;

import com.undertale.engine.Renderer;
import com.undertale.engine.InputEvent;

public abstract class UIElement {
    protected int x;
    protected int y;
    protected boolean visible;
    protected boolean active;

    public UIElement(int x, int y) {
        this.x = x;
        this.y = y;
        this.visible = true;
        this.active = true;
    }

    public abstract void render(Renderer renderer);

    public abstract void update(float dt);

    public void handleInput(InputEvent event) {
        // Default implementation - can be overridden
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
