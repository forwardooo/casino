package dev.forward.casino.engine.elements;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.engine.event.GuiScreenCloseEvent;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.engine.animation.Animation;
import dev.forward.casino.util.color.Color;
import dev.forward.casino.util.color.Palette;
import dev.forward.casino.util.math.V3;
import dev.forward.casino.util.render.SimpleDrawer;
import dev.forward.casino.util.screen.*;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGuiScreen
        extends AbstractElement<AbstractGuiScreen> implements ScreenDrawHandler,
        ScreenCloseHandler,
        ScreenResizeHandler,
        ScreenInitHandler,
        ScreenKeyTypedHandler, ScreenUpdateHandler, ScreenCharTypedHandler {
    public Container modalContainer;
    @Getter
    private final List<GuiModal> modalList = new ArrayList<GuiModal>();
    @Getter
    protected Screen screen;
    @Getter
    protected float blur = 0.0f;
    private GuiModal topOpenedModal;
    private GuiModal prevTopOpenedModal;
    private final double modalAlpha = 0.26;
    private double finalModalAlpha = 0.0;
    private double prevModalAlpha = 0.0;
    @Setter
    private boolean displayImageBackground = false;
    private final boolean useVanillaBackgroundFormat = true;
    @Setter
    private Identifier backgroundImage = new Identifier("casino:textures/default_background");

    public AbstractGuiScreen() {
        this.screen = new ScreenBuilding("screen").charTyped(this).draw(this).close(this).init(this).resize(this).update(this).keyTyped(this);
        if (!this.displayImageBackground) {
            this.setColor(new Color(0.0, 0.0, 0.0, 0.86));
        }
        this.modalContainer = new Container().setHideFromHierarchy(true);
        this.addChild(this.modalContainer);
        this.setScale(new V3(0.5, 0.5, 0.5));
        this.resize(this.screen, mc, 1, 1);
        this.registerEvent(GuiScreenCloseEvent.class, event -> this.disableHover(this));
    }

    private void disableHover(AbstractElement<?> element) {
        if (element == null) {
            return;
        }
        ArrayDeque<AbstractElement<?>> stack = new ArrayDeque<>();
        stack.push(element);
        while (!stack.isEmpty()) {
            AbstractElement<?> current = stack.pop();
            if (current.isHover) {
                current.setHoverState(false);
            }
            if (current.isFocused) {
                Engine.getInteractionManager().setFocusedElement(null);
                current.setFocused(false, null);
            }
            Engine.getInteractionManager().removeAttemptHoveredElement(current);
            for (AbstractElement<?> child : current.getChilds()) {
                if (child == null || !child.isEnabled()) continue;
                stack.push(child);
            }
        }
    }

    @Override
    public AbstractGuiScreen clearChilds() {
        super.clearChilds().addChild(this.modalContainer);
        return this;
    }

    public void open() {
        this.resize(this.screen, mc, 1, 1);
        mc.openScreen(this.screen);
        Engine.setCurrentScreen(this);
    }

    public void close() {
        mc.openScreen(null);
        this.fireEvent(new GuiScreenCloseEvent(this));
        Engine.setCurrentScreen(null);
    }

    public void drawScreen(Screen screen, MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
//        Engine.getLayeredFrameBuffer().startDrawFrame();
        this.transformAndRender(stack,null, partialTicks, mouseX, mouseY);
//        Engine.getLayeredFrameBuffer().endDrawFrame();
    }

    public void close(Screen screen) {
        this.fireEvent(new GuiScreenCloseEvent(this));
        Engine.setCurrentScreen(null);
    }

    public void init(Screen screen) {
        Engine.setCurrentScreen(this);
        this.resize(screen, mc, 1, 1);
    }

    public void resize(Screen screen, MinecraftClient mc, int w, int h) {
        Window resolution = mc.getWindow();
        double scaledWidth = mc.getWindow().getScaledWidth();
        double scaledHeight = mc.getWindow().getScaledHeight();
        double scaleFactor = 0.0;
        while (scaledWidth - 1920.0 * scaleFactor >= 480.0 && scaledHeight - 1080.0 * scaleFactor >= 270.0) {
            scaleFactor += 0.25;
        }
        if (scaleFactor < 1.0) {
            scaleFactor += 0.25;
        }
        V3 scale = new V3(scaleFactor * 2, scaleFactor *2 , scaleFactor * 2).divide(resolution.getScaleFactor());
        this.setScale(scale);
        this.setSize(new V3(resolution.getScaledWidth() * (1.0 / scale.getX()), resolution.getScaledHeight() * (1.0 / scale.getY()), 0.0));
    }

    public void update(Screen screen) {
        this.preTick();
        this.postTick();
    }

    @Override
    public AbstractGuiScreen setSize(V3 size) {
        super.setSize(size);
        this.modalContainer.setSize(size);
        for (GuiModal modal : this.modalList) {
            modal.setSize(size);
        }
        return this;
    }

    @Override
    public AbstractGuiScreen setColor(Color color) {
        super.setColor(color);
        this.finalModalAlpha = color.getAlpha();
        this.prevModalAlpha = 0.0;
        return this;
    }

    public AbstractGuiScreen addModal(GuiModal modal) {
        return this.addModal(0, modal);
    }

    public AbstractGuiScreen addModal(int index, GuiModal modal) {
        modal.setScreen(this);
        this.modalContainer.addChild(index, modal);
        this.modalList.add(index, modal);
        this.setupModals();
        return this;
    }

    private void setupModals() {
        int modalCount = this.modalList.size();
        for (int i = 0; i < modalCount; ++i) {
            GuiModal modal = this.modalList.get(i);
            modal.setRenderLayer(2 + modalCount - i);
        }
    }

    public void drawModalBackground(GuiModal modal) {
        if (modal == this.topOpenedModal) {
            this.drawBackground(this.finalModalAlpha);
        } else if (modal == this.prevTopOpenedModal) {
            this.drawBackground(this.prevModalAlpha);
        }
    }

    public void drawBackground(double alpha) {
        GlStateManager.disableAlphaTest();
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(770, 771, 1, 771);
        float totalAlpha = (float)(alpha * this.getAbsoluteAlpha());
        GlStateManager.color4f((float)this.color.getR(), (float)this.color.getG(), (float)this.color.getB(), totalAlpha);
        SimpleDrawer.drawRect(0.0, 0.0, this.size.getX(), this.size.getY());
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        Palette.WHITE.apply();
    }

    public void keyTyped(Screen screen, int code) {
        if (code == 1) {
            if (this.topOpenedModal != null) {
                if (this.topOpenedModal.isCloseOnEsc()) {
                    this.topOpenedModal.close();
                }
                return;
            }
            this.close();
            return;
        }
        AbstractElement<?> focusedElement = Engine.getInteractionManager().getFocusedElement();
        if (focusedElement != null) {
            focusedElement.keyPress(code);
        }
    }

    @Override
    public void render(MatrixStack stack, double partialTicks, double mouseX, double mouseY) {
        if (this.blur > 0.0f) {
            Palette.WHITE.apply();
            Engine.getBlurRender().draw(this.getSize(), new V3(0.0, 1.0), new V3(1.0, 0.0), this.blur, 0.0, false, false, false, false);
        }
        this.updateModals(this.modalList);
        if (this.displayImageBackground) {
            this.renderBackgroundImage();
        } else if (this.topOpenedModal == null) {
            this.drawBackground(this.finalModalAlpha);
        } else if (this.prevTopOpenedModal == null) {
            this.drawBackground(this.prevModalAlpha);
        }
    }

    private void renderBackgroundImage() {
        Window resolution = mc.getWindow();
        double width = resolution.getScaledWidth() * resolution.getScaleFactor();
        double height = resolution.getScaledHeight() * resolution.getScaleFactor();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        mc.getTextureManager().bindTexture(this.backgroundImage);
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.pushMatrix();
        if (this.useVanillaBackgroundFormat) {
            this.renderVanillaBackground(width, height, bufferbuilder, tessellator);
        } else {
            this.renderScaledBackground(width, height, bufferbuilder, tessellator);
        }
        GlStateManager.popMatrix();
    }

    private void renderVanillaBackground(double width, double height, BufferBuilder bufferbuilder, Tessellator tessellator) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        bufferbuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferbuilder.vertex(0.0, height, 0.0).texture(0.0F, (float) (height / 32.0F)).color(64, 64, 64, 255).next();
        bufferbuilder.vertex(width, height, 0.0).texture((float) (width / 32.0), (float) (height / 32.0)).color(64, 64, 64, 255).next();
        bufferbuilder.vertex(width, 0.0, 0.0).texture((float) (width / 32.0), 0.0F).color(64, 64, 64, 255).next();
        bufferbuilder.vertex(0.0, 0.0, 0.0).texture(0.0F, 0.0F).color(64, 64, 64, 255).next();
        tessellator.draw();
    }

    private void renderScaledBackground(double width, double height, BufferBuilder bufferbuilder, Tessellator tessellator) {
        bufferbuilder.begin(7, VertexFormats.POSITION_TEXTURE);
        bufferbuilder.vertex(0.0, height, 0.0).texture(0.0F, 0.0F).next();
        bufferbuilder.vertex(width, height, 0.0).texture(1.0F, 0.0F).next();
        bufferbuilder.vertex(width, 0.0, 0.0).texture(1.0F, 1.0F).next();
        bufferbuilder.vertex(0.0, 0.0, 0.0).texture(0.0F, 1.0F).next();
        tessellator.draw();
    }

    private void updateModals(List<GuiModal> modals) {
        GuiModal topModal = this.findTopActiveModal(modals);
        if (topModal != this.topOpenedModal) {
            this.handleModalChange(topModal);
        }
    }

    private GuiModal findTopActiveModal(List<GuiModal> modals) {
        for (GuiModal modal : modals) {
            if (!modal.isActive()) continue;
            if (Engine.getCurrentModal() != modal) {
                Engine.setCurrentModal(modal);
            }
            return modal;
        }
        if (Engine.getCurrentModal() != null) {
            Engine.setCurrentModal(null);
        }
        return null;
    }

    private void handleModalChange(GuiModal newTopModal) {
        this.prevTopOpenedModal = this.topOpenedModal;
        this.topOpenedModal = newTopModal;
        if (Engine.getCurrentModal() != this.prevTopOpenedModal) {
            Engine.setCurrentModal(this.prevTopOpenedModal);
        }
        this.playModalTransitionAnimation();
    }

    private void playModalTransitionAnimation() {
        Animation.play(this, "modal_transition", 0.26, (element, progress) -> {
            if (this.displayImageBackground) {
                double alpha = this.modalAlpha;
                this.finalModalAlpha = progress * alpha;
                this.prevModalAlpha = (alpha - this.finalModalAlpha) / (1.0 - this.finalModalAlpha);
            } else if (this.color != null) {
                double alpha = this.color.getAlpha();
                this.finalModalAlpha = progress * alpha;
                this.prevModalAlpha = (alpha - this.finalModalAlpha) / (1.0 - this.finalModalAlpha);
            }
        });
    }

    public void updateModalResize(GuiModal modal) {
    }

    @Override
    public AbstractGuiScreen copy(AbstractGuiScreen element) {
        super.copy(element);
        element.setBlur(this.getBlur());
        return element;
    }

    public AbstractGuiScreen setBlur(float blur) {
        this.blur = blur;
        return this;
    }
}
