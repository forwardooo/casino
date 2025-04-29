package dev.forward.casino.slots;

import dev.forward.casino.util.RandomObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
@AllArgsConstructor
@Getter
public enum SlotEnum {
    DOG1(37.5, 15, 5, 2.5),
    DOG2(25, 10, 4, 2),
    DOG3(15, 7.5, 2.5, 1.5),
    DOG4(10, 4, 2, 1.25),
    COLLAR(7.50, 2.5, 1.25, 1),
    BONE(5, 2.5, 1, 0.5),
    A(2.50, 1, 0.5, 0.25),
    K(2.50, 1, 0.5, 0.25),
    Q(1.5, 1, 0.5, 0.25),
    J(1.5, 1, 0.5, 0.25),
    TEN(1.5, 1, 0.5, 0.25);

    private final double win5;
    private final double win4;
    private final double win3;
    private final double win2;

    public double getSum(int count) {
        switch (count) {
            case 2:
                return win2;
            case 3:
                return win3;
            case 4:
                return win4;
            case 5:
                return win5;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static RandomObject<SlotEnum> getRandom() {
        Map<SlotEnum, Integer> map = new HashMap<>();
        map.put(DOG1, 25);
        map.put(DOG2, 35);
        map.put(DOG3, 50);
        map.put(DOG4, 70);
        map.put(COLLAR, 100);
        map.put(BONE, 120);
        map.put(A, 140);
        map.put(K, 140);
        map.put(Q, 200);
        map.put(J, 200);
        map.put(TEN, 200);
        return new RandomObject<>(map);
    }
}
