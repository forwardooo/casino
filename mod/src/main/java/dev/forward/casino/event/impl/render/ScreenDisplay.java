package dev.forward.casino.event.impl.render;

import dev.forward.casino.event.Event;
import dev.forward.casino.event.EventBus;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.screen.Screen;

public class ScreenDisplay implements Event {
    public static EventBus<ScreenDisplay> BUS = new EventBus<>();
    private static final ScreenDisplay INSTANCE = new ScreenDisplay();
    @Getter
    private Screen screen;
    public static ScreenDisplay of(Screen screen) {
        INSTANCE.screen = screen;
        INSTANCE.cancelled = false;
        return INSTANCE;
    }
    @Getter
    @Setter
    private boolean cancelled;
}
