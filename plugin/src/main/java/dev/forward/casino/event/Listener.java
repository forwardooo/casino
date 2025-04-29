package dev.forward.casino.event;

import java.util.function.Consumer;

public interface Listener<T> {
    Consumer<T> consumer();

    int priority();
}
