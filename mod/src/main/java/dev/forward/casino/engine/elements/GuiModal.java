package dev.forward.casino.engine.elements;

import dev.forward.casino.engine.animation.Animation;
import dev.forward.casino.engine.animation.AnimationClip;
import net.minecraft.client.util.math.MatrixStack;

public class GuiModal
        extends AbstractElement<GuiModal> {
    private AbstractGuiScreen screen;
    private boolean closeOnEsc = false;
    private boolean isActive;
    private AnimationClip<GuiModal> openAnimation;
    private AnimationClip<GuiModal> closeAnimation;

    public GuiModal() {
        this.setEnabled(false);
        this.setInteractive(true);
    }

    public void open() {
        if (Animation.hasAnimation(this, "open_close")) {
            return;
        }
        this.screen.updateModalResize(this);
        this.setEnabled(true);
        this.setAlpha(1.0);
        this.isActive = true;
        if (this.openAnimation != null) {
            Animation.play(this.openAnimation);
        }
    }

    public void close() {
        if (Animation.hasAnimation(this, "open_close")) {
            return;
        }
        this.isActive = false;
        if (this.closeAnimation != null) {
            Animation.play(this.closeAnimation);
        } else {
            Animation.play(this, "open_close", 0.26, (element, progress) -> this.setAlpha(1.0 - progress)).onComplete(element -> this.setEnabled(false));
        }
    }

    @Override
    public void render(MatrixStack stack, double partialTicks, double mouseX, double mouseY) {
        if (this.screen == null) {
            return;
        }
        this.screen.drawModalBackground(this);
    }

    @Override
    public GuiModal copy(GuiModal element) {
        super.copy(element);
        return element;
    }

    @Override
    public GuiModal clone() {
        return this.copy(new GuiModal());
    }

    public GuiModal setScreen(AbstractGuiScreen screen) {
        this.screen = screen;
        return this;
    }

    public boolean isCloseOnEsc() {
        return this.closeOnEsc;
    }

    public GuiModal setCloseOnEsc(boolean closeOnEsc) {
        this.closeOnEsc = closeOnEsc;
        return this;
    }

    public boolean isActive() {
        return this.isActive;
    }
}