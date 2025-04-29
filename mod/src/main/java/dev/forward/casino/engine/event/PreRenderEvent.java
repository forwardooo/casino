package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractElement;

public class PreRenderEvent
        extends ElementEvent {
    public PreRenderEvent(AbstractElement<?> element) {
        super(element);
    }
}
