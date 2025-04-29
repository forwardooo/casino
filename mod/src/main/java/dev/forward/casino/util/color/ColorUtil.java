package dev.forward.casino.util.color;

import java.util.regex.Pattern;

public class ColorUtil {
    private static final Pattern REGEX = Pattern.compile("§[0-9a-fk-or]|¨[0-9a-fA-F]{6}");

    public static int[] getRGBFromHSB(float hue, float saturation, float brightness) {
        int r = 0;
        int g = 0;
        int b = 0;
        if (saturation == 0.0f) {
            g = b = (int)(brightness * 255.0f + 0.5f);
            r = b;
        } else {
            float h = (hue - (float)Math.floor(hue)) * 6.0f;
            float f = h - (float)Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - saturation * (1.0f - f));
            switch ((int)h) {
                case 0: {
                    r = (int)(brightness * 255.0f + 0.5f);
                    g = (int)(t * 255.0f + 0.5f);
                    b = (int)(p * 255.0f + 0.5f);
                    break;
                }
                case 1: {
                    r = (int)(q * 255.0f + 0.5f);
                    g = (int)(brightness * 255.0f + 0.5f);
                    b = (int)(p * 255.0f + 0.5f);
                    break;
                }
                case 2: {
                    r = (int)(p * 255.0f + 0.5f);
                    g = (int)(brightness * 255.0f + 0.5f);
                    b = (int)(t * 255.0f + 0.5f);
                    break;
                }
                case 3: {
                    r = (int)(p * 255.0f + 0.5f);
                    g = (int)(q * 255.0f + 0.5f);
                    b = (int)(brightness * 255.0f + 0.5f);
                    break;
                }
                case 4: {
                    r = (int)(t * 255.0f + 0.5f);
                    g = (int)(p * 255.0f + 0.5f);
                    b = (int)(brightness * 255.0f + 0.5f);
                    break;
                }
                case 5: {
                    r = (int)(brightness * 255.0f + 0.5f);
                    g = (int)(p * 255.0f + 0.5f);
                    b = (int)(q * 255.0f + 0.5f);
                }
            }
        }
        return new int[]{r, g, b};
    }
    public static float[] rgba(final int color) {
        return new float[] {
                (color >> 16 & 0xFF) / 255f,
                (color >> 8 & 0xFF) / 255f,
                (color & 0xFF) / 255f,
                (color >> 24 & 0xFF) / 255f
        };
    }
    public static Color blending(Color color1, Color color2) {
        double alpha2;
        double alpha1 = color1.getAlpha();
        double combinedAlpha = alpha1 + (alpha2 = color2.getAlpha()) * (1.0 - alpha1);
        if (combinedAlpha == 0.0) {
            return new Color(0, 0, 0, 0);
        }
        return new Color((color1.getR() * alpha1 * (1.0 - alpha2) + color2.getR() * alpha2) / combinedAlpha, (color1.getG() * alpha1 * (1.0 - alpha2) + color2.getG() * alpha2) / combinedAlpha, (color1.getB() * alpha1 * (1.0 - alpha2) + color2.getB() * alpha2) / combinedAlpha, combinedAlpha);
    }
}