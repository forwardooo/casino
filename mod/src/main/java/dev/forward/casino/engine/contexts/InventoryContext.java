package dev.forward.casino.engine.contexts;


import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.engine.elements.AbstractElement;
import dev.forward.casino.engine.elements.Container;
import dev.forward.casino.event.impl.render.RenderTickPost;
import dev.forward.casino.event.impl.render.ScreenDisplay;
import dev.forward.casino.util.math.V3;
import dev.forward.casino.util.render.GLUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;

public class InventoryContext
        extends Container
        implements IContext {
    private static InventoryContext instance;

    private InventoryContext() {
        this.setScale(new V3(0.5, 0.5, 0.5));
        RenderTickPost.BUS.register(event -> {
            if (this.getEnabledChildsCount() == 0) {
                return;
            }
            Screen screen = mc.currentScreen;
            if (!(screen instanceof InventoryScreen)) {
                return;
            }
            V3 mousePos = GLUtils.getMousePos();
            this.transformAndRender(new MatrixStack(),null, getPartialTicks(), mousePos.getX(), mousePos.getY());
            GlStateManager.disableBlend();
        });
        ScreenDisplay.BUS.register(event -> {
            Screen screen = mc.currentScreen;
            if (!(screen instanceof InventoryScreen) || screen == event.getScreen()) {
                return;
            }
            this.disableHover(this);
        });
    }

    public static InventoryContext get() {
        if (instance == null) {
            instance = new InventoryContext();
        }
        return instance;
    }

    @Override
    public void unload() {
        this.clearChilds();
        instance = null;
    }

    private void disableHover(AbstractElement<?> element) {
        element.setHoverState(false);
        for (AbstractElement<?> abstractElement : element.getChilds()) {
            this.disableHover(abstractElement);
        }
    }
}