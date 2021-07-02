package com.zhang.autotouch.bean;

public class TouchPoint {
    private String name;
    private int x;
    private int y;
    private int delay;
    private String text;

    public TouchPoint(String name, int x, int y, int delay) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.delay = delay;
    }

    public TouchPoint(String name, int x, int y, int delay, String text) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.delay = delay;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDelay() {
        return delay;
    }

    public String getText() {
        return text;
    }
}
