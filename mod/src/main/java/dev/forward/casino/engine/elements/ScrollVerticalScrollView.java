package dev.forward.casino.engine.elements;

import dev.forward.casino.engine.event.MouseWheelEvent;

public class ScrollVerticalScrollView<T extends AbstractLayout<T>>
        extends VerticalScrollView<T> {
    public ScrollVerticalScrollView(Class<T> layoutClass) {
        super(layoutClass);
        this.clearEventListeners(MouseWheelEvent.class);
    }
}
