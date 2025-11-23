package com.undertale.engine;

public interface GameState {
    void enter();
    void update(float dt);
    void render(Renderer r);
    void exit();
    void handleInput(InputEvent e);
}
