package dev.forward.casino.engine.elements;

import dev.forward.casino.util.math.V3;

public abstract class AbstractLayout<T extends AbstractLayout<T>>
        extends AbstractCarvedRectangle<T> {
    protected V3 inset = new V3();
    protected boolean rescaleLock;

    protected abstract void rescale();

    protected void attemptRescale() {
        if (this.rescaleLock) {
            return;
        }
        this.rescaleLock = true;
        this.rescale();
        this.rescaleLock = false;
    }

    public T setInset(V3 inset) {
        if (this.inset.equals(inset)) {
            return (T)((AbstractLayout)this.genericThis);
        }
        this.inset = inset;
        this.markDirty();
        return (T)((AbstractLayout)this.genericThis);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        this.attemptRescale();
    }

    @Override
    public void childUpdate(AbstractElement<?> child) {
        this.attemptRescale();
    }

    @Override
    public T copy(T element) {
        super.copy(element);
        ((AbstractLayout)element).setInset(this.getInset());
        return element;
    }

    public V3 getInset() {
        return this.inset;
    }
}
