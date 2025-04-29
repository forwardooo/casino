package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractElement;

public class MouseRightClickEvent
        extends ElementEvent
        implements InteractiveEvent {
    public MouseRightClickEvent(AbstractElement<?> element) {
        super(element);
    }
}
