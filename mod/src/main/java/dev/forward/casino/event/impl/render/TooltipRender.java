package dev.forward.casino.event.impl.render;

import dev.forward.casino.event.Event;
import dev.forward.casino.event.EventBus;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

public class TooltipRender implements Event {
    public static EventBus<TooltipRender> BUS = new EventBus<>();
    private static final TooltipRender INSTANCE = new TooltipRender();
    @Getter
    protected List<Text> content;
    @Getter
    @Setter
    protected int x;
    @Getter
    @Setter
    protected int y;
    @Getter
    @Setter
    private boolean cancelled;
    @Getter
    @Setter
    private MatrixStack matrices;
    public void setPos(int n, int n2) {
        this.x = n;
        this.y = n2;
    }
    public static TooltipRender set(List<Text> list, int n, int n2) {
        INSTANCE.content = list;
        INSTANCE.x = n;
        INSTANCE.y = n2;
        INSTANCE.cancelled = false;
        return INSTANCE;
    }
}
