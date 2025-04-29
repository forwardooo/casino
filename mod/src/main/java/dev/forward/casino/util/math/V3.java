package dev.forward.casino.util.math;

public class V3
        extends AbstractV3<V3> {
    protected final double x;
    protected final double y;
    protected final double z;

    public V3() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    public V3(double x, double y) {
        this(x, y, 0.0);
    }

    public V3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public V3(V3 v3) {
        this(v3.getX(), v3.getY(), v3.getZ());
    }

    @Override
    public V3 of(double x, double y, double z) {
        return new V3(x, y, z);
    }

    @Override
    public V3 set(double x, double y, double z) {
        return new V3(x, y, z);
    }

    @Override
    public V3 add(double x, double y, double z) {
        return new V3(this.x + x, this.y + y, this.z + z);
    }

    @Override
    public V3 multiply(double x, double y, double z) {
        return new V3(this.x * x, this.y * y, this.z * z);
    }

    @Override
    public V3 divide(double x, double y, double z) {
        return new V3(this.x / x, this.y / y, this.z / z);
    }

    public V3 cross(V3 v) {
        double tempX = this.y * v.getZ() - this.z * v.getY();
        double tempY = this.z * v.getX() - this.x * v.getZ();
        double z2 = this.x * v.getY() - this.y * v.getX();
        return new V3(tempX, tempY, z2);
    }

    public V3 rotateY(double angle) {
        angle = Math.toRadians(angle);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double tempX = this.x * cos - this.z * sin;
        double z2 = this.z * cos + this.x * sin;
        return new V3(tempX, this.y, z2);
    }

    public String toString() {
        return String.format("V3(%s, %s, %s)", this.x, this.y, this.z);
    }

    @Override
    public V3 clone() {
        return new V3(this.x, this.y, this.z);
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    @Override
    public double getZ() {
        return this.z;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof V3)) {
            return false;
        }
        V3 other = (V3)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (Double.compare(this.getX(), other.getX()) != 0) {
            return false;
        }
        if (Double.compare(this.getY(), other.getY()) != 0) {
            return false;
        }
        return Double.compare(this.getZ(), other.getZ()) == 0;
    }

    protected boolean canEqual(Object other) {
        return other instanceof V3;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $x = Double.doubleToLongBits(this.getX());
        result = result * 59 + (int)($x >>> 32 ^ $x);
        long $y = Double.doubleToLongBits(this.getY());
        result = result * 59 + (int)($y >>> 32 ^ $y);
        long $z = Double.doubleToLongBits(this.getZ());
        result = result * 59 + (int)($z >>> 32 ^ $z);
        return result;
    }
}