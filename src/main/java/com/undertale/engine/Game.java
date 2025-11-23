package com.undertale.engine;

public class Game {
    private boolean running;
    private GameState currentState;
    private Renderer renderer;
    private InputManager inputManager;
    private AssetManager assetManager;

    private static final int TARGET_FPS = 60;
    private static final float TARGET_DELTA = 1.0f / TARGET_FPS;

    public Game(int screenWidth, int screenHeight) {
        this.running = false;
        this.renderer = new Renderer(screenWidth, screenHeight);
        this.inputManager = new InputManager();
        this.assetManager = new AssetManager();
    }

    public void init() {
        // Initialize game systems
        running = true;

        // Load initial assets
        loadAssets();

        // Set initial state if needed
        if (currentState != null) {
            currentState.enter();
        }
    }

    private void loadAssets() {
        // Placeholder for loading initial assets
        // In a real implementation, this would load sprites and sounds
    }

    public void gameLoop() {
        long lastTime = System.nanoTime();
        float deltaAccumulator = 0.0f;

        while (running) {
            long currentTime = System.nanoTime();
            float delta = (currentTime - lastTime) / 1_000_000_000.0f;
            lastTime = currentTime;

            deltaAccumulator += delta;

            // Fixed timestep update
            while (deltaAccumulator >= TARGET_DELTA) {
                update(TARGET_DELTA);
                deltaAccumulator -= TARGET_DELTA;
            }

            render();

            // Simple frame rate limiting
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        shutdown();
    }

    private void update(float dt) {
        // Poll input events
        for (InputEvent event : inputManager.pollEvents()) {
            if (currentState != null) {
                currentState.handleInput(event);
            }
        }

        // Update current state
        if (currentState != null) {
            currentState.update(dt);
        }
    }

    private void render() {
        renderer.clear();

        if (currentState != null) {
            currentState.render(renderer);
        }

        // In a real implementation, this would present the rendered frame
    }

    public void changeState(GameState newState) {
        if (currentState != null) {
            currentState.exit();
        }

        currentState = newState;

        if (currentState != null) {
            currentState.enter();
        }
    }

    public void stop() {
        running = false;
    }

    private void shutdown() {
        if (currentState != null) {
            currentState.exit();
        }
        assetManager.clear();
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public boolean isRunning() {
        return running;
    }
}
