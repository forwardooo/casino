package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractElement;

public class HoverEvent
        extends AbstractEvent
        implements InteractiveEvent {
    private final boolean hover;

    public HoverEvent(AbstractElement<?> element, boolean hover) {
        super(element);
        this.hover = hover;
    }

    public boolean isHover() {
        return this.hover;
    }
}
