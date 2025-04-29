package dev.forward.casino.event.impl.loop;

import dev.forward.casino.event.Event;
import dev.forward.casino.event.EventBus;


public class GameTickPre implements Event {
    public static EventBus<GameTickPre> BUS = new EventBus<>();
    public static final GameTickPre INSTANCE = new GameTickPre();
}
