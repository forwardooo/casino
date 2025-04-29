package dev.forward.casino.engine.elements;

import net.minecraft.client.util.math.MatrixStack;

public class Container
        extends AbstractElement<Container> {
    @Override
    public void render(MatrixStack stack, double partialTicks, double mouseX, double mouseY) {
    }

    public int getEnabledChildsCount() {
        int count = 0;
        for (AbstractElement<?> child : this.getChilds()) {
            count += child.isEnabled() ? 1 : 0;
        }
        return count;
    }

    @Override
    public Container copy(Container element) {
        super.copy(element);
        return element;
    }

    @Override
    public Container clone() {
        return this.copy(new Container());
    }
}
