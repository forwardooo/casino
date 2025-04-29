package dev.forward.casino.engine.elements;

import dev.forward.casino.engine.event.PostRenderEvent;
import dev.forward.casino.engine.event.ScrollViewLayoutElementHiddenEvent;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.util.interactive.InteractionManager;
import dev.forward.casino.util.math.Relative;
import dev.forward.casino.util.math.V3;
import dev.forward.casino.util.render.GLUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Pair;
import org.apache.logging.log4j.util.TriConsumer;

public class VerticalScrollView<T extends AbstractLayout<T>>
        extends AbstractScrollView<T, VerticalScrollView<T>> {
    public VerticalScrollView() {
    }

    public VerticalScrollView(Class<T> layoutClass) {
        super(layoutClass);
    }

    @Override
    protected TriConsumer<Pair<MatrixStack, T>, Boolean, InteractionManager> getLayoutPostRender() {
        return (pair, isInteractive, interactionManager) -> {
            V3 mouse = GLUtils.getMousePos();
            AbstractElement<?> element = pair.getRight();
            for (AbstractElement<?> child : element.getChilds()) {
                double minY = child.getOffset().getY() + child.getPos().getY() + element.getPos().getY() - child.getSize().getY() * child.getOrigin().getY();
                double maxY = minY + child.getSize().getY();
                if (child.getSize().getY() != 0.0 && (maxY < 0.0 || minY > this.getSize().getY())) {
                    child.isHover = false;
                    child.fireEvent(new ScrollViewLayoutElementHiddenEvent(child));
                    continue;
                }
                child.transformAndRender(pair.getLeft(),this.layout, getPartialTicks(), mouse.getX(), mouse.getY(), isInteractive, interactionManager);
            }
            element.fireEvent(new PostRenderEvent(element));
        };
    }

    @Override
    protected void setupHandleBar() {
        this.bar.setOrigin(Relative.TOP_LEFT).setAlign(Relative.TOP_RIGHT).setSize(new V3(4.0, this.getSize().getY())).setPos(new V3(10.0, 0.0));
        this.handle.setOriginAndAlign(Relative.TOP).setSize(this.bar.getSize());
        this.needHandlePos = this.handle.getSize().getY() / 2.0;
    }

    @Override
    protected double getLayoutMaxOffset() {
        double layoutSize = this.layout.getSize().getY();
        return Math.max(layoutSize - this.getSize().getY(), 0.0);
    }

    @Override
    protected double getCurrentHandleOffset() {
        return this.handle.getPos().getY();
    }

    @Override
    protected void changeLayoutSize(boolean force) {
        double layoutSizeY = this.layout.getSize().getY();
        double layoutMaxOffset = Math.max(layoutSizeY - this.getSize().getY(), 0.0);
        if (force || layoutSizeY - this.lastLayoutSize != 0.0) {
            this.lastLayoutSize = layoutSizeY;
            double handleSizeScale = 1.0 - Math.sqrt(layoutMaxOffset) / 50.0;
            this.handle.setSizeY(Math.max(this.bar.getSize().getY() * handleSizeScale, 60.0));
            double maxHandlePosY = this.bar.getSize().getY() - this.handle.getSize().getY();
            this.handle.setPosY(maxHandlePosY * this.scrollProgress);
        }
    }

    @Override
    protected double getMaxHandlePos() {
        return this.bar.getSize().getY() - this.handle.getSize().getY();
    }

    @Override
    protected void calculateScrollProgress(double maxHandlePos, double mouseX, double mouseY) {
        if (maxHandlePos == 0.0) {
            this.scrollProgress = 0.0;
            return;
        }
        double handlePosY = this.pressHandleOffset + (mouseY - this.pressMousePos.getY()) * (1.0 / this.getAbsoluteScale().getY());
        handlePosY = Math.max(Math.min(handlePosY, maxHandlePos), 0.0);
        this.handle.setPosY(handlePosY);
        this.scrollProgress = handlePosY / maxHandlePos;
    }

    @Override
    protected void setHandlePosInternal(double pos) {
        this.handle.setPosY(pos);
    }

    @Override
    protected void setLayoutPos(double pos) {
        this.layout.setPosY(pos);
    }

    @Override
    public VerticalScrollView<T> setSize(V3 size) {
        super.setSize(size);
        this.bar.setSizeY(size.getY());
        this.changeLayoutSize(true);
        return this;
    }

    @Override
    public VerticalScrollView<T> copy(VerticalScrollView<T> element) {
        super.copy(element);
        element.setDisableBarOnInactive(this.isDisableBarOnInactive());
        this.getBar().copy(element.getBar());
        this.getHandle().copy(element.getHandle());
        ((AbstractLayout)this.getLayout()).copy(element.getLayout());
        return element;
    }

    @Override
    public VerticalScrollView<T> clone() {
        return this.copy(new VerticalScrollView(this.layout.getClass()));
    }
}