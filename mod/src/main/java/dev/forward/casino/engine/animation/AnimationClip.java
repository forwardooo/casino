package dev.forward.casino.engine.animation;

import dev.forward.casino.engine.elements.AbstractElement;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class AnimationClip<T extends AbstractElement<T>> {
    private final T element;
    private final String id;
    private final long delay;
    private final Function<Double, Double> easingFunction;
    private final BiConsumer<T, Double> consumer;
    private final List<Consumer<T>> onComplete = new ArrayList<Consumer<T>>();
    private final List<Consumer<T>> onStop = new ArrayList<Consumer<T>>();
    private final long duration;
    private long startTime;
    private boolean isCompleted = false;
    private boolean repeat;
    private AnimationClip<?> after;

    public AnimationClip(T element, String id, double seconds, Function<Double, Double> easingFunction, BiConsumer<T, Double> consumer) {
        this(element, id, seconds, 0.0, easingFunction, consumer);
    }

    public AnimationClip(T element, String id, double seconds, double delay, Function<Double, Double> easingFunction, BiConsumer<T, Double> consumer) {
        this.element = element;
        this.id = id;
        this.duration = (long)(seconds * 1000.0);
        this.delay = (long)(delay * 1000.0);
        this.easingFunction = easingFunction;
        this.consumer = consumer;
    }

    public void setup() {
        this.isCompleted = false;
        this.startTime = 0L;
    }

    public boolean isCompleted() {
        return this.isCompleted;
    }

    public AnimationClip<T> setRepeat(boolean repeat) {
        this.repeat = repeat;
        return this;
    }

    public AnimationClip<T> onComplete(Consumer<T> consumer) {
        this.onComplete.add(consumer);
        return this;
    }

    public AnimationClip<T> onStop(Consumer<T> consumer) {
        this.onStop.add(consumer);
        return this;
    }

    public void stopped() {
        for (Consumer<T> consumer : this.onStop) {
            consumer.accept(this.element);
        }
    }

    public <B extends AbstractElement<B>> AnimationClip<B> playAfter(AnimationClip<B> clip) {
        this.after = clip;
        return (AnimationClip<B>) this.after;
    }

    public void tick() {
        long currentDuration;
        double progress;
        if (this.isCompleted) {
            return;
        }
        long currentTime = Instant.now().toEpochMilli();
        if (this.startTime == 0L) {
            this.startTime = currentTime + this.delay;
        }
        if ((progress = this.calculateProgress(currentDuration = currentTime - this.startTime)) < 0.0) {
            return;
        }
        if (progress >= 1.0) {
            this.completeAnimation(currentTime);
        } else {
            this.consumer.accept(this.element, this.easingFunction.apply(progress));
        }
    }

    private double calculateProgress(long currentDuration) {
        return Math.min((double)currentDuration / (double)this.duration, 1.0);
    }

    private void completeAnimation(long currentTime) {
        this.consumer.accept(this.element, 1.0);
        for (Consumer<T> onCompleteConsumer : this.onComplete) {
            onCompleteConsumer.accept(this.element);
        }
        if (this.repeat) {
            this.startTime = currentTime + this.delay;
        } else {
            this.isCompleted = true;
            if (this.after != null) {
                Animation.play(this.after);
            }
        }
    }

    public T getElement() {
        return this.element;
    }

    public String getId() {
        return this.id;
    }
}
