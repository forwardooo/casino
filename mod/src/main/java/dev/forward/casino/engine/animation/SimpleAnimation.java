package dev.forward.casino.engine.animation;

import dev.forward.casino.engine.elements.AbstractElement;
import dev.forward.casino.util.math.MathUtil;
import dev.forward.casino.util.math.V3;

import java.util.function.Function;

public class SimpleAnimation {
    public static <T extends AbstractElement<T>> AnimationClip<T> playSize(T element, V3 targetV3, double duration, double delay, Function<Double, Double> easingFunc) {
        Animation.stop(element, "simple_size");
        V3 originalSize = element.getSize();
        return Animation.play(element, "simple_size", duration, delay, easingFunc, (e, progress) -> e.setSize(MathUtil.calculateDifference(originalSize, targetV3, progress)));
    }

    public static <T extends AbstractElement<T>> AnimationClip<T> playSize(T element, V3 targetV3, double duration, Function<Double, Double> easingFunc) {
        return SimpleAnimation.playSize(element, targetV3, duration, 0.0, easingFunc);
    }

    public static <T extends AbstractElement<T>> AnimationClip<T> playRotation(T element, V3 targetV3, double duration, double delay, Function<Double, Double> easingFunc) {
        Animation.stop(element, "simple_rotation");
        V3 originalRotation = element.getRotation();
        return Animation.play(element, "simple_rotation", duration, delay, easingFunc, (e, progress) -> e.setRotation(MathUtil.calculateDifference(originalRotation, targetV3, progress)));
    }

    public static <T extends AbstractElement<T>> AnimationClip<T> playRotation(T element, V3 targetV3, double duration, Function<Double, Double> easingFunc) {
        return SimpleAnimation.playRotation(element, targetV3, duration, 0.0, easingFunc);
    }

//    public static <T extends AbstractElement<T>> AnimationClip<T> clipRotation(T element, V3 targetV3, double duration, double delay, Function<Double, Double> easingFunc) {
//        V3 originalRotation = element.getRotation();
//        return new AnimationClip<AbstractElement>(element, "simple_rotation", duration, delay, easingFunc, (e, progress) -> e.setRotation(MathUtil.calculateDifference(originalRotation, targetV3, progress)));
//    }
//
//    public static <T extends AbstractElement<T>> AnimationClip<T> clipRotation(T element, V3 targetV3, double duration, Function<Double, Double> easingFunc) {
//        return SimpleAnimation.clipRotation(element, targetV3, duration, 0.0, easingFunc);
//    }
}
