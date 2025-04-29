package dev.forward.casino.event.impl.render;

import dev.forward.casino.event.Event;
import dev.forward.casino.event.EventBus;
import dev.forward.casino.event.impl.loop.GameTickPre;

public class RenderTickPost implements Event {
    public static EventBus<RenderTickPost> BUS = new EventBus<>();
    public static RenderTickPost INSTANCE = new RenderTickPost();
}
