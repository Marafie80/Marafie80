package com.undertale.engine;

public class InputEvent {
    private KeyCode keyCode;
    private EventType type;

    public enum EventType {
        KEY_PRESSED,
        KEY_RELEASED,
        KEY_TYPED
    }

    public InputEvent(KeyCode keyCode, EventType type) {
        this.keyCode = keyCode;
        this.type = type;
    }

    public KeyCode getKeyCode() {
        return keyCode;
    }

    public EventType getType() {
        return type;
    }
}
