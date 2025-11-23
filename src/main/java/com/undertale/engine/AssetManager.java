package com.undertale.engine;

import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    private Map<String, Sprite> sprites;
    private Map<String, Sound> sounds;

    public AssetManager() {
        this.sprites = new HashMap<>();
        this.sounds = new HashMap<>();
    }

    public void loadSprite(String id, String path) {
        Sprite sprite = new Sprite(id, path);
        // In a real implementation, this would load the actual image file
        sprites.put(id, sprite);
    }

    public void loadSound(String id, String path) {
        Sound sound = new Sound(id, path);
        // In a real implementation, this would load the actual audio file
        sounds.put(id, sound);
    }

    public Sprite getSprite(String id) {
        return sprites.get(id);
    }

    public Sound getSound(String id) {
        return sounds.get(id);
    }

    public boolean hasSprite(String id) {
        return sprites.containsKey(id);
    }

    public boolean hasSound(String id) {
        return sounds.containsKey(id);
    }

    public void unloadSprite(String id) {
        sprites.remove(id);
    }

    public void unloadSound(String id) {
        sounds.remove(id);
    }

    public void clear() {
        sprites.clear();
        sounds.clear();
    }
}
