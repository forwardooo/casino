package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractElement;

public class MouseClickEvent
        extends ElementEvent
        implements InteractiveEvent {
    private final int mouse;

    public MouseClickEvent(AbstractElement<?> element, int mouse) {
        super(element);
        this.mouse = mouse;
    }

    public int getMouse() {
        return this.mouse;
    }
}
