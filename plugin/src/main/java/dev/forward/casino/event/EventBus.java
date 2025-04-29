package dev.forward.casino.event;

import org.bukkit.event.Event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class EventBus<T> {

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
    public void fire(T t) {
        listeners.forEach((listener) -> {
            listener.consumer().accept(t);
        });
    }
    private EventBus() {
    }
    private static final Map<Class<?>, EventBus<?>> REGISTERED_BUSES = new ConcurrentHashMap<>();
    @SuppressWarnings("unchecked")
    public static <B> EventBus<B> of(Class<B> eventClass) {
        return (EventBus<B>) REGISTERED_BUSES.computeIfAbsent(eventClass, k -> new EventBus<B>());
    }
}
