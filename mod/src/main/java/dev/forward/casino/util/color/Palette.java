package dev.forward.casino.util.color;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;
public class Palette {
    private static final Map<String, PaletteData> PALETTE_DATA_MAP = new LinkedHashMap<String, PaletteData>();
    public static final Color WHITE = Palette.addPaletteColor("WHITE", "#FFFFFF", "#FFFFFF", "#FFFFFF");
    public static final Color WHITE_86 = Palette.addPaletteColor("WHITE_86", "#FFFFFF", "#FFFFFF", "#FFFFFF", 0.86);
    public static final Color WHITE_62 = Palette.addPaletteColor("WHITE_62", "#FFFFFF", "#FFFFFF", "#FFFFFF", 0.62);
    public static final Color WHITE_28 = Palette.addPaletteColor("WHITE_28", "#FFFFFF", "#FFFFFF", "#FFFFFF", 0.28);
    public static final Color WHITE_0 = Palette.addPaletteColor("WHITE_0", "#FFFFFF", "#FFFFFF", "#FFFFFF", 0.0);
    public static final Color BLACK = Palette.addPaletteColor("BLACK", "#000000", "#707070", "#707070");
    public static final Color BLACK_86 = Palette.addPaletteColor("BLACK_86", "#000000", "#707070", "#707070", 0.86);
    public static final Color BLACK_62 = Palette.addPaletteColor("BLACK_62", "#000000", "#707070", "#707070", 0.62);
    public static final Color BLACK_28 = Palette.addPaletteColor("BLACK_28", "#000000", "#707070", "#707070", 0.28);
    public static final Color BLUE_LIGHT = Palette.addPaletteColor("BLUE_LIGHT", "#4A8CEC", "#77ABF8", "#4A8CEC");
    public static final Color BLUE_LIGHT_86 = Palette.addPaletteColor("BLUE_LIGHT_86", "#4A8CEC", "#77ABF8", "#4A8CEC", 0.86);
    public static final Color BLUE_LIGHT_62 = Palette.addPaletteColor("BLUE_LIGHT_62", "#4A8CEC", "#77ABF8", "#4A8CEC", 0.62);
    public static final Color BLUE_LIGHT_28 = Palette.addPaletteColor("BLUE_LIGHT_28", "#4A8CEC", "#77ABF8", "#4A8CEC", 0.28);
    public static final Color BLUE = Palette.addPaletteColor("BLUE", "#2A66BD", "#4A8CEC", "#4A8CEC");
    public static final Color BLUE_86 = Palette.addPaletteColor("BLUE_86", "#2A66BD", "#4A8CEC", "#4A8CEC", 0.86);
    public static final Color BLUE_62 = Palette.addPaletteColor("BLUE_62", "#2A66BD", "#4A8CEC", "#4A8CEC", 0.62);
    public static final Color BLUE_28 = Palette.addPaletteColor("BLUE_28", "#2A66BD", "#4A8CEC", "#4A8CEC", 0.28);
    public static final Color BLUE_MIDDLE = Palette.addPaletteColor("BLUE_MIDDLE", "#275495", "#2A66BD", "#4A8CEC");
    public static final Color BLUE_MIDDLE_86 = Palette.addPaletteColor("BLUE_MIDDLE_86", "#275495", "#2A66BD", "#4A8CEC", 0.86);
    public static final Color BLUE_MIDDLE_62 = Palette.addPaletteColor("BLUE_MIDDLE_62", "#275495", "#2A66BD", "#4A8CEC", 0.62);
    public static final Color BLUE_MIDDLE_28 = Palette.addPaletteColor("BLUE_MIDDLE_28", "#275495", "#2A66BD", "#4A8CEC", 0.28);
    public static final Color BLUE_DARK = Palette.addPaletteColor("BLUE_DARK", "#153562", "#275495", "#4A8CEC");
    public static final Color BLUE_DARK_86 = Palette.addPaletteColor("BLUE_DARK_86", "#153562", "#275495", "#4A8CEC", 0.86);
    public static final Color BLUE_DARK_62 = Palette.addPaletteColor("BLUE_DARK_62", "#153562", "#275495", "#4A8CEC", 0.62);
    public static final Color BLUE_DARK_28 = Palette.addPaletteColor("BLUE_DARK_28", "#153562", "#275495", "#4A8CEC", 0.28);
    public static final Color RED_LIGHT = Palette.addPaletteColor("RED_LIGHT", "#E73D4B", "#F35C69", "#E73D4B");
    public static final Color RED_LIGHT_86 = Palette.addPaletteColor("RED_LIGHT_86", "#E73D4B", "#F35C69", "#E73D4B", 0.86);
    public static final Color RED_LIGHT_62 = Palette.addPaletteColor("RED_LIGHT_62", "#E73D4B", "#F35C69", "#E73D4B", 0.62);
    public static final Color RED_LIGHT_28 = Palette.addPaletteColor("RED_LIGHT_28", "#E73D4B", "#F35C69", "#E73D4B", 0.28);
    public static final Color RED = Palette.addPaletteColor("RED", "#A91925", "#E73D4B", "#E73D4B");
    public static final Color RED_86 = Palette.addPaletteColor("RED_86", "#A91925", "#E73D4B", "#E73D4B", 0.86);
    public static final Color RED_62 = Palette.addPaletteColor("RED_62", "#A91925", "#E73D4B", "#E73D4B", 0.62);
    public static final Color RED_28 = Palette.addPaletteColor("RED_28", "#A91925", "#E73D4B", "#E73D4B", 0.28);
    public static final Color RED_MIDDLE = Palette.addPaletteColor("RED_MIDDLE", "#66131B", "#A91925", "#E73D4B");
    public static final Color RED_MIDDLE_86 = Palette.addPaletteColor("RED_MIDDLE_86", "#66131B", "#A91925", "#E73D4B", 0.86);
    public static final Color RED_MIDDLE_62 = Palette.addPaletteColor("RED_MIDDLE_62", "#66131B", "#A91925", "#E73D4B", 0.62);
    public static final Color RED_MIDDLE_28 = Palette.addPaletteColor("RED_MIDDLE_28", "#66131B", "#A91925", "#E73D4B", 0.28);
    public static final Color RED_DARK = Palette.addPaletteColor("RED_DARK", "#4A0F15", "#66131B", "#E73D4B");
    public static final Color RED_DARK_86 = Palette.addPaletteColor("RED_DARK_86", "#4A0F15", "#66131B", "#E73D4B", 0.86);
    public static final Color RED_DARK_62 = Palette.addPaletteColor("RED_DARK_62", "#4A0F15", "#66131B", "#E73D4B", 0.62);
    public static final Color RED_DARK_28 = Palette.addPaletteColor("RED_DARK_28", "#4A0F15", "#66131B", "#E73D4B", 0.28);
    public static final Color YELLOW_LIGHT = Palette.addPaletteColor("YELLOW_LIGHT", "#FFCA42", "#FFDA7B", "#FFCA42");
    public static final Color YELLOW_LIGHT_86 = Palette.addPaletteColor("YELLOW_LIGHT_86", "#FFCA42", "#FFDA7B", "#FFCA42", 0.86);
    public static final Color YELLOW_LIGHT_62 = Palette.addPaletteColor("YELLOW_LIGHT_62", "#FFCA42", "#FFDA7B", "#FFCA42", 0.62);
    public static final Color YELLOW_LIGHT_28 = Palette.addPaletteColor("YELLOW_LIGHT_28", "#FFCA42", "#FFDA7B", "#FFCA42", 0.28);
    public static final Color YELLOW = Palette.addPaletteColor("YELLOW", "#EFAC00", "#FFCA42", "#FFCA42");
    public static final Color YELLOW_86 = Palette.addPaletteColor("YELLOW_86", "#EFAC00", "#FFCA42", "#FFCA42", 0.86);
    public static final Color YELLOW_62 = Palette.addPaletteColor("YELLOW_62", "#EFAC00", "#FFCA42", "#FFCA42", 0.62);
    public static final Color YELLOW_28 = Palette.addPaletteColor("YELLOW_28", "#EFAC00", "#FFCA42", "#FFCA42", 0.28);
    public static final Color YELLOW_MIDDLE = Palette.addPaletteColor("YELLOW_MIDDLE", "#8D6808", "#EFAC00", "#FFCA42");
    public static final Color YELLOW_MIDDLE_86 = Palette.addPaletteColor("YELLOW_MIDDLE_86", "#8D6808", "#EFAC00", "#FFCA42", 0.86);
    public static final Color YELLOW_MIDDLE_62 = Palette.addPaletteColor("YELLOW_MIDDLE_62", "#8D6808", "#EFAC00", "#FFCA42", 0.62);
    public static final Color YELLOW_MIDDLE_28 = Palette.addPaletteColor("YELLOW_MIDDLE_28", "#8D6808", "#EFAC00", "#FFCA42", 0.28);
    public static final Color YELLOW_DARK = Palette.addPaletteColor("YELLOW_DARK", "#553F06", "#8D6808", "#FFCA42");
    public static final Color YELLOW_DARK_86 = Palette.addPaletteColor("YELLOW_DARK_86", "#553F06", "#8D6808", "#FFCA42", 0.86);
    public static final Color YELLOW_DARK_62 = Palette.addPaletteColor("YELLOW_DARK_62", "#553F06", "#8D6808", "#FFCA42", 0.62);
    public static final Color YELLOW_DARK_28 = Palette.addPaletteColor("YELLOW_DARK_28", "#553F06", "#8D6808", "#FFCA42", 0.28);
    public static final Color GREEN_LIGHT = Palette.addPaletteColor("GREEN_LIGHT", "#49DF74", "#69F290", "#49DF74");
    public static final Color GREEN_LIGHT_86 = Palette.addPaletteColor("GREEN_LIGHT_86", "#49DF74", "#69F290", "#49DF74", 0.86);
    public static final Color GREEN_LIGHT_62 = Palette.addPaletteColor("GREEN_LIGHT_62", "#49DF74", "#69F290", "#49DF74", 0.62);
    public static final Color GREEN_LIGHT_28 = Palette.addPaletteColor("GREEN_LIGHT_28", "#49DF74", "#69F290", "#49DF74", 0.28);
    public static final Color GREEN = Palette.addPaletteColor("GREEN", "#22AE49", "#49DF74", "#49DF74");
    public static final Color GREEN_86 = Palette.addPaletteColor("GREEN_86", "#22AE49", "#49DF74", "#49DF74", 0.86);
    public static final Color GREEN_62 = Palette.addPaletteColor("GREEN_62", "#22AE49", "#49DF74", "#49DF74", 0.62);
    public static final Color GREEN_28 = Palette.addPaletteColor("GREEN_28", "#22AE49", "#49DF74", "#49DF74", 0.28);
    public static final Color GREEN_MIDDLE = Palette.addPaletteColor("GREEN_MIDDLE", "#146229", "#22AE49", "#49DF74");
    public static final Color GREEN_MIDDLE_86 = Palette.addPaletteColor("GREEN_MIDDLE_86", "#146229", "#22AE49", "#49DF74", 0.86);
    public static final Color GREEN_MIDDLE_62 = Palette.addPaletteColor("GREEN_MIDDLE_62", "#146229", "#22AE49", "#49DF74", 0.62);
    public static final Color GREEN_MIDDLE_28 = Palette.addPaletteColor("GREEN_MIDDLE_28", "#146229", "#22AE49", "#49DF74", 0.28);
    public static final Color GREEN_DARK = Palette.addPaletteColor("GREEN_DARK", "#044816", "#146229", "#49DF74");
    public static final Color GREEN_DARK_86 = Palette.addPaletteColor("GREEN_DARK_86", "#0F4A1A", "#146229", "#49DF74", 0.86);
    public static final Color GREEN_DARK_62 = Palette.addPaletteColor("GREEN_DARK_62", "#0F4A1A", "#146229", "#49DF74", 0.62);
    public static final Color GREEN_DARK_28 = Palette.addPaletteColor("GREEN_DARK_28", "#0F4A1A", "#146229", "#49DF74", 0.28);
    public static final Color ORANGE_LIGHT = Palette.addPaletteColor("ORANGE_LIGHT", "#FF9D42", "#FFB36D", "#FF9D42");
    public static final Color ORANGE_LIGHT_86 = Palette.addPaletteColor("ORANGE_LIGHT_86", "#FF9D42", "#FFB36D", "#FF9D42", 0.86);
    public static final Color ORANGE_LIGHT_62 = Palette.addPaletteColor("ORANGE_LIGHT_62", "#FF9D42", "#FFB36D", "#FF9D42", 0.62);
    public static final Color ORANGE_LIGHT_28 = Palette.addPaletteColor("ORANGE_LIGHT_28", "#FF9D42", "#FFB36D", "#FF9D42", 0.28);
    public static final Color ORANGE = Palette.addPaletteColor("ORANGE", "#E07614", "#FF9D42", "#FF9D42");
    public static final Color ORANGE_86 = Palette.addPaletteColor("ORANGE_86", "#FF7F00", "#FF9D42", "#FF9D42", 0.86);
    public static final Color ORANGE_62 = Palette.addPaletteColor("ORANGE_62", "#FF7F00", "#FF9D42", "#FF9D42", 0.62);
    public static final Color ORANGE_28 = Palette.addPaletteColor("ORANGE_28", "#FF7F00", "#FF9D42", "#FF9D42", 0.28);
    public static final Color ORANGE_MIDDLE = Palette.addPaletteColor("ORANGE_MIDDLE", "#964A09", "#E07614", "#FF9D42");
    public static final Color ORANGE_MIDDLE_86 = Palette.addPaletteColor("ORANGE_MIDDLE_86", "#F57C00", "#FF7F00", "#FF9D42", 0.86);
    public static final Color ORANGE_MIDDLE_62 = Palette.addPaletteColor("ORANGE_MIDDLE_62", "#F57C00", "#FF7F00", "#FF9D42", 0.62);
    public static final Color ORANGE_MIDDLE_28 = Palette.addPaletteColor("ORANGE_MIDDLE_28", "#F57C00", "#FF7F00", "#FF9D42", 0.28);
    public static final Color ORANGE_DARK = Palette.addPaletteColor("ORANGE_DARK", "#5C2B01", "#964A09", "#FF9D42");
    public static final Color ORANGE_DARK_86 = Palette.addPaletteColor("ORANGE_DARK_86", "#E56A00", "#F57C00", "#FF9D42", 0.86);
    public static final Color ORANGE_DARK_62 = Palette.addPaletteColor("ORANGE_DARK_62", "#E56A00", "#F57C00", "#FF9D42", 0.62);
    public static final Color ORANGE_DARK_28 = Palette.addPaletteColor("ORANGE_DARK_28", "#E56A00", "#F57C00", "#FF9D42", 0.28);
    public static final Color PURPLE_LIGHT = Palette.addPaletteColor("PURPLE_LIGHT", "#7E4AEC", "#9669F4", "#7E4AEC");
    public static final Color PURPLE_LIGHT_86 = Palette.addPaletteColor("PURPLE_LIGHT_86", "#7E4AEC", "#9669F4", "#7E4AEC", 0.86);
    public static final Color PURPLE_LIGHT_62 = Palette.addPaletteColor("PURPLE_LIGHT_62", "#7E4AEC", "#9669F4", "#7E4AEC", 0.62);
    public static final Color PURPLE_LIGHT_28 = Palette.addPaletteColor("PURPLE_LIGHT_28", "#7E4AEC", "#9669F4", "#7E4AEC", 0.28);
    public static final Color PURPLE = Palette.addPaletteColor("PURPLE", "#6826F5", "#7E4AEC", "#7E4AEC");
    public static final Color PURPLE_86 = Palette.addPaletteColor("PURPLE_86", "#5A2CC9", "#7E4AEC", "#7E4AEC", 0.86);
    public static final Color PURPLE_62 = Palette.addPaletteColor("PURPLE_62", "#5A2CC9", "#7E4AEC", "#7E4AEC", 0.62);
    public static final Color PURPLE_28 = Palette.addPaletteColor("PURPLE_28", "#5A2CC9", "#7E4AEC", "#7E4AEC", 0.28);
    public static final Color PURPLE_MIDDLE = Palette.addPaletteColor("PURPLE_MIDDLE", "#390A9E", "#6826F5", "#7E4AEC");
    public static final Color PURPLE_MIDDLE_86 = Palette.addPaletteColor("PURPLE_MIDDLE_86", "#4B1C9F", "#5A2CC9", "#7E4AEC", 0.86);
    public static final Color PURPLE_MIDDLE_62 = Palette.addPaletteColor("PURPLE_MIDDLE_62", "#4B1C9F", "#5A2CC9", "#7E4AEC", 0.62);
    public static final Color PURPLE_MIDDLE_28 = Palette.addPaletteColor("PURPLE_MIDDLE_28", "#4B1C9F", "#5A2CC9", "#7E4AEC", 0.28);
    public static final Color PURPLE_DARK = Palette.addPaletteColor("PURPLE_DARK", "#1E0A4A", "#390A9E", "#7E4AEC");
    public static final Color PURPLE_DARK_86 = Palette.addPaletteColor("PURPLE_DARK_86", "#3D0F76", "#4B1C9F", "#7E4AEC", 0.86);
    public static final Color PURPLE_DARK_62 = Palette.addPaletteColor("PURPLE_DARK_62", "#3D0F76", "#4B1C9F", "#7E4AEC", 0.62);
    public static final Color PURPLE_DARK_28 = Palette.addPaletteColor("PURPLE_DARK_28", "#3D0F76", "#4B1C9F", "#7E4AEC", 0.28);
    public static final Color PINK_LIGHT = Palette.addPaletteColor("PINK_LIGHT", "#F062C0", "#FF86D6", "#F062C0");
    public static final Color PINK_LIGHT_86 = Palette.addPaletteColor("PINK_LIGHT_86", "#F062C0", "#FF86D6", "#F062C0", 0.86);
    public static final Color PINK_LIGHT_62 = Palette.addPaletteColor("PINK_LIGHT_62", "#F062C0", "#FF86D6", "#F062C0", 0.62);
    public static final Color PINK_LIGHT_28 = Palette.addPaletteColor("PINK_LIGHT_28", "#F062C0", "#FF86D6", "#F062C0", 0.28);
    public static final Color PINK = Palette.addPaletteColor("PINK", "#ED1FA7", "#F062C0", "#F062C0");
    public static final Color PINK_86 = Palette.addPaletteColor("PINK_86", "#D61C8C", "#F062C0", "#F062C0", 0.86);
    public static final Color PINK_62 = Palette.addPaletteColor("PINK_62", "#D61C8C", "#F062C0", "#F062C0", 0.62);
    public static final Color PINK_28 = Palette.addPaletteColor("PINK_28", "#D61C8C", "#F062C0", "#F062C0", 0.28);
    public static final Color PINK_MIDDLE = Palette.addPaletteColor("PINK_MIDDLE", "#880A5D", "#ED1FA7", "#F062C0");
    public static final Color PINK_MIDDLE_86 = Palette.addPaletteColor("PINK_MIDDLE_86", "#C11676", "#D61C8C", "#F062C0", 0.86);
    public static final Color PINK_MIDDLE_62 = Palette.addPaletteColor("PINK_MIDDLE_62", "#C11676", "#D61C8C", "#F062C0", 0.62);
    public static final Color PINK_MIDDLE_28 = Palette.addPaletteColor("PINK_MIDDLE_28", "#C11676", "#D61C8C", "#F062C0", 0.28);
    public static final Color PINK_DARK = Palette.addPaletteColor("PINK_DARK", "#530F3C", "#880A5D", "#F062C0");
    public static final Color PINK_DARK_86 = Palette.addPaletteColor("PINK_DARK_86", "#9C0F58", "#C11676", "#F062C0", 0.86);
    public static final Color PINK_DARK_62 = Palette.addPaletteColor("PINK_DARK_62", "#9C0F58", "#C11676", "#F062C0", 0.62);
    public static final Color PINK_DARK_28 = Palette.addPaletteColor("PINK_DARK_28", "#9C0F58", "#C11676", "#F062C0", 0.28);
    public static final Color CYAN_LIGHT = Palette.addPaletteColor("CYAN_LIGHT", "#18D4D4", "#37EFEF", "#18D4D4");
    public static final Color CYAN_LIGHT_86 = Palette.addPaletteColor("CYAN_LIGHT_86", "#18D4D4", "#37EFEF", "#18D4D4", 0.86);
    public static final Color CYAN_LIGHT_62 = Palette.addPaletteColor("CYAN_LIGHT_62", "#18D4D4", "#37EFEF", "#18D4D4", 0.62);
    public static final Color CYAN_LIGHT_28 = Palette.addPaletteColor("CYAN_LIGHT_28", "#18D4D4", "#37EFEF", "#18D4D4", 0.28);
    public static final Color CYAN = Palette.addPaletteColor("CYAN", "#03BCBC", "#18D4D4", "#18D4D4");
    public static final Color CYAN_86 = Palette.addPaletteColor("CYAN_86", "#00A0A0", "#18D4D4", "#18D4D4", 0.86);
    public static final Color CYAN_62 = Palette.addPaletteColor("CYAN_62", "#00A0A0", "#18D4D4", "#18D4D4", 0.62);
    public static final Color CYAN_28 = Palette.addPaletteColor("CYAN_28", "#00A0A0", "#18D4D4", "#18D4D4", 0.28);
    public static final Color CYAN_MIDDLE = Palette.addPaletteColor("CYAN_MIDDLE", "#065F5F", "#03BCBC", "#18D4D4");
    public static final Color CYAN_MIDDLE_86 = Palette.addPaletteColor("CYAN_MIDDLE_86", "#009E9E", "#00A0A0", "#18D4D4", 0.86);
    public static final Color CYAN_MIDDLE_62 = Palette.addPaletteColor("CYAN_MIDDLE_62", "#009E9E", "#00A0A0", "#18D4D4", 0.62);
    public static final Color CYAN_MIDDLE_28 = Palette.addPaletteColor("CYAN_MIDDLE_28", "#009E9E", "#00A0A0", "#18D4D4", 0.28);
    public static final Color CYAN_DARK = Palette.addPaletteColor("CYAN_DARK", "#083F3F", "#065F5F", "#18D4D4");
    public static final Color CYAN_DARK_86 = Palette.addPaletteColor("CYAN_DARK_86", "#007D7D", "#009E9E", "#18D4D4", 0.86);
    public static final Color CYAN_DARK_62 = Palette.addPaletteColor("CYAN_DARK_62", "#007D7D", "#009E9E", "#18D4D4", 0.62);
    public static final Color CYAN_DARK_28 = Palette.addPaletteColor("CYAN_DARK_28", "#007D7D", "#009E9E", "#18D4D4", 0.28);
    public static final Color GREY_LIGHT = Palette.addPaletteColor("GREY_LIGHT", "#A8A8A8", "#BEBEBE", "#A8A8A8");
    public static final Color GREY_LIGHT_86 = Palette.addPaletteColor("GREY_LIGHT_86", "#A8A8A8", "#BEBEBE", "#A8A8A8", 0.86);
    public static final Color GREY_LIGHT_62 = Palette.addPaletteColor("GREY_LIGHT_62", "#A8A8A8", "#BEBEBE", "#A8A8A8", 0.62);
    public static final Color GREY_LIGHT_28 = Palette.addPaletteColor("GREY_LIGHT_28", "#A8A8A8", "#BEBEBE", "#A8A8A8", 0.28);
    public static final Color GREY = Palette.addPaletteColor("GREY", "#707070", "#A8A8A8", "#A8A8A8");
    public static final Color GREY_86 = Palette.addPaletteColor("GREY_86", "#707070", "#A8A8A8", "#A8A8A8", 0.86);
    public static final Color GREY_62 = Palette.addPaletteColor("GREY_62", "#707070", "#A8A8A8", "#A8A8A8", 0.62);
    public static final Color GREY_28 = Palette.addPaletteColor("GREY_28", "#707070", "#A8A8A8", "#A8A8A8", 0.28);
    public static final Color GREY_MIDDLE = Palette.addPaletteColor("GREY_MIDDLE", "#4B4B4B", "#707070", "#A8A8A8");
    public static final Color GREY_MIDDLE_86 = Palette.addPaletteColor("GREY_MIDDLE_86", "#4B4B4B", "#707070", "#A8A8A8", 0.86);
    public static final Color GREY_MIDDLE_62 = Palette.addPaletteColor("GREY_MIDDLE_62", "#4B4B4B", "#707070", "#A8A8A8", 0.62);
    public static final Color GREY_MIDDLE_28 = Palette.addPaletteColor("GREY_MIDDLE_28", "#4B4B4B", "#707070", "#A8A8A8", 0.28);
    public static final Color GREY_DARK = Palette.addPaletteColor("GREY_DARK", "#363636", "#4B4B4B", "#A8A8A8");
    public static final Color GREY_DARK_86 = Palette.addPaletteColor("GREY_DARK_86", "#363636", "#4B4B4B", "#A8A8A8", 0.86);
    public static final Color GREY_DARK_62 = Palette.addPaletteColor("GREY_DARK_62", "#363636", "#4B4B4B", "#A8A8A8", 0.62);
    public static final Color GREY_DARK_28 = Palette.addPaletteColor("GREY_DARK_28", "#363636", "#4B4B4B", "#A8A8A8", 0.28);
    public static final Color WORLD_GREY = Palette.addPaletteColor("WORLD_GREY", "#1E1E1E", "#363636", "#363636");

