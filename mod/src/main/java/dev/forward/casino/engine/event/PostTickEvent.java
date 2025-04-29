package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractElement;

public class PostTickEvent
        extends ElementEvent {
    public PostTickEvent(AbstractElement<?> element) {
        super(element);
    }
}
