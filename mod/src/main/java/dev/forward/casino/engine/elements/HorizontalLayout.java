package dev.forward.casino.engine.elements;

import dev.forward.casino.util.color.Palette;
import dev.forward.casino.util.math.V3;

public class HorizontalLayout
        extends AbstractLayout<HorizontalLayout> {
    protected double spacing;

    public HorizontalLayout() {
        this(0.0);
    }

    public HorizontalLayout(double spacing) {
        this.spacing = spacing;
        this.setColor(Palette.WHITE.alpha(0.0));
    }

    @Override
    public void rescale() {
        if (this.childs.isEmpty()) {
            this.setSize(this.inset.multiply(2.0));
            return;
        }
        double currentOffset = 0.0;
        double maxY = 0.0;
        for (AbstractElement<?> child : this.childs) {
            if (!child.isEnabled()) continue;
            double offsetX = child.getSize().getX() * child.getOrigin().getX();
            double offsetY = this.inset.getY() * 2.0 * (0.5 - this.getOrigin().getY());
            child.setOffset(new V3(currentOffset + offsetX + this.inset.getX(), offsetY += (child.getOrigin().getY() - this.getOrigin().getY()) * child.getSize().getY(), 0.0));
            child.align = this.getOrigin().setX(0.0);
            double sizeY = child.getSize().getY();
            if (sizeY > maxY) {
                maxY = sizeY;
            }
            currentOffset += child.getSize().getX() + this.spacing;
        }
        this.setSize(new V3(currentOffset - this.spacing + this.inset.getX() * 2.0, maxY + this.inset.getY() * 2.0));
    }

    public HorizontalLayout setSpacing(double spacing) {
        if (this.spacing == spacing) {
            return this;
        }
        this.spacing = spacing;
        this.markDirty();
        return this;
    }

    @Override
    public HorizontalLayout copy(HorizontalLayout element) {
        super.copy(element);
        element.setSpacing(this.getSpacing());
        return element;
    }

    @Override
    public HorizontalLayout clone() {
        return this.copy(new HorizontalLayout());
    }

    public double getSpacing() {
        return this.spacing;
    }
}