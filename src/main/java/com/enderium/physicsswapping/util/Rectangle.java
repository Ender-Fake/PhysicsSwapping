package com.enderium.physicsswapping.util;

public record Rectangle(int x, int y, int width, int height) {


    public int right() {
        return x + width;
    }

    public int bottom() {
        return y + height;
    }

    public int centerX() {
        return x + (width >> 1);
    }

    public int centerY() {
        return y + (height >> 1);
    }

    public boolean isOver(final double x, final double y) {
        return x >= this.x && y >= this.y && x < this.right() && y < this.bottom();
    }

    public static Rectangle of(int width, int height) {
        return new Rectangle(0, 0, width, height);
    }

    public static Rectangle of(int x, int y, int width, int height) {
        return new Rectangle(x, y, width, height);
    }

}
