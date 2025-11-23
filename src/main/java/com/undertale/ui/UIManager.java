package com.undertale.ui;

import com.undertale.engine.Renderer;
import com.undertale.engine.InputEvent;

import java.util.ArrayList;
import java.util.List;

public class UIManager {
    private List<UIElement> elements;

    public UIManager() {
        this.elements = new ArrayList<>();
    }

    public void render(Renderer renderer) {
        for (UIElement element : elements) {
            if (element.isVisible()) {
                element.render(renderer);
            }
        }
    }

    public void update(float dt) {
        for (UIElement element : elements) {
            if (element.isActive()) {
                element.update(dt);
            }
        }
    }

    public void handleInput(InputEvent event) {
        for (UIElement element : elements) {
            if (element.isActive()) {
                element.handleInput(event);
            }
        }
    }

    public void addElement(UIElement element) {
        if (element != null && !elements.contains(element)) {
            elements.add(element);
        }
    }

    public void removeElement(UIElement element) {
        elements.remove(element);
    }

    public void clearElements() {
        elements.clear();
    }

    public void openMenu(String menuName) {
        // This would open a specific menu by name
        // Implementation depends on how menus are registered and managed
    }

    public <T extends UIElement> T getElement(Class<T> type) {
        for (UIElement element : elements) {
            if (type.isInstance(element)) {
                return type.cast(element);
            }
        }
        return null;
    }

    public List<UIElement> getElements() {
        return new ArrayList<>(elements);
    }
}
