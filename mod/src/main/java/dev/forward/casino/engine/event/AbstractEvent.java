package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractElement;

public abstract class AbstractEvent {
    private AbstractElement<?> element;

    public AbstractEvent(AbstractElement<?> element) {
        this.element = element;
    }
}
