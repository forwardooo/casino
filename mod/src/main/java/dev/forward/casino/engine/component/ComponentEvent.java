package dev.forward.casino.engine.component;

import dev.forward.casino.engine.event.AbstractEvent;
import dev.forward.casino.engine.elements.AbstractElement;

public class ComponentEvent
        extends AbstractEvent {
    private AbstractElement<?> element;

    public ComponentEvent(AbstractElement<?> element) {
        super(element);
    }

    public void setElement(AbstractElement<?> element) {
        this.element = element;
    }
}
