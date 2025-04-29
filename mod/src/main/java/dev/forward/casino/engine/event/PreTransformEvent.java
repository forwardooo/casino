package dev.forward.casino.engine.event;


import dev.forward.casino.engine.elements.AbstractElement;

public class PreTransformEvent
        extends ElementEvent {
    public PreTransformEvent(AbstractElement<?> element) {
        super(element);
    }
}
