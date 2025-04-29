package dev.forward.casino.event.impl.input;

import dev.forward.casino.event.Event;
import dev.forward.casino.event.EventBus;
import lombok.Getter;

public class MouseWheel implements Event {
    public static EventBus<MouseWheel> BUS = new EventBus<MouseWheel>();
    private static final MouseWheel INSTANCE = new MouseWheel();
    @Getter
    private int wheel;
    public static MouseWheel set(int wheel) {
        INSTANCE.wheel = wheel;

        return INSTANCE;
    }
}
