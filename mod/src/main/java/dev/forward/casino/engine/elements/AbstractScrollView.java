package dev.forward.casino.engine.elements;
import dev.forward.casino.engine.event.MouseLeftClickEvent;
import dev.forward.casino.engine.event.MouseWheelEvent;
import dev.forward.casino.engine.event.PostRenderEvent;
import dev.forward.casino.engine.animation.Animation;
import dev.forward.casino.util.color.Color;
import dev.forward.casino.util.color.Palette;
import dev.forward.casino.util.interactive.InteractionManager;
import dev.forward.casino.util.math.V3;
import dev.forward.casino.util.render.GLUtils;
import lombok.Getter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Pair;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.List;

public abstract class AbstractScrollView<T extends AbstractLayout<T>, B extends AbstractScrollView<T, B>>
        extends AbstractElement<B> {
    @Getter
    protected Rectangle bar;
    @Getter
    protected Rectangle handle;
    @Getter
    protected T layout;
    @Getter
    protected double scrollProgress;
    protected double lastLayoutSize = 0.0;
    protected V3 pressMousePos = new V3();
    protected double pressHandleOffset = 0.0;
    protected double needHandlePos;
    protected int scrollStep = 100;
    @Getter
    protected boolean disableBarOnInactive = false;
    protected double animateStartScrollProgress = 0.0;
    protected double animateScrollOffset = 0.0;

    public AbstractScrollView() {
        this((Class<T>) VerticalLayout.class);
    }

    public AbstractScrollView(Class<T> layoutClass) {
        this.setHoverLayer(1L);
        this.layout = this.makeLayout(layoutClass);
        super.addChild(this.layout);
        this.bar = new Rectangle().setColor(this.getColor()).setHideFromHierarchy(true);
        super.addChild(this.bar);
        this.handle = new Rectangle().setColor(Palette.getBrighterColor(this.getColor())).setHideFromHierarchy(true);
        this.handle.registerEvent(MouseLeftClickEvent.class, event -> {
            this.pressMousePos = GLUtils.getMousePos();
            this.pressHandleOffset = this.getCurrentHandleOffset();
        });
        this.bar.addChild(this.handle);
        this.setupHandleBar();
        this.registerEvent(MouseWheelEvent.class, event -> {
            if (this.handle.isLeftPressed()) {
                return;
            }
            int wheel = event.getDWheel();
            int scrollSteps = wheel / -120;
            double layoutMaxOffset = this.getLayoutMaxOffset();
            double progressStep = (double)this.scrollStep / layoutMaxOffset;
            this.animateScrollOffset = Animation.hasAnimation(this, "scroll") ? (this.animateScrollOffset -= this.scrollProgress - this.animateStartScrollProgress) : 0.0;
            this.animateStartScrollProgress = this.scrollProgress;
            this.animateScrollOffset = Math.max(Math.min(this.animateStartScrollProgress + this.animateScrollOffset + progressStep * (double)scrollSteps, 1.0), 0.0) - this.animateStartScrollProgress;
            if (this.animateScrollOffset == 0.0) {
                Animation.stop(this, "scroll");
                return;
            }
            Animation.play(this.genericThis, "scroll", 0.26, (element1, progress) -> {
                this.scrollProgress = this.animateStartScrollProgress + this.animateScrollOffset * progress;
            });
        });
    }

    protected abstract TriConsumer<Pair<MatrixStack, T>, Boolean, InteractionManager> getLayoutPostRender();

    private T makeLayout(Class<T> layoutClass) {
        final TriConsumer<Pair<MatrixStack, T>, Boolean, InteractionManager> layoutPostRender = this.getLayoutPostRender();
        if (layoutClass == GridLayout.class) {
            return (T)new GridLayout(LayoutPriority.HORIZONTAL, -1, 1, 5.0){
                @Override
                public void postRender(MatrixStack stack,double partialTicks, double mouseX, double mouseY, boolean isInteractive, InteractionManager interactionManager) {
                    layoutPostRender.accept(new Pair<>(stack, (T) this), isInteractive, interactionManager);
                }
            };
        }
        if (layoutClass == HorizontalLayout.class) {
            return (T)new HorizontalLayout(5.0){

                @Override
                public void postRender(MatrixStack stack,double partialTicks, double mouseX, double mouseY, boolean isInteractive, InteractionManager interactionManager) {
                    layoutPostRender.accept(new Pair<>(stack, (T) this), isInteractive, interactionManager);
                }
            };
        }
        if (layoutClass == Flex.class) {
            LayoutPriority priority = this instanceof VerticalScrollView ? LayoutPriority.HORIZONTAL : LayoutPriority.VERTICAL;
            return (T)new Flex(priority, 100.0){

                @Override
                public void postRender(MatrixStack stack,double partialTicks, double mouseX, double mouseY, boolean isInteractive, InteractionManager interactionManager) {
                    layoutPostRender.accept(new Pair<>(stack, (T) this), isInteractive, interactionManager);
                }
            };
        }
        return (T)new VerticalLayout(5.0){

            @Override
            public void postRender(MatrixStack stack,double partialTicks, double mouseX, double mouseY, boolean isInteractive, InteractionManager interactionManager) {
                layoutPostRender.accept(new Pair<>(stack, (T) this), isInteractive, interactionManager);
            }
        };
    }

    protected abstract void setupHandleBar();

    protected abstract double getLayoutMaxOffset();

    protected abstract double getCurrentHandleOffset();

    @Override
    public B setColor(Color color) {
        super.setColor(color);
        this.bar.setColor(color);
        this.handle.setColor(Palette.getBrighterColor(this.getColor()));
        return this.genericThis;
    }

    public B setScrollProgress(double progress) {
        this.scrollProgress = progress;
        return this.genericThis;
    }

    public B setDisableBarOnInactive(boolean disableBarOnInactive) {
        this.disableBarOnInactive = disableBarOnInactive;
        return this.genericThis;
    }

    @Override
    public B addChild(AbstractElement<?> ... elements) {
        ((AbstractElement<?>)this.layout).addChild(elements);
        return this.genericThis;
    }

    @Override
    public B addChild(int index, AbstractElement<?> element) {
        ((AbstractElement<?>)this.layout).addChild(index, element);
        return this.genericThis;
    }

    @Override
    public B removeChild(AbstractElement<?> ... elements) {
        ((AbstractElement<?>)this.layout).removeChild(elements);
        return this.genericThis;
    }

    @Override
    public B clearChilds() {
        ((AbstractElement<?>)this.layout).clearChilds();
        return this.genericThis;
    }

    @Override
    public int getChildCount() {
        return this.layout.getChildCount();
    }

    @Override
    public List<AbstractElement<?>> getChilds() {
        return this.layout.getChilds();
    }

    @Override
    public void updateHoverState(InteractionManager interactionManager, double mouseX, double mouseY, boolean isInteractive) {
        boolean newHover;
        if (!this.isInteractive() && !interactionManager.isAlwaysAllowHover()) {
            newHover = false;
        } else {
            AbstractElement<?> upperParent = this.getUpperParent();
            boolean bl = newHover = upperParent != null && upperParent.isWorldContext() ? this.getHover3d() : this.getHover2d(mouseX, mouseY);
        }
        if (newHover) {
            interactionManager.attemptSetHoveredElement(this, this.getHierarchyDeep());
            return;
        }
        this.setHoverState(false);
    }

    protected abstract void changeLayoutSize(boolean var1);

    protected abstract double getMaxHandlePos();

    protected abstract void calculateScrollProgress(double var1, double var3, double var5);

    protected abstract void setHandlePosInternal(double var1);

    protected abstract void setLayoutPos(double var1);

    @Override
    public void render(MatrixStack stack, double partialTicks, double mouseX, double mouseY) {
        GLUtils.enableScissor(new V3(0.0, 0.0, 0.0), this.getSize().multiply(this.getAbsoluteScale()));
        this.layout.transformAndRender(stack,this, partialTicks, mouseX, mouseY, this.isHover(), this.lastInteractionManager);
        GLUtils.disableScissor();
        this.changeLayoutSize(false);
        double layoutMaxOffset = this.getLayoutMaxOffset();
        if (layoutMaxOffset == 0.0) {
            this.scrollProgress = 0.0;
        }
        if (this.disableBarOnInactive) {
            this.bar.setEnabled(layoutMaxOffset != 0.0);
        }
        double maxHandlePos = this.getMaxHandlePos();
        if (this.handle.isLeftPressed()) {
            Animation.stop(this, "scroll");
            this.calculateScrollProgress(maxHandlePos, mouseX, mouseY);
        }
        this.setHandlePosInternal(maxHandlePos * this.scrollProgress);
        this.setLayoutPos(-layoutMaxOffset * this.scrollProgress);
    }

    @Override
    public void postRender(MatrixStack stack,double partialTicks, double mouseX, double mouseY, boolean isInteractive, InteractionManager interactionManager) {
        this.fireEvent(new PostRenderEvent(this));
        for (AbstractElement<?> child : this.childs) {
            if (child == this.layout) continue;
            child.transformAndRender(stack,this, partialTicks, mouseX, mouseY, isInteractive, interactionManager);
        }
    }

}
