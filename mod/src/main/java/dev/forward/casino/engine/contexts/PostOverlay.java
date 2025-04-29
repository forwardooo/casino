package dev.forward.casino.engine.contexts;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.event.impl.render.RenderTickPost;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.engine.elements.AbstractElement;
import dev.forward.casino.engine.elements.Container;
import dev.forward.casino.util.math.V3;
import dev.forward.casino.util.render.GLUtils;
import net.minecraft.client.util.math.MatrixStack;

public class PostOverlay
        extends Container
        implements IContext {
    private static PostOverlay instance;

    private PostOverlay() {
        this.setScale(new V3(0.5, 0.5, 0.5));
        RenderTickPost.BUS.register(-100,event -> {
            if (this.getEnabledChildsCount() == 0) {
                return;
            }
            V3 mousePos = GLUtils.getMousePos();
            this.transformAndRender(new MatrixStack(),null, getPartialTicks(), mousePos.getX(), mousePos.getY());
            GlStateManager.disableBlend();
        });
    }

    public static PostOverlay get() {
        if (instance == null) {
            instance = new PostOverlay();
        }
        return instance;
    }

    public static PostOverlay add(AbstractElement<?>... elements) {
        return (PostOverlay)PostOverlay.get().addChild(elements);
    }

    @Override
    public void unload() {
        this.clearChilds();
        instance = null;
    }
}