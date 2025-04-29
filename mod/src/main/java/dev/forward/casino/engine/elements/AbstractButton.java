package dev.forward.casino.engine.elements;

import dev.forward.casino.engine.event.*;
import dev.forward.casino.engine.animation.Animation;
import dev.forward.casino.util.color.ButtonColor;
import dev.forward.casino.util.math.Relative;
import dev.forward.casino.util.math.V3;

public abstract class AbstractButton<T extends AbstractButton<T>>
        extends AbstractCarvedRectangle<T> {
    protected Text textElement;
    protected ButtonColor buttonColor;
    protected boolean active = true;
    protected boolean playSound = true;
//    protected ResourceLocation clickSound = Sound.RANDOM_CLICK;

    public AbstractButton() {
        this("");
    }

    public AbstractButton(String content) {
        this(content, ButtonColor.BLUE);
    }

    public AbstractButton(String content, ButtonColor buttonColor) {
        this.setButtonColor(buttonColor);
        this.textElement = new Text(content).setOriginAndAlign(Relative.CENTER).setHideFromHierarchy(true);
        this.addChild(this.textElement);
        this.setSize(new V3(32.0 + this.textElement.getSize().getX(), 38.0));
        this.setupListeners();
    }

    protected void setupListeners() {
        this.registerEvent(HoverEvent.class, event -> {
            if (!this.active) {
                return;
            }
            Animation.stop(this.genericThis, "interactive");
            this.updateColor();
        });
        this.registerEvent(MouseClickEvent.class, event -> {
            if (!this.active) {
                return;
            }
            if (this.playSound) {

//                SoundPlayer.play(this.clickSound);
            }
            this.smoothChangeColor(this.buttonColor.getActiveColor(), 0.07);
            Animation.play(this.genericThis, "interactive", 0.07, (e, progress) -> {}).onComplete(element1 -> this.updateColor());
            int mouse = event.getMouse();
            this.fireEvent(new ButtonActionEvent(this, mouse));
            if (mouse == 0) {
                this.fireEvent(new ButtonLeftActionEvent(this));
            }
            if (mouse == 1) {
                this.fireEvent(new ButtonRightActionEvent(this));
            }
            if (mouse == 2) {
                this.fireEvent(new ButtonReleaseActionEvent(this));
            }
        });
    }

    public String getText() {
        return this.textElement.getText();
    }

    public T setText(String text) {
        this.textElement.setValue(text);
        return this.genericThis;
    }

    public T setActive(boolean isActive) {
        if (this.active == isActive) {
            return this.genericThis;
        }
        this.active = isActive;
        Animation.stop(this, "interactive");
        this.updateColor();
        return this.genericThis;
    }

    public T setActiveInstant(boolean isActive) {
        if (this.active == isActive) {
            return this.genericThis;
        }
        this.active = isActive;
        this.instantUpdateColor();
        return this.genericThis;
    }

    public T setPlaySound(boolean playSound) {
        this.playSound = playSound;
        return this.genericThis;
    }

    @Override
    public T setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return this.genericThis;
        }
        super.setEnabled(enabled);
        this.setColor(this.buttonColor.getDefaultColor());
        return this.genericThis;
    }

    public void updateColor() {
        if (!this.active) {
            this.smoothChangeColor(this.buttonColor.getDisableColor(), 0.15);
        } else if (this.isHover) {
            this.smoothChangeColor(this.buttonColor.getHoverColor(), 0.15);
        } else {
            this.smoothChangeColor(this.buttonColor.getDefaultColor(), 0.15);
        }
    }

    public void instantUpdateColor() {
        this.color = !this.active ? this.buttonColor.getDisableColor() : (this.isHover ? this.buttonColor.getHoverColor() : this.buttonColor.getDefaultColor());
    }

    public T setButtonColor(ButtonColor buttonColor) {
        this.buttonColor = buttonColor;
        this.setColor(buttonColor.getDefaultColor());
        this.updateColor();
        return this.genericThis;
    }

    public T setButtonColorInstant(ButtonColor buttonColor) {
        this.buttonColor = buttonColor;
        this.setColor(buttonColor.getDefaultColor());
        this.instantUpdateColor();
        return this.genericThis;
    }

    @Override
    public T copy(T element) {
        super.copy(element);
        ((AbstractButton)element).setActive(this.isActive()).setPlaySound(this.isPlaySound()).setButtonColor(this.getButtonColor());
        this.getTextElement().copy(element.getTextElement());
        return element;
    }

    public Text getTextElement() {
        return this.textElement;
    }

    public ButtonColor getButtonColor() {
        return this.buttonColor;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isPlaySound() {
        return this.playSound;
    }
}