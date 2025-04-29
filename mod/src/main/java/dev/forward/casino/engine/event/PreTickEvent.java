package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractElement;

public class PreTickEvent
        extends ElementEvent {
    public PreTickEvent(AbstractElement<?> element) {
        super(element);
    }
}
