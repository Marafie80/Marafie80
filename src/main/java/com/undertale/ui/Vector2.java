package com.undertale.ui;

public class Vector2 {
    public float x;
    public float y;

    public Vector2() {
        this(0, 0);
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vector2 other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void scale(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public void normalize() {
        float len = length();
        if (len != 0) {
            x /= len;
            y /= len;
        }
    }

    public Vector2 copy() {
        return new Vector2(x, y);
    }
}