    private static Color addPaletteColor(String name, String color, String brighterColor, String hoverColor) {
        PaletteData paletteData = new PaletteData(name.toLowerCase(), new Color(color), new Color(brighterColor), new Color(hoverColor));
        PALETTE_DATA_MAP.put(name.toLowerCase(), paletteData);
        return paletteData.getColor();
    }

    private static Color addPaletteColor(String name, String color, String brighterColor, String hoverColor, double alpha) {
        PaletteData paletteData = new PaletteData(name.toLowerCase(), new Color(color).alpha(alpha), new Color(brighterColor).alpha(alpha), new Color(hoverColor).alpha(alpha));
        PALETTE_DATA_MAP.put(name.toLowerCase(), paletteData);
        return paletteData.getColor();
    }

    public static PaletteData valueOf(String name) {
        return PALETTE_DATA_MAP.get(name.toLowerCase());
    }
    public static PaletteData getPaletteByColor(Color color) {
        for (PaletteData paletteData : PALETTE_DATA_MAP.values()) {
            Color c = paletteData.getColor();
            if (c.getIntR() != color.getIntR() || c.getIntG() != color.getIntG() || c.getIntB() != color.getIntB()) continue;
            return paletteData;
        }
        return null;
    }

    public static Color getBrighterColor(Color color) {
        if (color == null) {
            return null;
        }
        if (color.getR() == 0.0 && color.getG() == 0.0 && color.getB() == 0.0) {
            return GREY.alpha(0.28);
        }
        PaletteData paletteData = Palette.getPaletteByColor(color);
        if (paletteData != null && paletteData == Palette.valueOf("GREY_DARK")) {
            return GREY.alpha(0.28);
        }
        if (color.getAlpha() < 1.0) {
            return color;
        }
        if (paletteData != null) {
            return paletteData.getBrighterColor();
        }
        return new Color(color.getR() * 1.2, color.getG() * 1.2, color.getB() * 1.2, color.getAlpha());
    }

    public static Color getHoverColor(Color color) {
        if (color == null) {
            return null;
        }
        PaletteData paletteData = Palette.getPaletteByColor(color);
        if (paletteData != null) {
            return paletteData.getHoverColor();
        }
        return new Color(color.getR() * 1.4, color.getG() * 1.4, color.getB() * 1.4, color.getAlpha());
    }

    @Getter
    public static class PaletteData {
        private final String name;
        private final Color color;
        private final Color brighterColor;
        private final Color hoverColor;

        public PaletteData(String name, Color color, Color brighterColor, Color hoverColor) {
            this.name = name;
            this.color = color;
            this.brighterColor = brighterColor;
            this.hoverColor = hoverColor;
        }
    }
}
