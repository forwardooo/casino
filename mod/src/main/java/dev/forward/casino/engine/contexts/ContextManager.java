package dev.forward.casino.engine.contexts;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.engine.elements.AbstractElement;
import dev.forward.casino.engine.elements.AbstractGuiScreen;
import dev.forward.casino.engine.elements.GuiModal;
import dev.forward.casino.event.impl.loop.GameTickPost;
import dev.forward.casino.event.impl.loop.GameTickPre;
import dev.forward.casino.event.impl.render.WindowResize;
import dev.forward.casino.util.FastAccess;
import dev.forward.casino.util.math.V3;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.util.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Getter
public class ContextManager implements FastAccess {
    private final List<AbstractElement<?>> renderContexts = new ArrayList<>();
    @Setter
    private AbstractGuiScreen currentScreen;
    @Setter
    private GuiModal currentModal;

    public ContextManager() {
        this.renderContexts.addAll(Arrays.asList(PostOverlay.get(), InventoryContext.get(), Overlay.get(),  WorldContext.get())); //+ tab context post worl context
        GameTickPre.BUS.register(event -> {
            for (AbstractElement<?> context : this.renderContexts) {
                context.preTick();
            }
        });
        GameTickPost.BUS.register(event -> {
            for (AbstractElement<?> context : this.renderContexts) {
                context.postTick();
            }
        });
        WindowResize.BUS.register(event -> this.rescale(mc.getWindow()));
        this.rescale(mc.getWindow());
    }

    public void unload() {
        for (AbstractElement<?> context : this.renderContexts) {
            ((IContext) context).unload();
        }
        this.renderContexts.clear();
    }

    public void rescale(Window resolution) {
        V3 size = new V3(resolution.getScaledWidth() * 2.0, resolution.getScaledHeight() * 2.0, 0.0);
        for (AbstractElement<?> context : this.renderContexts) {
            if (context.isWorldContext()) continue;
            context.setSize(size);
        }
    }

}
