package com.undertale.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class InputManager {
    private Map<KeyCode, Boolean> keyStates;
    private List<InputEvent> eventQueue;

    public InputManager() {
        this.keyStates = new HashMap<>();
        this.eventQueue = new ArrayList<>();

        // Initialize all keys as not pressed
        for (KeyCode key : KeyCode.values()) {
            keyStates.put(key, false);
        }
    }

    public boolean isKeyDown(KeyCode keyCode) {
        return keyStates.getOrDefault(keyCode, false);
    }

    public float getAxis(String axisName) {
        // Returns -1, 0, or 1 for axis input
        switch (axisName.toLowerCase()) {
            case "horizontal":
                if (isKeyDown(KeyCode.LEFT)) return -1.0f;
                if (isKeyDown(KeyCode.RIGHT)) return 1.0f;
                return 0.0f;
            case "vertical":
                if (isKeyDown(KeyCode.UP)) return -1.0f;
                if (isKeyDown(KeyCode.DOWN)) return 1.0f;
                return 0.0f;
            default:
                return 0.0f;
        }
    }

    public List<InputEvent> pollEvents() {
        List<InputEvent> events = new ArrayList<>(eventQueue);
        eventQueue.clear();
        return events;
    }

    public void keyPressed(KeyCode keyCode) {
        keyStates.put(keyCode, true);
        eventQueue.add(new InputEvent(keyCode, InputEvent.EventType.KEY_PRESSED));
    }

    public void keyReleased(KeyCode keyCode) {
        keyStates.put(keyCode, false);
        eventQueue.add(new InputEvent(keyCode, InputEvent.EventType.KEY_RELEASED));
    }
}
