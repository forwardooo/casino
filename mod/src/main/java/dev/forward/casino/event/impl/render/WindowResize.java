package dev.forward.casino.event.impl.render;

import dev.forward.casino.event.Event;
import dev.forward.casino.event.EventBus;

public class WindowResize implements Event {
    public static EventBus<WindowResize> BUS = new EventBus<>();
    public static final WindowResize INSTANCE = new WindowResize();
}
