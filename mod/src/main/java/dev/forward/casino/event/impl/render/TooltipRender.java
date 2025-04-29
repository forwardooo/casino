package dev.forward.casino.event.impl.render;

import dev.forward.casino.event.Event;
import dev.forward.casino.event.EventBus;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

@Getter
public class TooltipRender implements Event {
    public static EventBus<TooltipRender> BUS = new EventBus<>();
    private static final TooltipRender INSTANCE = new TooltipRender();
    protected List<Text> content;
    @Setter
    protected int x;
    @Setter
    protected int y;
    @Setter
    private boolean cancelled;
    @Setter
    private MatrixStack matrices;
    public static TooltipRender of(List<Text> list, int n, int n2) {
        INSTANCE.content = list;
        INSTANCE.x = n;
        INSTANCE.y = n2;
        INSTANCE.cancelled = false;
        return INSTANCE;
    }
}
