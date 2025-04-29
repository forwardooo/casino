package dev.forward.casino.event;

import java.util.*;
import java.util.function.Consumer;

public class EventBus<T extends Event> {
    private final List<Listener<T>> listeners = new ArrayList<>();
    private final Comparator<Listener<T>> compare = (first, second) -> second.priority() - first.priority();
    public void register(int priority, Consumer<T> consumer) {
        Listener<T> register = new Listener<T>() {
            public Consumer<T> consumer() {
                return consumer;
            }
            public int priority() {
                return priority;
            }
        };
        listeners.add(register);
        listeners.sort(compare);
    }
    public void register(Consumer<T> consumer) {
        register(0, consumer);
    }
    public T fire(T t) {
        listeners.forEach((listener) -> {
            listener.consumer().accept(t);
        });
        return t;
    }
}
