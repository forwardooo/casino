package dev.forward.casino.event.impl.loop;

import dev.forward.casino.event.Event;
import dev.forward.casino.event.EventBus;

public class GameTickPost implements Event {
    public static EventBus<GameTickPost> BUS = new EventBus<>();
    public static final GameTickPost INSTANCE = new GameTickPost();
}
