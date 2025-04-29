package dev.forward.casino.engine.tooltip;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.engine.event.PostRenderEvent;
import dev.forward.casino.engine.event.PreRenderEvent;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.engine.contexts.PostOverlay;
import dev.forward.casino.engine.elements.AbstractElement;
import dev.forward.casino.engine.elements.AbstractGuiScreen;
import dev.forward.casino.util.math.V3;

public abstract class AbstractTooltip<T extends AbstractElement<T>> {
    protected T element;

    protected AbstractTooltip(T element, boolean disable) {
        this.element = element;
        element.registerEvent(PreRenderEvent.class, event -> GlStateManager.disableLighting());
        if (disable) {
            element.registerEvent(PostRenderEvent.class, event -> element.setEnabled(false));
        }
    }

    protected AbstractTooltip(T element) {
        this(element, true);
    }

    public void updateTooltipPosition(V3 renderPos) {
        PostOverlay overlay = PostOverlay.get();
        AbstractGuiScreen guiScreen = Engine.getCurrentScreen();
        if (guiScreen != null) {
            ((AbstractElement<?>)this.element).setScale(guiScreen.getScale().multiply(2.0));
        } else {
            ((AbstractElement<?>)this.element).setScale(new V3(1.0, 1.0, 1.0));
        }
        ((AbstractElement<?>)this.element).setPos(renderPos.multiply(2.0).add(this.element.getScale().multiply(new V3(24.0, -24.0))));
        V3 pos = this.element.getPos();
        V3 size = this.element.getSize().multiply(this.element.getScale());
        if (pos.getX() + size.getX() + 8.0 > overlay.getSize().getX()) {
            pos = pos.subtract(size.getX() + 48.0 * this.element.getScale().getX(), 0.0, 0.0);
        }
        V3 maxPos = overlay.getSize().subtract(size).subtract(8.0, 8.0, 0.0);
        this.element.setPosX(Math.max(Math.min(pos.getX(), maxPos.getX()), 8.0)).setPosY(Math.max(Math.min(pos.getY(), maxPos.getY()), 8.0));
        ((AbstractElement<?>)this.element).setEnabled(true);
    }
}
