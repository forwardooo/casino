package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractElement;

public class PostTransformEvent
        extends ElementEvent {
    public PostTransformEvent(AbstractElement<?> element) {
        super(element);
    }
}
