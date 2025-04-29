package dev.forward.casino.engine.elements;


import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.engine.animation.Animation;
import dev.forward.casino.engine.contexts.Overlay;
import dev.forward.casino.util.color.Color;
import dev.forward.casino.util.color.ColorUtil;
import dev.forward.casino.util.color.Palette;
import dev.forward.casino.util.math.V3;
import dev.forward.casino.util.render.CarvedRectangleRender;
import dev.forward.casino.util.render.GLUtils;
import lombok.Getter;
import net.minecraft.client.util.math.MatrixStack;

public abstract class AbstractCarvedRectangle<T extends AbstractCarvedRectangle<T>>
        extends AbstractElement<T> {
    @Getter
    protected double carveSize = 4.0;
    protected Boolean[] carveCorners = new Boolean[]{true, true, true, true};
    protected Boolean[] carveOutlines = new Boolean[]{true, true, true, true};
    @Getter
    protected double outline = 2.0;
    @Getter
    protected Color outlineColor = null;
    @Getter
    protected Color outlineGradientColor = null;
    @Getter
    protected double outlineGradientHeight = 1.0;
    @Getter
    protected float shadowSize = 0.0f;
    @Getter
    protected Color shadowColor = Palette.BLACK.alpha(0.2);
    protected V3 shadowOffset = new V3();
    @Getter
    protected float blur = 0.0f;
    protected float rippleRadius;
    protected Color rippleColor;
    protected V3 ripplePos = new V3();

    public AbstractCarvedRectangle() {
        this(4.0);
    }

    public AbstractCarvedRectangle(double carveSize) {
        this(carveSize, 2.0);
    }

    public AbstractCarvedRectangle(double carveSize, double outline) {
        this.carveSize = carveSize;
        this.outline = outline;
    }

    public T setCarveSize(double carveSize) {
        this.carveSize = carveSize;
        return this.genericThis;
    }

    public T setOutline(double outline) {
        if (this.is3dRender()) {
            outline /= 2.5;
        }
        this.outline = outline;
        return this.genericThis;
    }

    public T setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
        return this.genericThis;
    }

    public T setOutlineGradientColor(Color outlineGradientColor) {
        this.outlineGradientColor = outlineGradientColor;
        return this.genericThis;
    }

    public T setOutlineGradientHeight(double outlineGradientHeight) {
        this.outlineGradientHeight = outlineGradientHeight;
        return this.genericThis;
    }

    public T setCarveCorners(Boolean ... carveCorners) {
        this.carveCorners = carveCorners;
        return this.genericThis;
    }

    public T setCarveOutlines(Boolean ... outLines) {
        this.carveOutlines = outLines;
        return this.genericThis;
    }

    public void smoothChangeColor(Color changeColor, double seconds) {
        Animation.stop(this, "change_color");
        Color startColor = this.getColor();
        if (startColor.getDecimal() == changeColor.getDecimal()) {
            return;
        }
        boolean hasOutlineColor = this.getOutlineColor() != null;
        Color outlineColor = hasOutlineColor ? null : Palette.getBrighterColor(startColor);
        Color changeOutlineColor = hasOutlineColor ? null : Palette.getBrighterColor(changeColor);
        Animation.play(this.genericThis, "change_color", seconds, (element, progress) -> {
            this.color = startColor.progressTo(changeColor, progress);
            if (changeOutlineColor != null) {
                this.setOutlineColor(outlineColor.progressTo(changeOutlineColor, progress));
            }
            this.markDirty();
        }).onComplete(element -> {
            this.color = changeColor;
            this.markDirty();
        }).onStop(element -> {
            if (!hasOutlineColor) {
                this.setOutlineColor(null);
            }
        });
    }

    public T setShadowSize(float shadowSize) {
        this.shadowSize = shadowSize;
        return this.genericThis;
    }

    public T setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
        return this.genericThis;
    }

    public T setBlur(float blur) {
        this.blur = blur;
        return this.genericThis;
    }

    @Override
    public T setIs3dRender(boolean is3dRender) {
        if (this.is3dRender() != is3dRender) {
            this.is3dRender = is3dRender;
            this.setOutline(this.getOutline() / 2.5);
        }
        for (AbstractElement<?> child : this.childs) {
            child.setIs3dRender(is3dRender);
        }
        return this.genericThis;
    }
