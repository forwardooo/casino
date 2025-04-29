package dev.forward.casino.engine.event;

import dev.forward.casino.engine.elements.AbstractGuiScreen;

public class GuiScreenCloseEvent
        extends ElementEvent {
    public GuiScreenCloseEvent(AbstractGuiScreen element) {
        super(element);
    }
}
