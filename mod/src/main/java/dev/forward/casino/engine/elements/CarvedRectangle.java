package dev.forward.casino.engine.elements;

public class CarvedRectangle
        extends AbstractCarvedRectangle<CarvedRectangle> {
    public CarvedRectangle() {
        super(4.0);
    }

    public CarvedRectangle(double carveSize) {
        super(carveSize, 2.0);
    }

    @Override
    public CarvedRectangle clone() {
        return this.copy(new CarvedRectangle());
    }
}
