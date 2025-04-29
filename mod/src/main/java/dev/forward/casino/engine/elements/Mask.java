package dev.forward.casino.engine.elements;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.util.color.Palette;
import dev.forward.casino.util.interactive.InteractionManager;
import dev.forward.casino.util.render.CarvedRectangleRender;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

public class Mask
        extends AbstractCarvedRectangle<Mask> {
    protected boolean outMode;

    public Mask() {
        this(4.0);
    }

    public Mask(double carveSize) {
        this(false, carveSize);
    }

    public Mask(boolean outMode) {
        this(outMode, 4.0);
    }

    public Mask(boolean outMode, double carveSize) {
        super(carveSize);
        this.outMode = outMode;
        this.setColor(Palette.WHITE.alpha(0.0));
    }

    public Mask setOutMode(boolean outMode) {
        this.outMode = outMode;
        return this;
    }

    @Override
    public void render(MatrixStack stack, double partialTicks, double mouseX, double mouseY) {
        super.render(stack, partialTicks, mouseX, mouseY);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GlStateManager.stencilMask(255);
        GlStateManager.clearStencil(0);
        GlStateManager.clear(1024, false);
        GlStateManager.stencilFunc(519, 1, -1);
        GlStateManager.stencilOp(7681, 7681, 7681);
        GlStateManager.colorMask(false, false, false, false);
        CarvedRectangleRender.draw(this.size, Palette.WHITE, this.carveSize, this.carveCorners, 0.0, null, this.carveOutlines, null, 1.0);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.stencilFunc(this.outMode ? 517 : 514, 1, -1);
        GlStateManager.stencilOp(7680, 7680, 7680);
        GlStateManager.stencilMask(0);
    }

    @Override
    public void postRender(MatrixStack stack,double partialTicks, double mouseX, double mouseY, boolean isInteractive, InteractionManager interactionManager) {
        super.postRender(stack,partialTicks, mouseX, mouseY, isInteractive, interactionManager);
        GL11.glDisable(GL11.GL_STENCIL_TEST);
    }

    @Override
    public Mask copy(Mask element) {
        super.copy(element);
        element.setOutMode(this.isOutMode());
        return element;
    }

    @Override
    public Mask clone() {
        return this.copy(new Mask());
    }

    public boolean isOutMode() {
        return this.outMode;
    }
}