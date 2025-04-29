package dev.forward.casino.util.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public interface ScreenResizeHandler extends UIHandler {
    void resize(Screen screen, MinecraftClient mc, int w, int h);
}
