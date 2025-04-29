package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractButton;

public class ButtonRightActionEvent
        extends ElementEvent {
    public ButtonRightActionEvent(AbstractButton<?> element) {
        super(element);
    }
}
