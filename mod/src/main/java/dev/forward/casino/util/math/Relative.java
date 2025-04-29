package dev.forward.casino.util.math;

public class Relative {
    public static final V3 TOP_LEFT = new V3(0.0, 0.0, 0.0);
    public static final V3 TOP = new V3(0.5, 0.0, 0.0);
    public static final V3 TOP_RIGHT = new V3(1.0, 0.0, 0.0);
    public static final V3 RIGHT = new V3(1.0, 0.5, 0.0);
    public static final V3 BOTTOM_RIGHT = new V3(1.0, 1.0, 0.0);
    public static final V3 BOTTOM = new V3(0.5, 1.0, 0.0);
    public static final V3 BOTTOM_LEFT = new V3(0.0, 1.0, 0.0);
    public static final V3 LEFT = new V3(0.0, 0.5, 0.0);
    public static final V3 CENTER = new V3(0.5, 0.5, 0.0);
    private static final V3[] values = new V3[]{TOP_LEFT, TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, CENTER};
}
