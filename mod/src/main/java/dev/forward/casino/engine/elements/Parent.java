package dev.forward.casino.engine.elements;

import dev.forward.casino.util.math.V3;

public interface Parent<T extends AbstractElement<T>> {
    public T setPos(V3 var1);

    public V3 getPos();

    default public T setPosX(double posX) {
        return this.setPos((V3)this.getPos().setX(posX));
    }

    default public T setPosY(double posY) {
        return this.setPos((V3)this.getPos().setY(posY));
    }

    default public T setPosZ(double posZ) {
        return this.setPos((V3)this.getPos().setZ(posZ));
    }

    public T setOffset(V3 var1);

    public V3 getOffset();

    default public T setOffsetY(double offsetY) {
        return this.setOffset((V3)this.getOffset().setY(offsetY));
    }

    default public T addOffset(double x, double y, double z) {
        return this.setOffset(this.getOffset().add(x, y, z));
    }

    public T setOrigin(V3 var1);

    public T setAlign(V3 var1);

    default public T setOriginAndAlign(V3 relative) {
        this.setOrigin(relative);
        return this.setAlign(relative);
    }

    public T setSize(V3 var1);

    public V3 getSize();

    default public T setSizeX(double sizeX) {
        return this.setSize((V3)this.getSize().setX(sizeX));
    }

    default public T setSizeY(double sizeY) {
        return this.setSize((V3)this.getSize().setY(sizeY));
    }

    public T setScale(V3 var1);

    default public T setScale(double x, double y, double z) {
        return this.setScale(new V3(x, y, z));
    }

    public T setRotation(V3 var1);
    public T setGlowing(boolean bl);

    public V3 getRotation();

    default public T setRotationX(double rotationX) {
        return this.setRotation((V3)this.getRotation().setX(rotationX));
    }

    default public T setRotationY(double rotationY) {
        return this.setRotation((V3)this.getRotation().setY(rotationY));
    }

    default public T setRotationZ(double rotationZ) {
        return this.setRotation((V3)this.getRotation().setZ(rotationZ));
    }

    default public T addRotation(double x, double y, double z) {
        return this.setRotation(this.getRotation().add(x, y, z));
    }

    default public T multiplyRotation(double x, double y, double z) {
        return this.setRotation(this.getRotation().multiply(x, y, z));
    }
}
