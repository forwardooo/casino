package dev.forward.casino.engine.contexts;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.event.impl.render.GuiOverlayRender;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.engine.elements.AbstractElement;
import dev.forward.casino.engine.elements.Container;
import dev.forward.casino.util.math.V3;
import dev.forward.casino.util.render.GLUtils;

public class Overlay
        extends Container
        implements IContext {
    private static Overlay instance;
    private Overlay() {
        this.setScale(new V3(0.5, 0.5, 0.5));
//        this.addChild(new CarvedRectangle().setSize(new V3(400,400)).setTooltip("привіт хлоп"));
        GuiOverlayRender.BUS.register(event -> {
//            this.setColor(new Color(255,255,255,255));
//            this.setAlpha(0.92);;
//            this.setOutline(0);
//            if (this.getEnabledChildsCount() == 0) {
//                return;
//            }
            V3 mousePos = GLUtils.getMousePos();
            this.transformAndRender(event.getStack(),null, getPartialTicks(), mousePos.getX(), mousePos.getY());
            GlStateManager.disableBlend();
        });
    }

    public static Overlay get() {
        if (instance == null) {
            instance = new Overlay();
        }
        return instance;
    }

    public static Overlay add(AbstractElement<?>... elements) {
        return (Overlay)Overlay.get().addChild(elements);
    }

    public static Overlay remove(AbstractElement<?> ... elements) {
        return (Overlay)Overlay.get().removeChild(elements);
    }

    @Override
    public void unload() {
        this.clearChilds();
        instance = null;
    }
}
