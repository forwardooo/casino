package dev.forward.casino.event.impl.render;

import dev.forward.casino.event.Event;
import dev.forward.casino.event.EventBus;

public class RenderTickPre implements Event {
    public static EventBus<RenderTickPre> BUS = new EventBus<>();
    public static RenderTickPre INSTANCE = new RenderTickPre();
}
