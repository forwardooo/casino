package dev.forward.casino.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomObject<T> {
    private final Map<T, Integer> weightMap;
    private final int totalWeight;
    private final Random random;

    public RandomObject(Map<T, Integer> weightMap) {
        this.weightMap = new HashMap<>(weightMap);
        this.random = new Random();

        int sum = 0;
        for (int weight : weightMap.values()) {
            if (weight <= 0) throw new IllegalArgumentException("Random weight will be more then 0");
            sum += weight;
        }
        this.totalWeight = sum;
    }

    @SafeVarargs
    public RandomObject(T... items) {
        this.weightMap = new HashMap<>();
        this.random = new Random();
        for (T item : items) {
            weightMap.put(item, 1);
        }
        this.totalWeight = weightMap.size();
    }
    public T get() {
        int randomValue = random.nextInt(totalWeight);
        int currentSum = 0;

        for (Map.Entry<T, Integer> entry : weightMap.entrySet()) {
            currentSum += entry.getValue();
            if (randomValue < currentSum) {
                return entry.getKey();
            }
        }

        throw new IllegalStateException("Error while trying to random");
    }

}