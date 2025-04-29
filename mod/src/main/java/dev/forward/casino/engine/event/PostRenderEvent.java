package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractElement;

public class PostRenderEvent
        extends ElementEvent {
    public PostRenderEvent(AbstractElement<?> element) {
        super(element);
    }
}
