package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractElement;
import lombok.Getter;

public class FocusedEvent
        extends AbstractEvent
        implements InteractiveEvent {
    @Getter
    private final boolean focused;
    private final AbstractElement<?> newFocusedElement;

    public FocusedEvent(AbstractElement<?> element, boolean focused, AbstractElement<?> newFocusedElement) {
        super(element);
        this.focused = focused;
        this.newFocusedElement = newFocusedElement;
    }

}
