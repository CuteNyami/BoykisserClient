package net.luconia.boykisser;

import java.awt.*;

public class MathUtil {
    public static int lerp(int a, int b, float f) {
        return (int) (a + (b - a) * f);
    }

    public static Color lerp(Color a, Color b, float f) {
        return new Color(lerp(a.getRed(), b.getRed(), f), lerp(a.getGreen(), b.getGreen(), f), lerp(a.getBlue(), b.getBlue(), f), lerp(a.getAlpha(), b.getAlpha(), f));
    }
}
