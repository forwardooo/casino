package dev.forward.casino.slots;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.engine.elements.AbstractElement;
import dev.forward.casino.engine.elements.CarvedRectangle;
import dev.forward.casino.util.math.V3;
import dev.forward.casino.util.render.GLUtils;

public class AnimatedInfoBackground extends CarvedRectangle {
    @Override
    public void transform() {
        boolean needRound;
        boolean isWorldRender;
        if (!this.isDirty) {
            GLUtils.loadModelViewMatrix(this.bakedMatrix);
            AbstractElement<?> upperParent = this.getUpperParent();
            if (upperParent == null || !upperParent.isWorldContext()) {
            }
            return;
        }
        double translateX = 0.0;
        double translateY = 0.0;
        double translateZ = 0.0;
        if (this.lastParent != null) {
            V3 parentSize = this.lastParent.getSize();
            translateX += parentSize.getX() * this.align.getX();
            translateY += parentSize.getY() * this.align.getY();
            translateZ += parentSize.getZ() * this.align.getZ();
        }
        AbstractElement<?> upperParent = this.getUpperParent();
        isWorldRender = this.lastParent != null && this.lastParent.isWorldContext();
        if (isWorldRender) {
            translateX += (this.pos.getX() + this.offset.getX()) * -16.0;
            translateY += (this.pos.getY() + this.offset.getY()) * -16.0;
            translateZ += (this.pos.getZ() + this.offset.getZ()) * 16.0;
        } else {
            translateX += this.pos.getX() + this.offset.getX();
            translateY += this.pos.getY() + this.offset.getY();
            translateZ += this.pos.getZ() + this.offset.getZ();
            if (upperParent == null || !upperParent.isWorldContext()) {
            }
        }
        needRound = upperParent != null && upperParent.isWorldContext();
        if (needRound) {
            GlStateManager.translated(translateX, translateY, translateZ);
        } else {
            GlStateManager.translatef(Math.round(translateX), Math.round(translateY), Math.round(translateZ));
        }
        GlStateManager.translated(0, -this.size.getY(), 0);
        GlStateManager.scaled(this.scale.getX(), this.scale.getY(), this.scale.getZ());
        GlStateManager.translated(0, this.size.getY(), 0);
        GlStateManager.rotatef((float)this.rotation.getY(), 0.0f, 1.0f, 0.0f);
        GlStateManager.rotatef((float)this.rotation.getX(), 1.0f, 0.0f, 0.0f);
        GlStateManager.rotatef((float)this.rotation.getZ(), 0.0f, 0.0f, 1.0f);
        if (needRound) {
            GlStateManager.translated(-this.size.getX() * this.origin.getX(), -this.size.getY() * this.origin.getY(), -this.size.getZ() * this.origin.getZ());
        } else {
            GlStateManager.translatef(Math.round(-this.size.getX() * this.origin.getX()), Math.round(-this.size.getY() * this.origin.getY()), Math.round(-this.size.getZ() * this.origin.getZ()));
        }
        GLUtils.getModelViewMatrix().get(this.bakedMatrix);
        this.isDirty = isWorldRender || this.is3dRender();
    }
}
