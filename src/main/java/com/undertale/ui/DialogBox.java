package com.undertale.ui;

import com.undertale.engine.Renderer;
import com.undertale.engine.InputEvent;
import com.undertale.engine.KeyCode;

import java.util.LinkedList;
import java.util.Queue;

public class DialogBox extends UIElement {
    private Queue<String> lines;
    private String currentLine;
    private int characterIndex;
    private float textSpeed;
    private float timer;
    private boolean finished;
    private int width;
    private int height;

    public DialogBox(int x, int y, int width, int height) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.lines = new LinkedList<>();
        this.currentLine = "";
        this.characterIndex = 0;
        this.textSpeed = 0.05f; // Time between characters
        this.timer = 0;
        this.finished = false;
    }

    @Override
    public void render(Renderer renderer) {
        if (!visible) {
            return;
        }

        // Draw dialog box background
        renderer.drawFilledRect(x, y, width, height);

        // Draw current text
        if (currentLine != null && !currentLine.isEmpty()) {
            String displayText = currentLine.substring(0, characterIndex);
            renderer.drawText(displayText, x + 10, y + 10);
        }
    }

    @Override
    public void update(float dt) {
        if (!active || currentLine == null || currentLine.isEmpty()) {
            return;
        }

        // Animate text appearance
        if (characterIndex < currentLine.length()) {
            timer += dt;
            if (timer >= textSpeed) {
                characterIndex++;
                timer = 0;
            }
        } else {
            finished = true;
        }
    }

    @Override
    public void handleInput(InputEvent event) {
        if (event.getType() == InputEvent.EventType.KEY_PRESSED) {
            if (event.getKeyCode() == KeyCode.Z || event.getKeyCode() == KeyCode.ENTER) {
                next();
            }
        }
    }

    public void showText(String text) {
        lines.add(text);
        if (currentLine == null || currentLine.isEmpty()) {
            loadNextLine();
        }
    }

    public void next() {
        if (!finished) {
            // Skip to end of current line
            characterIndex = currentLine.length();
            finished = true;
        } else {
            // Move to next line
            loadNextLine();
        }
    }

    private void loadNextLine() {
        if (!lines.isEmpty()) {
            currentLine = lines.poll();
            characterIndex = 0;
            finished = false;
            timer = 0;
        } else {
            currentLine = "";
            finished = true;
        }
    }

    public boolean isFinished() {
        return finished && lines.isEmpty();
    }

    public void clear() {
        lines.clear();
        currentLine = "";
        characterIndex = 0;
        finished = false;
    }
}
