package dev.forward.casino.util.math;

public class Objects {
    private final Object[] objects;
    public Objects(Object... objects) {
        this.objects = objects;
    }
    public <B> B get(int index) {
        return (B) objects[index];
    }
}