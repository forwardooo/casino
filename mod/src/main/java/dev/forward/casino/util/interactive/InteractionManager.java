package dev.forward.casino.util.interactive;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.engine.elements.AbstractElement;
import dev.forward.casino.engine.elements.AbstractGuiScreen;
import dev.forward.casino.event.impl.input.KeyPress;
import dev.forward.casino.event.impl.input.MousePress;
import dev.forward.casino.event.impl.input.MouseWheel;
import dev.forward.casino.event.impl.input.TextPaste;
import dev.forward.casino.event.impl.render.RenderTickPre;
import dev.forward.casino.event.impl.render.ScreenDisplay;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

public class InteractionManager {
    protected final HashMap<Long, HoverElementData> hoverLayers = new HashMap();
    @Getter
    protected AbstractElement<?> focusedElement;
    @Getter
    protected boolean alwaysAllowHover;

    public InteractionManager() {
        this.setup();
    }

    protected void setup() {
        RenderTickPre.BUS.register(-10, event -> {
            for (HoverElementData data : this.hoverLayers.values()) {
                data.element.setHoverState(true);
            }
            this.hoverLayers.clear();
        });
        KeyPress.BUS.register(event -> {
            if (this.focusedElement != null && this.focusedElement.isEnabled()) {
                this.focusedElement.keyPress(event.getKey());
            }
        });
        MousePress.BUS.register(10, event -> {
            block7: {
                List<AbstractElement<?>> renderContexts;
                AbstractGuiScreen screen;
                int mouseButton;
                block6: {
                    mouseButton = event.getButton();
                    screen = Engine.getCurrentScreen();
                    renderContexts = Engine.getContextManager().getRenderContexts();
                    if (!event.isState()) break block6;
                    AbstractElement<?> tempFocusedElement = null;
                    if (mouseButton == 0) {
                        tempFocusedElement = this.focusedElement;
                        this.focusedElement = null;
                    }
                    if (screen == null || !screen.mouseClick(this, mouseButton)) {
                        for (AbstractElement<?> context : renderContexts) {
                            if (context.mouseClick(this, mouseButton)) break;
                        }
                    }
                    if (mouseButton != 0 || tempFocusedElement == this.focusedElement) break block7;
                    if (tempFocusedElement != null) {
                        tempFocusedElement.setFocused(false, this.focusedElement);
                    }
                    if (this.focusedElement == null) break block7;
                    this.focusedElement.setFocused(true, this.focusedElement);
                    break block7;
                }
                if (screen != null) {
                    screen.mouseRelease(mouseButton);
                }
                for (AbstractElement<?> context : renderContexts) {
                    context.mouseRelease(mouseButton);
                }
            }
        });
        MouseWheel.BUS.register(10, event -> {
            int wheel = event.getWheel();
            AbstractGuiScreen screen = Engine.getCurrentScreen();
            List<AbstractElement<?>> renderContexts = Engine.getContextManager().getRenderContexts();
            for (AbstractElement<?> context : renderContexts) {
                context.wheel(wheel);
            }
            if (screen != null) {
                screen.wheel(wheel);
            }
        });
        TextPaste.BUS.register(event -> {
            AbstractElement<?> element = this.getFocusedElement();
            if (element != null) {
                element.pasteText(event.getText());
            }
        });
        ScreenDisplay.BUS.register(event -> this.setFocusedElement(null));
    }

    public void setFocusedElement(AbstractElement<?> element) {
        if (this.focusedElement == element) {
            return;
        }
        this.focusedElement = element;
    }

    public void attemptSetHoveredElement(AbstractElement<?> element, long hierarchyDeep) {
        long hoverLayer = element.getHoverLayer();
        if (Engine.getCurrentModal() != null && element.getTotalRenderLayer() < Engine.getCurrentModal().getTotalRenderLayer()) {
            element.setHoverState(false);
            return;
        }
        HoverElementData data = this.hoverLayers.getOrDefault(hoverLayer, null);
        if (data != null) {
            if (data.getElement().getTotalRenderLayer() > element.getTotalRenderLayer() || data.hierarchyDeep >= hierarchyDeep) {
                element.setHoverState(false);
                return;
            }
            data.element.setHoverState(false);
        }
        data = new HoverElementData(element, hierarchyDeep);
        this.hoverLayers.put(hoverLayer, data);
    }

    public void removeAttemptHoveredElement(AbstractElement<?> element) {
        this.hoverLayers.values().removeIf(hoverElementData -> hoverElementData.getElement().equals(element));
    }

    public static class HoverElementData {
        @Getter
        private final AbstractElement<?> element;
        private final long hierarchyDeep;

        public HoverElementData(AbstractElement<?> element, long hierarchyDeep) {
            this.element = element;
            this.hierarchyDeep = hierarchyDeep;
        }

    }
}
