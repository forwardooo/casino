package dev.forward.casino.event.impl.input;

import dev.forward.casino.event.Event;
import dev.forward.casino.event.EventBus;
import lombok.Getter;
import lombok.Setter;

public class MousePress implements Event {
    public static EventBus<MousePress> BUS = new EventBus<>();
    private static final MousePress INSTANCE = new MousePress();
    @Getter
    private int button;
    @Getter
    @Setter
    private boolean cancelled;
    @Getter
    private boolean state;
    public static MousePress set(int button, boolean state) {
        INSTANCE.button = button;

        INSTANCE.state = state;
        INSTANCE.cancelled = false;
        return INSTANCE;
    }
}
