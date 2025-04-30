package dev.forward.casino.element;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.engine.elements.AbstractCarvedRectangle;
import dev.forward.casino.engine.elements.CarvedRectangle;
import dev.forward.casino.util.color.Palette;
import dev.forward.casino.util.math.MathUtil;
import dev.forward.casino.util.math.V3;
import dev.forward.casino.util.render.GLUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class Scroller extends AbstractCarvedRectangle<Scroller> {
    private final int minValue;
    private final int maxValue;
    private final int increment;
    private int currentValue;
    private float anim;
    private final CarvedRectangle scroller;
    public Scroller(int min, int max, int increment, int currentValue) {
        this.minValue = min ;
        this.maxValue = max;
        this.increment = increment;
        this.currentValue = currentValue;
        this.setInteractive(true);
        scroller = new CarvedRectangle().setSize(new V3(4,14)).setColor(Palette.WHITE).setOutlineColor(Palette.WHITE_86).setCarveSize(1).setOutline(2);
    }
    public float getValue() {
        return currentValue;
    }
    @Override
    public void render(MatrixStack stack, double tickDelta, double mouseX, double mouseY) {
        double sliderX = GLUtils.getRenderPos().getX();
        double sliderWidth = getSize().getX();

        if (isLeftPressed()) {
            float relative = (float) ((((mouseX - 4)- sliderX) * 2) / (sliderWidth - 20));
            relative = MathHelper.clamp(relative, 0.0f, 1.0f);
            float value = minValue + relative * (maxValue - minValue);
            currentValue = (int) MathUtil.round(value, increment);
        }

        float targetSliderWidth = ((float) (currentValue - minValue) / (maxValue - minValue)) * (float) (sliderWidth - 20);
        anim = anim == 0 ? targetSliderWidth : MathUtil.fast(anim, targetSliderWidth, 20);
        super.render(stack, tickDelta, mouseX, mouseY);
        GlStateManager.translatef(anim - 2 + 10, -3, 0);
        scroller.render(stack, tickDelta, mouseX, mouseY);
        GlStateManager.translatef(-anim + 2 - 10, 3, 0);
    }


}
