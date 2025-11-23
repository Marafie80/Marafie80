package com.undertale.engine;

public class Sprite {
    private String id;
    private String path;
    private int width;
    private int height;
    private Object imageData; // Placeholder for actual image data

    public Sprite(String id, String path) {
        this.id = id;
        this.path = path;
        this.width = 0;
        this.height = 0;
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Object getImageData() {
        return imageData;
    }

    public void setImageData(Object imageData) {
        this.imageData = imageData;
    }
}
