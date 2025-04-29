package dev.forward.casino.event.impl.render;

import dev.forward.casino.event.Event;
import dev.forward.casino.event.EventBus;

public class EntitiesRenderPost implements Event {
    public static EventBus<EntitiesRenderPost> BUS = new EventBus<>();
    public static final EntitiesRenderPost INSTANCE = new EntitiesRenderPost();
}
