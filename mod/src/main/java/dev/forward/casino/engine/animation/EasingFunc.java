package dev.forward.casino.engine.animation;

import java.util.function.Function;

public class EasingFunc {
    public static final Function<Double, Double> LINEAR = x -> x;
    public static final Function<Double, Double> EASE_IN_SINE = x -> 1.0 - Math.cos(x * Math.PI / 2.0);
    public static final Function<Double, Double> EASE_OUT_SINE = x -> Math.sin(x * Math.PI / 2.0);
    public static final Function<Double, Double> EASE_IN_OUT_SINE = x -> -(Math.cos(Math.PI * x) - 1.0) / 2.0;
    public static final Function<Double, Double> EASE_IN_QUAD = x -> x * x;
    public static final Function<Double, Double> EASE_OUT_QUAD = x -> 1.0 - (1.0 - x) * (1.0 - x);
    public static final Function<Double, Double> EASE_IN_OUT_QUAD = x -> x < 0.5 ? 2.0 * x * x : 1.0 - Math.pow(-2.0 * x + 2.0, 2.0) / 2.0;
    public static final Function<Double, Double> EASE_IN_CUBIC = x -> x * x * x;
    public static final Function<Double, Double> EASE_OUT_CUBIC = x -> 1.0 - Math.pow(1.0 - x, 3.0);
    public static final Function<Double, Double> EASE_IN_OUT_CUBIC = x -> x < 0.5 ? 4.0 * x * x * x : 1.0 - Math.pow(-2.0 * x + 2.0, 3.0) / 2.0;
    public static final Function<Double, Double> EASE_IN_QUART = x -> x * x * x * x;
    public static final Function<Double, Double> EASE_OUT_QUART = x -> 1.0 - Math.pow(1.0 - x, 4.0);
    public static final Function<Double, Double> EASE_IN_OUT_QUART = x -> x < 0.5 ? 8.0 * x * x * x * x : 1.0 - Math.pow(-2.0 * x + 2.0, 4.0) / 2.0;
    public static final Function<Double, Double> EASE_IN_QUINT = x -> x * x * x * x * x;
    public static final Function<Double, Double> EASE_OUT_QUINT = x -> 1.0 - Math.pow(1.0 - x, 5.0);
    public static final Function<Double, Double> EASE_IN_OUT_QUINT = x -> x < 0.5 ? 16.0 * x * x * x * x * x : 1.0 - Math.pow(-2.0 * x + 2.0, 5.0) / 2.0;
    public static final Function<Double, Double> EASE_IN_EXPO = x -> x == 0.0 ? 0.0 : Math.pow(2.0, 10.0 * x - 10.0);
    public static final Function<Double, Double> EASE_OUT_EXPO = x -> x == 1.0 ? 1.0 : 1.0 - Math.pow(2.0, -10.0 * x);
    public static final Function<Double, Double> EASE_IN_OUT_EXPO = x -> x == 0.0 ? 0.0 : (x == 1.0 ? 1.0 : (x < 0.5 ? Math.pow(2.0, 20.0 * x - 10.0) / 2.0 : (2.0 - Math.pow(2.0, -20.0 * x + 10.0)) / 2.0));
    public static final Function<Double, Double> EASE_IN_CIRC = x -> 1.0 - Math.sqrt(1.0 - Math.pow(x, 2.0));
    public static final Function<Double, Double> EASE_OUT_CIRC = x -> Math.sqrt(1.0 - Math.pow(x - 1.0, 2.0));
    public static final Function<Double, Double> EASE_IN_OUT_CIRC = x -> x < 0.5 ? (1.0 - Math.sqrt(1.0 - Math.pow(2.0 * x, 2.0))) / 2.0 : (Math.sqrt(1.0 - Math.pow(-2.0 * x + 2.0, 2.0)) + 1.0) / 2.0;
    public static final Function<Double, Double> EASE_IN_BACK = x -> {
        double c1 = 1.70158;
        double c3 = c1 + 1.0;
        return c3 * x * x * x - c1 * x * x;
    };
    public static final Function<Double, Double> EASE_OUT_BACK = x -> {
        double c1 = 1.70158;
        double c3 = c1 + 1.0;
        return 1.0 + c3 * Math.pow(x - 1.0, 3.0) + c1 * Math.pow(x - 1.0, 2.0);
    };
    public static final Function<Double, Double> EASE_IN_OUT_BACK = x -> {
        double c1 = 1.70158;
        double c2 = c1 * 1.525;
        return x < 0.5 ? Math.pow(2.0 * x, 2.0) * ((c2 + 1.0) * 2.0 * x - c2) / 2.0 : (Math.pow(2.0 * x - 2.0, 2.0) * ((c2 + 1.0) * (x * 2.0 - 2.0) + c2) + 2.0) / 2.0;
    };
    public static final Function<Double, Double> EASE_IN_ELASTIC = x -> {
        double c4 = 2.0943951023931953;
        return x == 0.0 ? 0.0 : (x == 1.0 ? 1.0 : -Math.pow(2.0, 10.0 * x - 10.0) * Math.sin((x * 10.0 - 10.75) * c4));
    };
    public static final Function<Double, Double> EASE_OUT_ELASTIC = x -> {
        double c4 = 2.0943951023931953;
        return x == 0.0 ? 0.0 : (x == 1.0 ? 1.0 : Math.pow(2.0, -10.0 * x) * Math.sin((x * 10.0 - 0.75) * c4) + 1.0);
    };
    public static final Function<Double, Double> EASE_IN_OUT_ELASTIC = x -> {
        double c5 = 1.3962634015954636;
        return x == 0.0 ? 0.0 : (x == 1.0 ? 1.0 : (x < 0.5 ? -(Math.pow(2.0, 20.0 * x - 10.0) * Math.sin((20.0 * x - 11.125) * c5)) / 2.0 : Math.pow(2.0, -20.0 * x + 10.0) * Math.sin((20.0 * x - 11.125) * c5) / 2.0 + 1.0));
    };
    public static final Function<Double, Double> EASE_OUT_BOUNCE = x -> {
        double n1 = 7.5625;
        double d1 = 2.75;
        if (x < 1.0 / d1) {
            return n1 * x * x;
        }
        if (x < 2.0 / d1) {
            x = x - 1.5 / d1;
            return n1 * x * x + 0.75;
        }
        if (x < 2.5 / d1) {
            x = x - 2.25 / d1;
            return n1 * x * x + 0.9375;
        }
        x = x - 2.625 / d1;
        return n1 * x * x + 0.984375;
    };
    public static final Function<Double, Double> EASE_IN_BOUNCE = x -> 1.0 - EASE_OUT_BOUNCE.apply(x - 1.0);
    public static final Function<Double, Double> EASE_IN_OUT_BOUNCE = x -> x < 0.5 ? (1.0 - EASE_OUT_BOUNCE.apply(1.0 - 2.0 * x)) / 2.0 : (1.0 + EASE_OUT_BOUNCE.apply(2.0 * x - 1.0)) / 2.0;
}
