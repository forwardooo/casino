package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractElement;

public class CharTypedEvent extends ElementEvent implements InteractiveEvent {
    private char charc;

    public CharTypedEvent(AbstractElement<?> element, char c) {
        super(element);
        this.charc = c;
    }

    public char getChar() {
        return this.charc;
    }
}
