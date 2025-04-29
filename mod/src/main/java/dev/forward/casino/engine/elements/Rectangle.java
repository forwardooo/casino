package dev.forward.casino.engine.elements;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.util.render.SimpleDrawer;
import net.minecraft.client.util.math.MatrixStack;

public class Rectangle
        extends AbstractElement<Rectangle> {
    @Override
    public void render(MatrixStack stack, double partialTicks, double mouseX, double mouseY) {
        this.applyColor();
        SimpleDrawer.drawRect(0.0, 0.0, this.size.getX(), this.size.getY());
        GlStateManager.disableBlend();
    }

    @Override
    public Rectangle copy(Rectangle element) {
        super.copy(element);
        return element;
    }

    @Override
    public Rectangle clone() {
        return this.copy(new Rectangle());
    }
}
