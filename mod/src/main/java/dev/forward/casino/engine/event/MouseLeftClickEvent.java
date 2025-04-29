package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractElement;

public class MouseLeftClickEvent
        extends ElementEvent
        implements InteractiveEvent {
    public MouseLeftClickEvent(AbstractElement<?> element) {
        super(element);
    }
}
