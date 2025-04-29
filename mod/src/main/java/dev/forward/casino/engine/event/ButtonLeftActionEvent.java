package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractButton;

public class ButtonLeftActionEvent
        extends ElementEvent {
    public ButtonLeftActionEvent(AbstractButton<?> element) {
        super(element);
    }
}
