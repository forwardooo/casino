package dev.forward.casino.util.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

public interface ScreenDrawHandler extends UIHandler {
    void drawScreen(Screen screen, MatrixStack stack, int mouseX, int mouseY, float tickLength);
}
