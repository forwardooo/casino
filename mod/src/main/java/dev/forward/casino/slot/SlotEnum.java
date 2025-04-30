package dev.forward.casino.element;

import dev.forward.casino.util.color.Color;
import dev.forward.casino.util.color.Palette;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.Identifier;
@Getter
@AllArgsConstructor
public enum SlotEnum {
    DOG1(new Identifier("casino:textures/dogs/dog1.png"), Palette.BLUE, Palette.BLUE_LIGHT,37.5, 15, 5, 2.5),
    DOG2(new Identifier("casino:textures/dogs/dog2.png"), Palette.YELLOW, Palette.YELLOW_LIGHT,25, 10, 4, 2),
    DOG3(new Identifier("casino:textures/dogs/dog3.png"), Palette.CYAN.darker(), Palette.CYAN,15, 7.5, 2.5, 1.5),
    DOG4(new Identifier("casino:textures/dogs/dog4.png"), Palette.PINK_62, Palette.PINK_LIGHT,10, 4, 2, 1.25),
    COLLAR(new Identifier("casino:textures/dogs/collar.png"), Palette.GREY_DARK_62, Palette.GREY_DARK_62.brighter(),7.50, 2.5, 1.25, 1),
    BONE(new Identifier("casino:textures/dogs/bone.png"), Palette.GREY_DARK_62, Palette.GREY_DARK_62.brighter(),5, 2.5, 1, 0.5),
    A(new Identifier("casino:textures/dogs/a.png"), Palette.GREY_DARK_62, Palette.GREY_DARK_62.brighter(),2.50, 1, 0.5, 0.25),
    K(new Identifier("casino:textures/dogs/k.png"), Palette.GREY_DARK_62,Palette.GREY_DARK_62.brighter(), 2.50, 1, 0.5, 0.25),
    Q(new Identifier("casino:textures/dogs/q.png"), Palette.GREY_DARK_62,Palette.GREY_DARK_62.brighter(), 1.5, 1, 0.5, 0.25),
    J(new Identifier("casino:textures/dogs/j.png"), Palette.GREY_DARK_62,Palette.GREY_DARK_62.brighter(),1.5, 1, 0.5, 0.25),
    TEN(new Identifier("casino:textures/dogs/ten.png"), Palette.GREY_DARK_62,Palette.GREY_DARK_62.brighter() ,1.5, 1, 0.5, 0.25);
    private final Identifier texture;
    private final Color color;
    private final Color brighterColor;
    private final double win5;
    private final double win4;
    private final double win3;
    private final double win2;
}
