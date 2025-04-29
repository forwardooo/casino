package dev.forward.casino.util.math;
public abstract class AbstractV3<T extends AbstractV3<T>> {
    public abstract T of(double var1, double var3, double var5);

    public abstract double getX();

    public T setX(double x) {
        return this.set(x, this.getY(), this.getZ());
    }

    public abstract double getY();

    public T setY(double y) {
        return this.set(this.getX(), y, this.getZ());
    }

    public abstract double getZ();

    public T setZ(double z) {
        return this.set(this.getX(), this.getY(), z);
    }

    public abstract T set(double var1, double var3, double var5);

    public T set(AbstractV3<?> v) {
        return this.set(v.getX(), v.getY(), v.getZ());
    }

    public abstract T add(double var1, double var3, double var5);

    public T add(AbstractV3<?> v) {
        return this.add(v.getX(), v.getY(), v.getZ());
    }

    public T subtract(double x, double y, double z) {
        return this.add(-x, -y, -z);
    }

    public T subtract(AbstractV3<?> v) {
        return this.subtract(v.getX(), v.getY(), v.getZ());
    }

    public abstract T multiply(double var1, double var3, double var5);

    public T multiply(double m) {
        return this.multiply(m, m, m);
    }

    public T multiply(AbstractV3<?> m) {
        return this.multiply(m.getX(), m.getY(), m.getZ());
    }

    public abstract T divide(double var1, double var3, double var5);

    public T divide(double d) {
        return this.divide(d, d, d);
    }

    public T divide(AbstractV3<?> d) {
        return this.divide(d.getX(), d.getY(), d.getZ());
    }

    public double squaredDistance(AbstractV3<?> v) {
        double d0 = v.getX() - this.getX();
        double d1 = v.getY() - this.getY();
        double d2 = v.getZ() - this.getZ();
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double getLength() {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double dot(AbstractV3<?> v) {
        return this.getX() * v.getX() + this.getY() * v.getY() + this.getZ() * v.getZ();
    }

    public T clone() {
        return this.of(this.getX(), this.getY(), this.getZ());
    }
}