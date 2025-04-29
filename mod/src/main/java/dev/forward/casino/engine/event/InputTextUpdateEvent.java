package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.Input;

public class InputTextUpdateEvent
        extends ElementEvent {
    private String text;

    public InputTextUpdateEvent(Input element, String text) {
        super(element);
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
