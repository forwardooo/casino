package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractElement;

public class ElementEvent
        extends AbstractEvent {
    public ElementEvent(AbstractElement<?> element) {
        super(element);
    }
}
