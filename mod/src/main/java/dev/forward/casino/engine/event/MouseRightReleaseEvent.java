package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractElement;

public class MouseRightReleaseEvent
        extends ElementEvent
        implements InteractiveEvent {
    public MouseRightReleaseEvent(AbstractElement<?> element) {
        super(element);
    }
}
