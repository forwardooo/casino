package dev.forward.casino.util.color;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class ButtonColor {
    public static final Map<String, ButtonColor> COLOR_MAP = new LinkedHashMap<String, ButtonColor>();
    public static final ButtonColor BLUE = ButtonColor.createColor("BLUE", Palette.BLUE, Palette.BLUE_LIGHT, Palette.BLUE_DARK, Palette.BLUE_DARK.alpha(0.62));
    public static final ButtonColor BLUE_28 = ButtonColor.createColor("BLUE_28", Palette.BLUE.alpha(0.28), Palette.BLUE_LIGHT.alpha(0.28), Palette.BLUE_DARK.alpha(0.28), Palette.BLUE_DARK.alpha(0.62));
    public static final ButtonColor BLUE_62 = ButtonColor.createColor("BLUE_62", Palette.BLUE.alpha(0.62), Palette.BLUE_LIGHT.alpha(0.62), Palette.BLUE_DARK.alpha(0.62), Palette.BLUE_DARK.alpha(0.52));
    public static final ButtonColor BLUE_86 = ButtonColor.createColor("BLUE_86", Palette.BLUE.alpha(0.86), Palette.BLUE_LIGHT.alpha(0.86), Palette.BLUE_DARK.alpha(0.86), Palette.BLUE_DARK.alpha(0.62));
    public static final ButtonColor RED = ButtonColor.createColor("RED", Palette.RED, Palette.RED_LIGHT, Palette.RED_MIDDLE, Palette.RED_MIDDLE.alpha(0.62));
    public static final ButtonColor RED_28 = ButtonColor.createColor("RED_28", Palette.RED.alpha(0.28), Palette.RED_LIGHT.alpha(0.28), Palette.RED_MIDDLE.alpha(0.28), Palette.RED_MIDDLE.alpha(0.62));
    public static final ButtonColor RED_62 = ButtonColor.createColor("RED_62", Palette.RED.alpha(0.62), Palette.RED_LIGHT.alpha(0.62), Palette.RED_MIDDLE.alpha(0.62), Palette.RED_MIDDLE.alpha(0.52));
    public static final ButtonColor RED_86 = ButtonColor.createColor("RED_86", Palette.RED.alpha(0.86), Palette.RED_LIGHT.alpha(0.86), Palette.RED_MIDDLE.alpha(0.86), Palette.RED_MIDDLE.alpha(0.62));
    public static final ButtonColor YELLOW = ButtonColor.createColor("YELLOW", Palette.YELLOW, Palette.YELLOW_LIGHT, Palette.YELLOW_MIDDLE, Palette.YELLOW_MIDDLE.alpha(0.62));
    public static final ButtonColor YELLOW_28 = ButtonColor.createColor("YELLOW_28", Palette.YELLOW.alpha(0.28), Palette.YELLOW_LIGHT.alpha(0.28), Palette.YELLOW_MIDDLE.alpha(0.28), Palette.YELLOW_MIDDLE.alpha(0.62));
    public static final ButtonColor YELLOW_62 = ButtonColor.createColor("YELLOW_62", Palette.YELLOW.alpha(0.62), Palette.YELLOW_LIGHT.alpha(0.62), Palette.YELLOW_MIDDLE.alpha(0.62), Palette.YELLOW_MIDDLE.alpha(0.52));
    public static final ButtonColor YELLOW_86 = ButtonColor.createColor("YELLOW_86", Palette.YELLOW.alpha(0.86), Palette.YELLOW_LIGHT.alpha(0.86), Palette.YELLOW_MIDDLE.alpha(0.86), Palette.YELLOW_MIDDLE.alpha(0.62));
    public static final ButtonColor GREEN = ButtonColor.createColor("GREEN", Palette.GREEN, Palette.GREEN_LIGHT, Palette.GREEN_MIDDLE, Palette.GREEN_MIDDLE.alpha(0.62));
    public static final ButtonColor GREEN_28 = ButtonColor.createColor("GREEN_28", Palette.GREEN.alpha(0.28), Palette.GREEN_LIGHT.alpha(0.28), Palette.GREEN_MIDDLE.alpha(0.28), Palette.GREEN_MIDDLE.alpha(0.62));
    public static final ButtonColor GREEN_62 = ButtonColor.createColor("GREEN_62", Palette.GREEN.alpha(0.62), Palette.GREEN_LIGHT.alpha(0.62), Palette.GREEN_MIDDLE.alpha(0.62), Palette.GREEN_MIDDLE.alpha(0.52));
    public static final ButtonColor GREEN_86 = ButtonColor.createColor("GREEN_86", Palette.GREEN.alpha(0.86), Palette.GREEN_LIGHT.alpha(0.86), Palette.GREEN_MIDDLE.alpha(0.86), Palette.GREEN_MIDDLE.alpha(0.62));
    public static final ButtonColor ORANGE = ButtonColor.createColor("ORANGE", Palette.ORANGE, Palette.ORANGE_LIGHT, Palette.ORANGE_MIDDLE, Palette.ORANGE_MIDDLE.alpha(0.62));
    public static final ButtonColor ORANGE_28 = ButtonColor.createColor("ORANGE_28", Palette.ORANGE.alpha(0.28), Palette.ORANGE_LIGHT.alpha(0.28), Palette.ORANGE_MIDDLE.alpha(0.28), Palette.ORANGE_MIDDLE.alpha(0.62));
    public static final ButtonColor ORANGE_62 = ButtonColor.createColor("ORANGE_62", Palette.ORANGE.alpha(0.62), Palette.ORANGE_LIGHT.alpha(0.62), Palette.ORANGE_MIDDLE.alpha(0.62), Palette.ORANGE_MIDDLE.alpha(0.52));
    public static final ButtonColor ORANGE_86 = ButtonColor.createColor("ORANGE_86", Palette.ORANGE.alpha(0.86), Palette.ORANGE_LIGHT.alpha(0.86), Palette.ORANGE_MIDDLE.alpha(0.86), Palette.ORANGE_MIDDLE.alpha(0.62));
    public static final ButtonColor PURPLE = ButtonColor.createColor("PURPLE", Palette.PURPLE, Palette.PURPLE_LIGHT, Palette.PURPLE_MIDDLE, Palette.PURPLE_MIDDLE.alpha(0.62));
    public static final ButtonColor PURPLE_28 = ButtonColor.createColor("PURPLE_28", Palette.PURPLE.alpha(0.28), Palette.PURPLE_LIGHT.alpha(0.28), Palette.PURPLE_MIDDLE.alpha(0.28), Palette.PURPLE_MIDDLE.alpha(0.62));
    public static final ButtonColor PURPLE_62 = ButtonColor.createColor("PURPLE_62", Palette.PURPLE.alpha(0.62), Palette.PURPLE_LIGHT.alpha(0.62), Palette.PURPLE_MIDDLE.alpha(0.62), Palette.PURPLE_MIDDLE.alpha(0.52));
    public static final ButtonColor PURPLE_86 = ButtonColor.createColor("PURPLE_86", Palette.PURPLE.alpha(0.86), Palette.PURPLE_LIGHT.alpha(0.86), Palette.PURPLE_MIDDLE.alpha(0.86), Palette.PURPLE_MIDDLE.alpha(0.62));
    public static final ButtonColor PINK = ButtonColor.createColor("PINK", Palette.PINK, Palette.PINK_LIGHT, Palette.PINK_MIDDLE, Palette.PINK_MIDDLE.alpha(0.62));
    public static final ButtonColor PINK_28 = ButtonColor.createColor("PINK_28", Palette.PINK.alpha(0.28), Palette.PINK_LIGHT.alpha(0.28), Palette.PINK_MIDDLE.alpha(0.28), Palette.PINK_MIDDLE.alpha(0.62));
    public static final ButtonColor PINK_62 = ButtonColor.createColor("PINK_62", Palette.PINK.alpha(0.62), Palette.PINK_LIGHT.alpha(0.62), Palette.PINK_MIDDLE.alpha(0.62), Palette.PINK_MIDDLE.alpha(0.52));
    public static final ButtonColor PINK_86 = ButtonColor.createColor("PINK_86", Palette.PINK.alpha(0.86), Palette.PINK_LIGHT.alpha(0.86), Palette.PINK_MIDDLE.alpha(0.86), Palette.PINK_MIDDLE.alpha(0.62));
    public static final ButtonColor CYAN = ButtonColor.createColor("CYAN", Palette.CYAN, Palette.CYAN_LIGHT, Palette.CYAN_MIDDLE, Palette.CYAN_MIDDLE.alpha(0.62));
    public static final ButtonColor CYAN_28 = ButtonColor.createColor("CYAN_28", Palette.CYAN.alpha(0.28), Palette.CYAN_LIGHT.alpha(0.28), Palette.CYAN_MIDDLE.alpha(0.28), Palette.CYAN_MIDDLE.alpha(0.62));
    public static final ButtonColor CYAN_62 = ButtonColor.createColor("CYAN_62", Palette.CYAN.alpha(0.62), Palette.CYAN_LIGHT.alpha(0.62), Palette.CYAN_MIDDLE.alpha(0.62), Palette.CYAN_MIDDLE.alpha(0.52));
    public static final ButtonColor CYAN_86 = ButtonColor.createColor("CYAN_86", Palette.CYAN.alpha(0.86), Palette.CYAN_LIGHT.alpha(0.86), Palette.CYAN_MIDDLE.alpha(0.86), Palette.CYAN_MIDDLE.alpha(0.62));
    public static final ButtonColor GREY = ButtonColor.createColor("GREY", Palette.GREY_DARK, Palette.GREY, Palette.GREY_MIDDLE, Palette.GREY_MIDDLE.alpha(0.62));
    public static final ButtonColor GREY_28 = ButtonColor.createColor("GREY_28", Palette.GREY_DARK.alpha(0.28), Palette.GREY.alpha(0.28), Palette.GREY_MIDDLE.alpha(0.28), Palette.GREY_MIDDLE.alpha(0.62));
    public static final ButtonColor GREY_62 = ButtonColor.createColor("GREY_62", Palette.GREY_DARK.alpha(0.62), Palette.GREY.alpha(0.62), Palette.GREY_MIDDLE.alpha(0.62), Palette.GREY_MIDDLE.alpha(0.52));
    public static final ButtonColor GREY_86 = ButtonColor.createColor("GREY_86", Palette.GREY_DARK.alpha(0.86), Palette.GREY.alpha(0.86), Palette.GREY_MIDDLE.alpha(0.86), Palette.GREY_MIDDLE.alpha(0.62));
    private final Color defaultColor;
    private final Color hoverColor;
    private final Color activeColor;
    private final Color disableColor;

    public static ButtonColor createColor(String name, Color defaultColor, Color hoverColor, Color activeColor, Color disableColor) {
        ButtonColor buttonColor = new ButtonColor(defaultColor, hoverColor, activeColor, disableColor);
        COLOR_MAP.put(name, buttonColor);
        return buttonColor;
    }

    public ButtonColor(Color defaultColor, Color hoverColor, Color activeColor, Color disableColor) {
        this.defaultColor = defaultColor;
        this.hoverColor = hoverColor;
        this.activeColor = activeColor;
        this.disableColor = disableColor;
    }
    public ButtonColor darker() {
        return new ButtonColor(this.defaultColor.darker(), this.hoverColor.darker(), this.activeColor.darker(), this.disableColor.darker());
    }
    public ButtonColor brighter() {
        return new ButtonColor(this.defaultColor.brighter(), this.hoverColor.brighter(), this.activeColor.brighter(), this.disableColor.brighter());
    }
}