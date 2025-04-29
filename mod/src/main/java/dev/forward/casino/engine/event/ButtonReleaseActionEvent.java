package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractButton;

public class ButtonReleaseActionEvent
        extends ElementEvent {
    public ButtonReleaseActionEvent(AbstractButton<?> element) {
        super(element);
    }
}
