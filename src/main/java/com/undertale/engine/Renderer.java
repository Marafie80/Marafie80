package com.undertale.engine;

public class Renderer {
    private int screenWidth;
    private int screenHeight;

    public Renderer(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void drawImage(Sprite sprite, int x, int y) {
        // Placeholder for rendering sprite at position
        if (sprite != null) {
            // Render logic would go here
        }
    }

    public void drawText(String text, int x, int y) {
        // Placeholder for rendering text at position
        if (text != null && !text.isEmpty()) {
            // Render logic would go here
        }
    }

    public void clear() {
        // Placeholder for clearing the screen
    }

    public void drawRect(int x, int y, int width, int height) {
        // Placeholder for drawing rectangles (useful for UI)
    }

    public void drawFilledRect(int x, int y, int width, int height) {
        // Placeholder for drawing filled rectangles
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
}
