package dev.forward.casino.engine.elements;


import dev.forward.casino.util.color.Palette;
import dev.forward.casino.util.math.Relative;
import dev.forward.casino.util.math.V3;

public class Flex
        extends AbstractLayout<Flex> {
    protected LayoutPriority priority = LayoutPriority.HORIZONTAL;
    protected double sizeLimit = 0.0;
    protected double verticalSpacing = 0.0;
    protected double horizontalSpacing = 0.0;

    public Flex(LayoutPriority priority, double sizeLimit) {
        this(priority, sizeLimit, 0.0, 0.0);
    }

    public Flex(LayoutPriority priority, double sizeLimit, double verticalSpace, double horizontalSpace) {
        this.priority = priority;
        this.sizeLimit = sizeLimit;
        this.verticalSpacing = verticalSpace;
        this.horizontalSpacing = horizontalSpace;
        this.setColor( Palette.WHITE.alpha(0.0));
    }

    @Override
    public void rescale() {
        V3 currentOffset = this.inset;
        V3 maxPos = new V3();
        if (this.priority == LayoutPriority.HORIZONTAL) {
            double currentMaxYSize = 0.0;
            double currentSize = 0.0;
            for (AbstractElement child : this.childs) {
                if (!child.isEnabled()) continue;
                double sizeX = child.getSize().getX();
                if ((currentSize += sizeX + (currentSize != 0.0 ? this.horizontalSpacing : 0.0)) > this.sizeLimit) {
                    currentSize = sizeX;
                    currentOffset = currentOffset.set(this.inset.getX(), currentOffset.getY() + currentMaxYSize + this.verticalSpacing, 0.0);
                    currentMaxYSize = 0.0;
                }
                V3 originOffset = child.getSize().multiply(child.getOrigin());
                child.setOffset(new V3(currentOffset.getX() + originOffset.getX(), currentOffset.getY() + originOffset.getY()));
                child.align = Relative.TOP_LEFT;
                currentOffset = currentOffset.add(child.getSize().getX() + this.horizontalSpacing, 0.0, 0.0);
                currentMaxYSize = Math.max(currentMaxYSize, child.getSize().getY());
                maxPos = maxPos.set(this.sizeLimit, Math.max(maxPos.getY(), currentOffset.getY() + currentMaxYSize), 0.0);
            }
        } else if (this.priority == LayoutPriority.VERTICAL) {
            double currentMaxXSize = 0.0;
            double currentSize = 0.0;
            for (AbstractElement<?> child : this.childs) {
                if (!child.isEnabled()) continue;
                double sizeY = child.getSize().getY();
                if ((currentSize += sizeY + (currentSize != 0.0 ? this.verticalSpacing : 0.0)) > this.sizeLimit) {
                    currentSize = sizeY;
                    currentOffset = currentOffset.set(currentOffset.getX() + currentMaxXSize + this.horizontalSpacing, this.inset.getY(), 0.0);
                    currentMaxXSize = 0.0;
                }
                V3 originOffset = (V3)child.getSize().multiply(child.getOrigin());
                child.setOffset(new V3(currentOffset.getX() + originOffset.getX(), currentOffset.getY() + originOffset.getY()));
                child.align = Relative.TOP_LEFT;
                currentOffset = currentOffset.add(0.0, child.getSize().getY() + this.verticalSpacing, 0.0);
                currentMaxXSize = Math.max(currentMaxXSize, child.getSize().getX());
                maxPos = maxPos.set(Math.max(maxPos.getX(), currentOffset.getX() + currentMaxXSize), this.sizeLimit, 0.0);
            }
        }
        this.setSize((V3)maxPos.add(this.inset));
    }

    public Flex setPriority(LayoutPriority priority) {
        if (this.priority == priority) {
            return this;
        }
        this.priority = priority;
        this.markDirty();
        return this;
    }

    public Flex setVerticalSpacing(double verticalSpacing) {
        if (this.verticalSpacing == verticalSpacing) {
            return this;
        }
        this.verticalSpacing = verticalSpacing;
        this.markDirty();
        return this;
    }

    public Flex setHorizontalSpacing(double horizontalSpacing) {
        if (this.horizontalSpacing == horizontalSpacing) {
            return this;
        }
        this.horizontalSpacing = horizontalSpacing;
        this.markDirty();
        return this;
    }

    public Flex setSizeLimit(double sizeLimit) {
        if (this.sizeLimit == sizeLimit) {
            return this;
        }
        this.sizeLimit = sizeLimit;
        this.markDirty();
        return this;
    }

    @Override
    public Flex copy(Flex element) {
        super.copy(element);
        element.setVerticalSpacing(this.getVerticalSpacing()).setHorizontalSpacing(this.getHorizontalSpacing()).setPriority(this.getPriority()).setSizeLimit(this.getSizeLimit());
        return element;
    }

    @Override
    public Flex clone() {
        return this.copy(new Flex(this.getPriority(), this.getSizeLimit()));
    }

    public LayoutPriority getPriority() {
        return this.priority;
    }

    public double getSizeLimit() {
        return this.sizeLimit;
    }

    public double getVerticalSpacing() {
        return this.verticalSpacing;
    }

    public double getHorizontalSpacing() {
        return this.horizontalSpacing;
    }
}
