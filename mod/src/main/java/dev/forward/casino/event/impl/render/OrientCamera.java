package dev.forward.casino.event.impl.render;
import dev.forward.casino.event.Event;
import dev.forward.casino.event.EventBus;
import lombok.Getter;

public class OrientCamera implements Event {
    public static EventBus<OrientCamera> BUS = new EventBus<>();
    private static final OrientCamera INSTANCE = new OrientCamera();
    @Getter
    private float partialTicks;
    public static OrientCamera set(float tick) {
        INSTANCE.partialTicks = tick;
        return INSTANCE;
    }
}
