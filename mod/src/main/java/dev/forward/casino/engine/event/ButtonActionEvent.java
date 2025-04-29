package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractButton;

public class ButtonActionEvent
        extends ElementEvent {
    private final int mouseButton;

    public ButtonActionEvent(AbstractButton<?> element, int mouseButton) {
        super(element);
        this.mouseButton = mouseButton;
    }

    public int getMouseButton() {
        return this.mouseButton;
    }
}
