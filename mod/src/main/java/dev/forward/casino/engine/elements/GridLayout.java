package dev.forward.casino.engine.elements;

import dev.forward.casino.util.color.Palette;
import dev.forward.casino.util.math.Relative;
import dev.forward.casino.util.math.V3;

import java.util.ArrayList;
import java.util.Iterator;

public class GridLayout
        extends AbstractLayout<GridLayout> {
    protected LayoutPriority priority = LayoutPriority.HORIZONTAL;
    protected int rows = 0;
    protected int columns = 0;
    protected double verticalSpacing = -1.0;
    protected double horizontalSpacing = -1.0;

    public GridLayout(LayoutPriority priority) {
        this(priority, -1, -1);
    }

    public GridLayout(LayoutPriority priority, int rows, int columns) {
        this(priority, rows, columns, 0.0);
    }

    public GridLayout(LayoutPriority priority, int rows, int columns, double space) {
        this(priority, rows, columns, space, space);
    }

    public GridLayout(LayoutPriority priority, int rows, int columns, double verticalSpace, double horizontalSpace) {
        this.priority = priority;
        this.rows = rows;
        this.columns = columns;
        this.verticalSpacing = verticalSpace;
        this.horizontalSpacing = horizontalSpace;
        this.setColor(Palette.WHITE.alpha(0.0));
    }

    @Override
    public void rescale() {
        V3 currentOffset = this.inset;
        V3 maxPos = new V3();
        if (this.priority == LayoutPriority.HORIZONTAL) {
            double currentMaxYSize = 0.0;
            int currentColumns = 0;
            ArrayList<Double> rowsSizes = new ArrayList<Double>();
            double rowSize = 0.0;
            for (AbstractElement child : this.childs) {
                if (!child.isEnabled()) continue;
                if (this.columns > 0 && ++currentColumns > this.columns) {
                    currentColumns = 1;
                    currentOffset = currentOffset.set(this.inset.getX(), currentOffset.getY() + currentMaxYSize + this.verticalSpacing, 0.0);
                    currentMaxYSize = 0.0;
                    rowsSizes.add(rowSize - this.horizontalSpacing);
                    rowSize = 0.0;
                }
                V3 originOffset = child.getSize().multiply(child.getOrigin());
                child.setOffset(new V3(currentOffset.getX() + originOffset.getX(), currentOffset.getY() + originOffset.getY()));
                child.align = Relative.TOP_LEFT;
                currentOffset = currentOffset.add(child.getSize().getX() + this.horizontalSpacing, 0.0, 0.0);
                currentMaxYSize = Math.max(currentMaxYSize, child.getSize().getY());
                maxPos = maxPos.set(Math.max(maxPos.getX(), currentOffset.getX() - this.horizontalSpacing), Math.max(maxPos.getY(), currentOffset.getY() + currentMaxYSize), 0.0);
                rowSize += child.getSize().getX() + this.horizontalSpacing;
            }
            if (rowSize > 0.0) {
                rowsSizes.add(rowSize - this.horizontalSpacing);
            }
            Iterator rowsIterator = rowsSizes.iterator();
            Iterator childsIterator = this.childs.iterator();
            while (rowsIterator.hasNext()) {
                double size = (Double)rowsIterator.next();
                double freeSpace = maxPos.getX() - this.inset.getX() - size;
                double offset = freeSpace * this.origin.getX();
                for (int n = 0; n < this.columns && childsIterator.hasNext(); ++n) {
                    AbstractElement child = (AbstractElement)childsIterator.next();
                    if (!child.isEnabled()) {
                        --n;
                        continue;
                    }
                    child.addOffset(offset, 0.0, 0.0);
                }
            }
        } else if (this.priority == LayoutPriority.VERTICAL) {
            double currentMaxXSize = 0.0;
            int currentRows = 0;
            ArrayList<Double> columnsSizes = new ArrayList<Double>();
            double columnSize = 0.0;
            for (AbstractElement<?> child : this.childs) {
                if (!child.isEnabled()) continue;
                if (this.rows > 0 && ++currentRows > this.rows) {
                    currentRows = 1;
                    currentOffset = currentOffset.set(currentOffset.getX() + currentMaxXSize + this.horizontalSpacing, this.inset.getY(), 0.0);
                    currentMaxXSize = 0.0;
                    columnsSizes.add(columnSize - this.horizontalSpacing);
                    columnSize = 0.0;
                }
                V3 originOffset = child.getSize().multiply(child.getOrigin());
                child.setOffset(new V3(currentOffset.getX() + originOffset.getX(), currentOffset.getY() + originOffset.getY()));
                child.align = Relative.TOP_LEFT;
                currentOffset = currentOffset.add(0.0, child.getSize().getY() + this.verticalSpacing, 0.0);
                currentMaxXSize = Math.max(currentMaxXSize, child.getSize().getX());
                maxPos = maxPos.set(Math.max(maxPos.getX(), currentOffset.getX() + currentMaxXSize), Math.max(maxPos.getY(), currentOffset.getY() - this.verticalSpacing), 0.0);
                columnSize += child.getSize().getY() + this.verticalSpacing;
            }
            if (columnSize > 0.0) {
                columnsSizes.add(columnSize - this.verticalSpacing);
            }
            Iterator<Double> columnsIterator = columnsSizes.iterator();
            Iterator<AbstractElement<?>> childsIterator = this.childs.iterator();
            while (columnsIterator.hasNext()) {
                double size = columnsIterator.next();
                double freeSpace = maxPos.getY() - this.inset.getY() - size;
                double offset = freeSpace * this.origin.getY();
                for (int n = 0; n < this.columns && childsIterator.hasNext(); ++n) {
                    AbstractElement<?> child = childsIterator.next();
                    if (!child.isEnabled()) {
                        --n;
                        continue;
                    }
                    child.addOffset(0.0, offset, 0.0);
                }
            }
        }
        this.setSize(maxPos.add(this.inset));
    }

    public GridLayout setPriority(LayoutPriority priority) {
        if (this.priority == priority) {
            return this;
        }
        this.priority = priority;
        this.markDirty();
        return this;
    }

    public GridLayout setRows(int rows) {
        if (this.rows == rows) {
            return this;
        }
        this.rows = rows;
        this.markDirty();
        return this;
    }

    public GridLayout setColumns(int columns) {
        if (this.columns == columns) {
            return this;
        }
        this.columns = columns;
        this.markDirty();
        return this;
    }

    public GridLayout setVerticalSpacing(double verticalSpacing) {
        if (this.verticalSpacing == verticalSpacing) {
            return this;
        }
        this.verticalSpacing = verticalSpacing;
        this.markDirty();
        return this;
    }

    public GridLayout setHorizontalSpacing(double horizontalSpacing) {
        if (this.horizontalSpacing == horizontalSpacing) {
            return this;
        }
        this.horizontalSpacing = horizontalSpacing;
        this.markDirty();
        return this;
    }

    public GridLayout setSpacing(double space) {
        if (this.verticalSpacing == space && this.horizontalSpacing == space) {
            return this;
        }
        this.verticalSpacing = space;
        this.horizontalSpacing = space;
        this.markDirty();
        return this;
    }

    @Override
    public GridLayout copy(GridLayout element) {
        super.copy(element);
        element.setPriority(this.getPriority()).setRows(this.getRows()).setColumns(this.getColumns()).setVerticalSpacing(this.verticalSpacing).setHorizontalSpacing(this.horizontalSpacing);
        return element;
    }

    @Override
    public GridLayout clone() {
        return this.copy(new GridLayout(LayoutPriority.VERTICAL));
    }

    public LayoutPriority getPriority() {
        return this.priority;
    }

    public int getRows() {
        return this.rows;
    }

    public int getColumns() {
        return this.columns;
    }
}
