package dev.forward.casino.util.color;

import com.mojang.blaze3d.platform.GlStateManager;

public abstract class AbstractColor<T extends AbstractColor<T>> {
    public abstract T of(double var1, double var3, double var5, double var7);

    public abstract T of(int var1, int var2, int var3, double var4);

    public abstract double getR();

    public abstract double getG();

    public abstract double getB();

    public abstract double getAlpha();

    public T setAlpha(double alpha) {
        return this.set(this.getR(), this.getG(), this.getB(), alpha);
    }

    public abstract T set(double var1, double var3, double var5, double var7);

    public T set(AbstractColor<?> color) {
        return this.set(color.getR(), color.getG(), color.getB(), color.getAlpha());
    }

    public T alpha(double alpha) {
        return this.of(this.getR(), this.getG(), this.getB(), alpha);
    }

    public T multiplyAlpha(double alpha) {
        return this.set(this.getR(), this.getG(), this.getB(), this.getAlpha() * alpha);
    }

    public int getIntR() {
        return (int)(this.getR() * 255.0);
    }

    public int getIntG() {
        return (int)(this.getG() * 255.0);
    }

    public int getIntB() {
        return (int)(this.getB() * 255.0);
    }

    public int getIntAlpha() {
        return (int)(this.getAlpha() * 255.0);
    }

    public float[] getHSB() {
        float hue;
        int cmin;
        int r = this.getIntR();
        int g = this.getIntG();
        int b = this.getIntB();
        float[] hsbvals = new float[3];
        int cmax = Math.max(r, g);
        if (b > cmax) {
            cmax = b;
        }
        if (b < (cmin = Math.min(r, g))) {
            cmin = b;
        }
        float brightness = (float)cmax / 255.0f;
        float saturation = cmax != 0 ? (float)(cmax - cmin) / (float)cmax : 0.0f;
        if (saturation == 0.0f) {
            hue = 0.0f;
        } else {
            float redc = (float)(cmax - r) / (float)(cmax - cmin);
            float greenc = (float)(cmax - g) / (float)(cmax - cmin);
            float bluec = (float)(cmax - b) / (float)(cmax - cmin);
            hue = r == cmax ? bluec - greenc : (g == cmax ? 2.0f + redc - bluec : 4.0f + greenc - redc);
            if ((hue /= 6.0f) < 0.0f) {
                hue += 1.0f;
            }
        }
        hsbvals[0] = hue;
        hsbvals[1] = saturation;
        hsbvals[2] = brightness;
        return hsbvals;
    }

    public T progressTo(AbstractColor<?> color, double progress) {
        float[] hsb1 = this.getHSB();
        float[] hsb2 = color.getHSB();
        float hueDif1 = hsb2[0] > hsb1[0] ? -(1.0f - hsb2[0] + hsb1[0]) : 1.0f - hsb1[0] + hsb2[0];
        float hueDif2 = hsb2[0] - hsb1[0];
        float hue = (float)((double)hsb1[0] + (double)(Math.abs(hueDif2) < Math.abs(hueDif1) ? hueDif2 : hueDif1) * progress);
        if (hsb2[1] == 0.0f) {
            hue = hsb1[0];
        } else if (hsb1[1] == 0.0f) {
            hue = hsb2[0];
        }
        int[] rgb = ColorUtil.getRGBFromHSB(hue, (float)((double)hsb1[1] + (double)(hsb2[1] - hsb1[1]) * progress), (float)((double)hsb1[2] + (double)(hsb2[2] - hsb1[2]) * progress));
        double alpha = this.getAlpha();
        return this.of(rgb[0], rgb[1], rgb[2], alpha + (color.getAlpha() - alpha) * progress);
    }

    public T interpolateProgressColor(AbstractColor<?> color, double progress) {
        double red = this.getR() + (color.getR() - this.getR()) * progress;
        double green = this.getG() + (color.getG() - this.getG()) * progress;
        double blue = this.getB() + (color.getB() - this.getB()) * progress;
        double alpha = this.getAlpha() + (color.getAlpha() - this.getAlpha()) * progress;
        return this.of(red, green, blue, alpha);
    }

    public int getDecimal() {
        return (this.getIntR() << 16) + (this.getIntG() << 8) + this.getIntB() + (this.getIntAlpha() << 24);
    }

    public void apply() {
        this.apply(1.0);
    }

    public void apply(double alpha) {
        double totalAlpha = this.getAlpha() * alpha;
        GlStateManager.color4f((float)this.getR(), (float)this.getG(), (float)this.getB(), (float)totalAlpha);
        if (totalAlpha < 1.0) {
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(770, 771, 1, 771);
        } else {
            GlStateManager.disableBlend();
        }
    }

    public T clone() {
        return this.of(this.getR(), this.getG(), this.getB(), this.getAlpha());
    }
}
