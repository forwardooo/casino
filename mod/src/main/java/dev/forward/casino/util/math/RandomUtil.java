package dev.forward.casino.util.math;

import lombok.Getter;

import java.util.Random;

public final class RandomUtil {
    @Getter
    static Random random = new Random();

    public static int getRandomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static double getRandomDouble(double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }

}
