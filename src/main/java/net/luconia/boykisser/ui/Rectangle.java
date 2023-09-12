package net.luconia.boykisser.ui;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Rectangle {
    private RectData x;
    private RectData y;

    public Rectangle(float x, float y) {
        this.x = new RectData(x);
        this.y = new RectData(y);
    }

    public float getX() {
        return x.get();
    }

    public float getY() {
        return y.get();
    }

    @AllArgsConstructor
    public static class RectData {
        private float val;

        public float get() {
            return val;
        }

        public void set(float v) {
            val = v;
        }

        public void add(float v) {
            val += v;
        }

        public void sub(float v) {
            val -= v;
        }

        public void multiply(float v) {
            val *= v;
        }

        public void divide(float v) {
            val /= v;
        }
    }
}