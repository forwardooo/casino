package dev.forward.casino.event.impl.render;

import dev.forward.casino.event.Event;
import dev.forward.casino.event.EventBus;
import lombok.Getter;
import net.minecraft.client.util.math.MatrixStack;

@Getter
public class GuiOverlayRender implements Event {
    public static EventBus<GuiOverlayRender> BUS = new EventBus<>();
    private static final GuiOverlayRender INSTANCE = new GuiOverlayRender();
    private MatrixStack stack;
    private float partialTicks;
    public static GuiOverlayRender of(MatrixStack stack, float partialTicks) {
        INSTANCE.stack = stack;
        INSTANCE.partialTicks = partialTicks;
        return INSTANCE;
    }
}
