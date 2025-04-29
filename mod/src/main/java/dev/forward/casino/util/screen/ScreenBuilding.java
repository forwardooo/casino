package dev.forward.casino.util.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.io.IOException;

public class ScreenBuilding extends Screen {
    public ScreenBuilding(String title) {
        super(new LiteralText(title));
    }

    static final UIHandler DEFAULT = new UIHandler() {
    };
    private ScreenCloseHandler closeHandler;
    private ScreenDrawHandler drawHandler;
    private ScreenInitHandler initHandler;
    private ScreenKeyTypedHandler keyTypedHandler;
    private ScreenResizeHandler resizeHandler;
    private ScreenUpdateHandler updateHandler;
    private ScreenCharTypedHandler charTypedHandler;

    public ScreenBuilding close(ScreenCloseHandler var1) {
        this.closeHandler = var1;
        return this;
    }

    public ScreenBuilding draw(ScreenDrawHandler var1) {
        this.drawHandler = var1;
        return this;
    }

    public ScreenBuilding init(ScreenInitHandler var1) {
        this.initHandler = var1;
        return this;
    }

    public ScreenBuilding keyTyped(ScreenKeyTypedHandler var1) {
        this.keyTypedHandler = var1;
        return this;
    }
    public ScreenBuilding charTyped(ScreenCharTypedHandler var1) {
        this.charTypedHandler = var1;
        return this;
    }
    public ScreenBuilding resize(ScreenResizeHandler var1) {
        this.resizeHandler = var1;
        return this;
    }

    public ScreenBuilding update(ScreenUpdateHandler var1) {
        this.updateHandler = var1;
        return this;
    }
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        try {
            keyTypedHandler.keyTyped(this,  keyCode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    public void resize(MinecraftClient client, int width, int height) {
        this.resizeHandler.resize(this, client, width, height);
        super.resize(client, width, height);
    }
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.drawHandler.drawScreen(this,matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }
    public void removed() {
        this.closeHandler.close(this);
        super.removed();
    }
    public void tick() {
        this.updateHandler.update(this);
        super.tick();
    }
    public void init() {
        this.initHandler.init(this);
        super.init();
    }
}
