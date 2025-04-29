package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractElement;

public class KeyPressEvent
        extends ElementEvent
        implements InteractiveEvent {
    private final int key;

    public KeyPressEvent(AbstractElement<?> element, int key) {
        super(element);
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }
}
