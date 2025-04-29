package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractElement;

public class MouseWheelEvent
        extends ElementEvent
        implements InteractiveEvent {
    private final int dWheel;

    public MouseWheelEvent(AbstractElement<?> element, int dWheel) {
        super(element);
        this.dWheel = dWheel;
    }

    public int getDWheel() {
        return this.dWheel;
    }
}
