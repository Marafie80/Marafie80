package com.undertale.engine;

public class Sound {
    private String id;
    private String path;
    private Object audioData; // Placeholder for actual audio data

    public Sound(String id, String path) {
        this.id = id;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public Object getAudioData() {
        return audioData;
    }

    public void setAudioData(Object audioData) {
        this.audioData = audioData;
    }

    public void play() {
        // Placeholder for audio playback
    }

    public void stop() {
        // Placeholder for stopping audio
    }
}
