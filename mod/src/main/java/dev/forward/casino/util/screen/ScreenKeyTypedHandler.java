package dev.forward.casino.util.screen;

import net.minecraft.client.gui.screen.Screen;

import java.io.IOException;

public interface ScreenKeyTypedHandler extends UIHandler {
    void keyTyped(Screen screen, int code) throws IOException;
}
