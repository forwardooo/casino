package dev.forward.casino.engine.elements;

import dev.forward.casino.util.color.Palette;
import dev.forward.casino.util.math.V3;

public class VerticalLayout
        extends AbstractLayout<VerticalLayout> {
    protected double spacing = 0.0;

    public VerticalLayout() {
        this(0.0);
    }

    public VerticalLayout(double spacing) {
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
        double maxX = 0.0;
        for (AbstractElement<?> child : this.childs) {
            if (!child.isEnabled()) continue;
            double offsetY = child.getSize().getY() * child.getOrigin().getY();
            double offsetX = this.inset.getX() * 2.0 * (0.5 - this.getOrigin().getX());
            child.setOffset(new V3(offsetX += (child.getOrigin().getX() - this.getOrigin().getX()) * child.getSize().getX(), currentOffset + offsetY + this.inset.getY(), 0.0));
            child.align = this.getOrigin().setY(0.0);
            double sizeX = child.getSize().getX();
            if (sizeX > maxX) {
                maxX = sizeX;
            }
            currentOffset += child.getSize().getY() + this.spacing;
        }
        this.setSize(new V3(maxX + this.inset.getX() * 2.0, currentOffset - this.spacing + this.inset.getY() * 2.0));
    }

    public VerticalLayout setSpacing(double spacing) {
        if (this.spacing == spacing) {
            return this;
        }
        this.spacing = spacing;
        this.markDirty();
        return this;
    }

    @Override
    public VerticalLayout copy(VerticalLayout element) {
        super.copy(element);
        element.setSpacing(this.getSpacing());
        return element;
    }

    @Override
    public VerticalLayout clone() {
        return this.copy(new VerticalLayout());
    }

    public double getSpacing() {
        return this.spacing;
    }
}