//    public void render(double partialTicks, double mouseX, double mouseY) {
//        if (this.color.getAlpha() != 0.0 || this.outlineColor != null && this.outlineColor.getAlpha() != 0.0) {
//            double absoluteAlpha = this.getAbsoluteAlpha();
//            Color absoluteColor = this.color.clone().setAlpha(this.color.getAlpha() * absoluteAlpha);
//            if (this.outline == 0.0) {
//                SimpleDrawer.drawCarvedRect(0.0, 0.0, this.size.getX(), this.size.getY(), this.carveSize, this.is3dRender, this.carveCorners.clone(), absoluteColor);
//            }
//           /* else {
//                Color absoluteOutlineColor = this.outlineColor == null ? Palette.getBrighterColor(this.color) : this.outlineColor.clone();
//                absoluteOutlineColor = absoluteOutlineColor.setAlpha(absoluteOutlineColor.getAlpha() * absoluteAlpha);
//                SimpleDrawer.drawOutlinedCarvedRect(new V3(), this.size.clone(), this.carveSize, this.is3dRender, this.carveCorners.clone(), absoluteColor, absoluteOutlineColor, this.outline, this.is3dRender);
//            }*/
//
//        }
//    }
    @Override
    public void render(MatrixStack stack, double partialTicks, double mouseX, double mouseY) {
        if (this.color.getAlpha() == 0.0 && (this.outlineColor == null || this.outlineColor.getAlpha() == 0.0) && this.blur <= 0.0f) {
            return;
        }
        if (this.is3dRender) {
            GlStateManager.disableCull();
        }
        if (this.blur > 0.0f) {
            this.renderBlur();
        }
        GlStateManager.disableTexture();
        GlStateManager.blendFuncSeparate(770, 771, 1, 771);
        GlStateManager.enableBlend();
        GlStateManager.alphaFunc(517, 0.0f);
        double absoluteAlpha = this.getAbsoluteAlpha();
        if (this.shadowSize > 0.0f) {
            CarvedRectangleRender.drawShadow(this.size, this.shadowOffset, this.shadowSize, this.carveSize, (Color)this.shadowColor.multiplyAlpha(absoluteAlpha));
        }
        Color absoluteColor = this.color.setAlpha(this.color.getAlpha() * absoluteAlpha);
        Color absoluteOutlineColor = null;
        Color absoluteGradientOutlineColor = null;
        if (this.outline > 0.0) {
            absoluteOutlineColor = this.outlineColor == null ? Palette.getBrighterColor(this.color) : this.outlineColor;
            absoluteOutlineColor = absoluteOutlineColor.setAlpha(absoluteOutlineColor.getAlpha() * absoluteAlpha);
            Color color = absoluteGradientOutlineColor = this.outlineGradientColor == null ? null : this.outlineGradientColor;
            if (absoluteGradientOutlineColor != null) {
                absoluteGradientOutlineColor = absoluteGradientOutlineColor.setAlpha(absoluteGradientOutlineColor.getAlpha() * absoluteAlpha);
            }
            if (absoluteOutlineColor.getAlpha() < 1.0 && absoluteColor.getAlpha() > 0.0) {
                absoluteOutlineColor = ColorUtil.blending(absoluteColor, absoluteOutlineColor);
            }
        }
        Color absoluteRippleColor = null;
        if (this.rippleRadius > 0.0f && this.rippleColor != null && (absoluteRippleColor = this.rippleColor.setAlpha(this.rippleColor.getAlpha() * absoluteAlpha)).getAlpha() < 1.0 && absoluteColor.getAlpha() > 0.0) {
            absoluteRippleColor = ColorUtil.blending(absoluteColor, absoluteRippleColor);
        }
        CarvedRectangleRender.draw(this.size, absoluteColor, this.carveSize, this.carveCorners, this.outline, absoluteOutlineColor, this.rippleRadius, absoluteRippleColor, this.ripplePos, this.carveOutlines, absoluteGradientOutlineColor, this.outlineGradientHeight);
        GlStateManager.enableTexture();
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
    }

    private void renderBlur() {
        V3 screenSize = Overlay.get().getSize();
        V3 renderPos = GLUtils.getRenderPos();
        V3 minUV = renderPos.divide(screenSize.divide(2.0));
        V3 maxUV = minUV.add(this.getSize().multiply(this.getScale()).divide(screenSize));
        minUV = minUV.setY(1.0 - minUV.getY());
        maxUV = maxUV.setY(1.0 - maxUV.getY());
        Engine.getBlurRender().draw(this.size, minUV, maxUV, this.blur, this.carveSize, this.carveCorners);
    }

    @Override
    public T copy(T element) {
        super.copy(element);
        ((AbstractCarvedRectangle<?>)element).setCarveSize(this.getCarveSize()).setOutline(this.getOutline()).setOutlineColor(this.getOutlineColor()).setOutlineGradientColor(this.getOutlineGradientColor()).setCarveCorners(this.carveCorners.clone()).setCarveOutlines(this.carveOutlines.clone()).setOutlineGradientHeight(this.getOutlineGradientHeight()).setShadowSize(this.getShadowSize()).setShadowColor(this.getShadowColor()).setBlur(this.getBlur());
        return element;
    }

}
