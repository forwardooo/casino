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

public class HorizontalScrollView<T extends AbstractLayout<T>>
        extends AbstractScrollView<T, HorizontalScrollView<T>> {
    public HorizontalScrollView() {
    }

    public HorizontalScrollView(Class<T> layoutClass) {
        super(layoutClass);
    }

    @Override
    protected TriConsumer<Pair<MatrixStack, T>, Boolean, InteractionManager> getLayoutPostRender() {
        return (pair, isInteractive, interactionManager) -> {
            T element = pair.getRight();
            for (AbstractElement<?> child : element.getChilds()) {
                double minX = child.getOffset().getX() + child.getPos().getX() + element.getPos().getX() - child.getSize().getX() * child.getOrigin().getX();
                double maxX = minX + child.getSize().getX();
                if (child.getSize().getX() != 0.0 && (maxX < 0.0 || minX > this.getSize().getX())) {
                    child.isHover = false;
                    child.fireEvent(new ScrollViewLayoutElementHiddenEvent(child));
                    continue;
                }
                V3 mouse = GLUtils.getMousePos();
                child.transformAndRender(pair.getLeft(),this.layout, getPartialTicks(), mouse.getX(), mouse.getY(), isInteractive, interactionManager);
            }
            element.fireEvent(new PostRenderEvent(element));
        };
    }

    @Override
    protected void setupHandleBar() {
        this.bar.setOrigin(Relative.TOP_LEFT).setAlign(Relative.BOTTOM_LEFT).setSize(new V3(this.getSize().getX(), 4.0)).setPos(new V3(0.0, 10.0));
        this.handle.setOriginAndAlign(Relative.LEFT).setSize(this.bar.getSize());
        this.needHandlePos = this.handle.getSize().getX() / 2.0;
    }

    @Override
    protected double getLayoutMaxOffset() {
        double layoutSize = this.layout.getSize().getX();
        return Math.max(layoutSize - this.getSize().getX(), 0.0);
    }

    @Override
    protected double getCurrentHandleOffset() {
        return this.handle.getPos().getX();
    }

    @Override
    protected void changeLayoutSize(boolean force) {
        double layoutSizeX = this.layout.getSize().getX();
        double layoutMaxOffset = Math.max(layoutSizeX - this.getSize().getX(), 0.0);
        if (force || layoutSizeX - this.lastLayoutSize != 0.0) {
            this.lastLayoutSize = layoutSizeX;
            double handleSizeScale = 1.0 - Math.sqrt(layoutMaxOffset) / 50.0;
            this.handle.setSizeX(Math.max(this.bar.getSize().getX() * handleSizeScale, 60.0));
            double maxHandlePosX = this.bar.getSize().getX() - this.handle.getSize().getX();
            this.handle.setPosX(maxHandlePosX * this.scrollProgress);
        }
    }

    @Override
    protected double getMaxHandlePos() {
        return this.bar.getSize().getX() - this.handle.getSize().getX();
    }

    @Override
    protected void calculateScrollProgress(double maxHandlePos, double mouseX, double mouseY) {
        if (maxHandlePos == 0.0) {
            this.scrollProgress = 0.0;
            return;
        }
        double handlePosX = this.pressHandleOffset + (mouseX - this.pressMousePos.getX()) * (1.0 / this.getAbsoluteScale().getX());
        handlePosX = Math.max(Math.min(handlePosX, maxHandlePos), 0.0);
        this.handle.setPosX(handlePosX);
        this.scrollProgress = handlePosX / maxHandlePos;
    }

    @Override
    protected void setHandlePosInternal(double pos) {
        this.handle.setPosX(pos);
    }

    @Override
    protected void setLayoutPos(double pos) {
        this.layout.setPosX(pos);
    }

    @Override
    public HorizontalScrollView<T> setSize(V3 size) {
        super.setSize(size);
        this.bar.setSizeX(size.getX());
        this.changeLayoutSize(true);
        return this;
    }

    @Override
    public HorizontalScrollView<T> copy(HorizontalScrollView<T> element) {
        super.copy(element);
        element.setDisableBarOnInactive(this.isDisableBarOnInactive());
        this.getBar().copy(element.getBar());
        this.getHandle().copy(element.getHandle());
        ((AbstractLayout)this.getLayout()).copy(element.getLayout());
        return element;
    }

    @Override
    public HorizontalScrollView<T> clone() {
        return this.copy(new HorizontalScrollView(this.layout.getClass()));
    }
}