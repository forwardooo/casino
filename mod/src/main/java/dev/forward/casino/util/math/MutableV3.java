package dev.forward.casino.util.math;

public class MutableV3
        extends AbstractV3<MutableV3> {
    protected double x;
    protected double y;
    protected double z;

    public MutableV3() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    public MutableV3(double x, double y) {
        this(x, y, 0.0);
    }

    public MutableV3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public MutableV3 of(double x, double y, double z) {
        return new MutableV3(x, y, z);
    }

    @Override
    public MutableV3 set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    @Override
    public MutableV3 add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    @Override
    public MutableV3 multiply(double x, double y, double z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    @Override
    public MutableV3 divide(double x, double y, double z) {
        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }

    public String toString() {
        return String.format("MutableV3(%s, %s, %s)", this.x, this.y, this.z);
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
        if (!(o instanceof MutableV3)) {
            return false;
        }
        MutableV3 other = (MutableV3)o;
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
        return other instanceof MutableV3;
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
