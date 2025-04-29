package dev.forward.casino.event.impl.input;

import dev.forward.casino.event.Event;
import dev.forward.casino.event.EventBus;
import lombok.Getter;
import lombok.Setter;

public class KeyPress implements Event {
    public static EventBus<KeyPress> BUS = new EventBus<KeyPress>();
    private static final KeyPress INSTANCE = new KeyPress();
    @Getter
    private int key;
    @Getter
    @Setter
    private boolean cancelled;
    public static KeyPress of(int key) {
        INSTANCE.key = key;
        INSTANCE.cancelled = false;
        return INSTANCE;
    }
}
