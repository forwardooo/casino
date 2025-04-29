package dev.forward.casino.engine.event;


import dev.forward.casino.engine.elements.AbstractElement;

public class ScrollViewLayoutElementHiddenEvent
        extends ElementEvent {
    public ScrollViewLayoutElementHiddenEvent(AbstractElement<?> element) {
        super(element);
    }
}
