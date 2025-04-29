// Decompiled with: CFR 0.152
// Class Version: 8
package dev.forward.casino.util.color;


public class Color
        extends AbstractColor<Color> {
    private final double r;
    private final double g;
    private final double b;
    private final double alpha;

    public Color(double r, double g, double b, double alpha) {
        this.r = Math.min(Math.max(r, 0.0), 1.0);
        this.g = Math.min(Math.max(g, 0.0), 1.0);
        this.b = Math.min(Math.max(b, 0.0), 1.0);
        this.alpha = Math.min(Math.max(alpha, 0.0), 1.0);
    }

    public Color(int r, int g, int b, int alpha) {
        this((double)r / 255.0, (double)g / 255.0, (double)b / 255.0, (double)alpha / 255.0);
    }

    public Color(int r, int g, int b, double alpha) {
        this((double)r / 255.0, (double)g / 255.0, (double)b / 255.0, alpha);
    }

    public Color(String hexadecimal) {
        if (hexadecimal.startsWith("#")) {
            hexadecimal = hexadecimal.substring(1);
        }
        char[] chars = hexadecimal.toCharArray();
        this.r = (float)Integer.parseInt(chars[0] + "" + chars[1], 16) / 255.0f;
        this.g = (float)Integer.parseInt(chars[2] + "" + chars[3], 16) / 255.0f;
        this.b = (float)Integer.parseInt(chars[4] + "" + chars[5], 16) / 255.0f;
        this.alpha = chars.length >= 8 ? (double)((float)Integer.parseInt(chars[6] + "" + chars[7], 16) / 255.0f) : 1.0;
    }
    public Color darker() {
        return new Color(this.getR() * 0.7, this.getB() * 0.7, this.getG() * 0.7, this.getAlpha());
    }
    public Color brighter() {
        return new Color(this.getR() * 1.5, this.getB() * 1.5, this.getG() * 1.5, this.getAlpha());
    }
    @Override
    public Color of(double r, double g, double b, double alpha) {
        return new Color(r, g, b, alpha);
    }

    @Override
    public Color of(int r, int g, int b, double alpha) {
        return new Color(r, g, b, alpha);
    }

    @Override
    public Color set(double r, double g, double b, double a) {
        return new Color(r, g, b, a);
    }

    public String toString() {
        return String.format("Color(%s, %s, %s, %s)", this.r, this.b, this.b, this.alpha);
    }

    @Override
    public double getR() {
        return this.r;
    }

    @Override
    public double getG() {
        return this.g;
    }

    @Override
    public double getB() {
        return this.b;
    }

    @Override
    public double getAlpha() {
        return this.alpha;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Color)) {
            return false;
        }
        Color other = (Color)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (Double.compare(this.getR(), other.getR()) != 0) {
            return false;
        }
        if (Double.compare(this.getG(), other.getG()) != 0) {
            return false;
        }
        if (Double.compare(this.getB(), other.getB()) != 0) {
            return false;
        }
        return Double.compare(this.getAlpha(), other.getAlpha()) == 0;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Color;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $r = Double.doubleToLongBits(this.getR());
        result = result * 59 + (int)($r >>> 32 ^ $r);
        long $g = Double.doubleToLongBits(this.getG());
        result = result * 59 + (int)($g >>> 32 ^ $g);
        long $b = Double.doubleToLongBits(this.getB());
        result = result * 59 + (int)($b >>> 32 ^ $b);
        long $alpha = Double.doubleToLongBits(this.getAlpha());
        result = result * 59 + (int)($alpha >>> 32 ^ $alpha);
        return result;
    }
}
