package dev.forward.casino.engine.tooltip;


import dev.forward.casino.util.color.Color;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class TooltipData {
    private final Collection<String> lines;
    private final String title;
    private final Color cornerColor;
    private final double autoSkip;
    private final boolean sizeChangeAnimation;
    private final Color gradientOutlineColor;
    private final boolean tooltipChatScreenOnly;

    public TooltipData(Collection<String> lines, String title) {
        this(lines, title, null, true);
    }

    public TooltipData(Collection<String> lines, String title, Color cornerColor, boolean isAnimation) {
        this(lines, title, cornerColor, -1.0, isAnimation, null, false);
    }

    public boolean isEmpty() {
        return this.title.isEmpty() && (this.lines == null || this.lines.isEmpty());
    }

    public boolean canRenderLine() {
        return !this.title.isEmpty() && this.lines != null && !this.lines.isEmpty();
    }

    public boolean canRenderGradientOutline() {
        return this.gradientOutlineColor != null;
    }

    private static String $default$title() {
        return "";
    }

    private static double $default$autoSkip() {
        return -1.0;
    }

    private static boolean $default$sizeChangeAnimation() {
        return true;
    }

    private static boolean $default$tooltipChatScreenOnly() {
        return false;
    }

    public static TooltipDataBuilder builder() {
        return new TooltipDataBuilder();
    }

    public Collection<String> getLines() {
        return this.lines;
    }

    public String getTitle() {
        return this.title;
    }

    public Color getCornerColor() {
        return this.cornerColor;
    }

    public double getAutoSkip() {
        return this.autoSkip;
    }

    public boolean isSizeChangeAnimation() {
        return this.sizeChangeAnimation;
    }

    public Color getGradientOutlineColor() {
        return this.gradientOutlineColor;
    }

    public boolean isTooltipChatScreenOnly() {
        return this.tooltipChatScreenOnly;
    }

    public TooltipData(Collection<String> lines, String title, Color cornerColor, double autoSkip, boolean sizeChangeAnimation, Color gradientOutlineColor, boolean tooltipChatScreenOnly) {
        this.lines = lines;
        this.title = title;
        this.cornerColor = cornerColor;
        this.autoSkip = autoSkip;
        this.sizeChangeAnimation = sizeChangeAnimation;
        this.gradientOutlineColor = gradientOutlineColor;
        this.tooltipChatScreenOnly = tooltipChatScreenOnly;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TooltipData)) {
            return false;
        }
        TooltipData other = (TooltipData)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (Double.compare(this.getAutoSkip(), other.getAutoSkip()) != 0) {
            return false;
        }
        if (this.isSizeChangeAnimation() != other.isSizeChangeAnimation()) {
            return false;
        }
        if (this.isTooltipChatScreenOnly() != other.isTooltipChatScreenOnly()) {
            return false;
        }
        Collection<String> this$lines = this.getLines();
        Collection<String> other$lines = other.getLines();
        if (this$lines == null ? other$lines != null : !((Object)this$lines).equals(other$lines)) {
            return false;
        }
        String this$title = this.getTitle();
        String other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) {
            return false;
        }
        Color this$cornerColor = this.getCornerColor();
        Color other$cornerColor = other.getCornerColor();
        if (this$cornerColor == null ? other$cornerColor != null : !((Object)this$cornerColor).equals(other$cornerColor)) {
            return false;
        }
        Color this$gradientOutlineColor = this.getGradientOutlineColor();
        Color other$gradientOutlineColor = other.getGradientOutlineColor();
        return !(this$gradientOutlineColor == null ? other$gradientOutlineColor != null : !((Object)this$gradientOutlineColor).equals(other$gradientOutlineColor));
    }

    protected boolean canEqual(Object other) {
        return other instanceof TooltipData;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $autoSkip = Double.doubleToLongBits(this.getAutoSkip());
        result = result * 59 + (int)($autoSkip >>> 32 ^ $autoSkip);
        result = result * 59 + (this.isSizeChangeAnimation() ? 79 : 97);
        result = result * 59 + (this.isTooltipChatScreenOnly() ? 79 : 97);
        Collection<String> $lines = this.getLines();
        result = result * 59 + ($lines == null ? 43 : ((Object)$lines).hashCode());
        String $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        Color $cornerColor = this.getCornerColor();
        result = result * 59 + ($cornerColor == null ? 43 : ((Object)$cornerColor).hashCode());
        Color $gradientOutlineColor = this.getGradientOutlineColor();
        result = result * 59 + ($gradientOutlineColor == null ? 43 : ((Object)$gradientOutlineColor).hashCode());
        return result;
    }

    public static class TooltipDataBuilder {
        private Collection<String> lines;
        private boolean title$set;
        private String title$value;
        private Color cornerColor;
        private boolean autoSkip$set;
        private double autoSkip$value;
        private boolean sizeChangeAnimation$set;
        private boolean sizeChangeAnimation$value;
        private Color gradientOutlineColor;
        private boolean tooltipChatScreenOnly$set;
        private boolean tooltipChatScreenOnly$value;

        public TooltipDataBuilder linesArray(String text) {
            this.lines(new ArrayList<String>(Arrays.asList(text.split("\n"))));
            return this;
        }

        TooltipDataBuilder() {
        }

        public TooltipDataBuilder lines(Collection<String> lines) {
            this.lines = lines;
            return this;
        }
        public TooltipDataBuilder linesAsText(Collection<Text> lines) {
            this.lines = lines.stream().map(Text::asString).collect(Collectors.toList());
            return this;
        }
        public TooltipDataBuilder title(String title) {
            this.title$value = title;
            this.title$set = true;
            return this;
        }

        public TooltipDataBuilder cornerColor(Color cornerColor) {
            this.cornerColor = cornerColor;
            return this;
        }

        public TooltipDataBuilder tooltipChatScreenOnly(boolean tooltipChatScreenOnly) {
            this.tooltipChatScreenOnly$value = tooltipChatScreenOnly;
            this.tooltipChatScreenOnly$set = true;
            return this;
        }

        public TooltipData build() {
            String title$value = this.title$value;
            if (!this.title$set) {
                title$value = TooltipData.$default$title();
            }
            double autoSkip$value = this.autoSkip$value;
            if (!this.autoSkip$set) {
                autoSkip$value = TooltipData.$default$autoSkip();
            }
            boolean sizeChangeAnimation$value = this.sizeChangeAnimation$value;
            if (!this.sizeChangeAnimation$set) {
                sizeChangeAnimation$value = TooltipData.$default$sizeChangeAnimation();
            }
            boolean tooltipChatScreenOnly$value = this.tooltipChatScreenOnly$value;
            if (!this.tooltipChatScreenOnly$set) {
                tooltipChatScreenOnly$value = TooltipData.$default$tooltipChatScreenOnly();
            }
            return new TooltipData(this.lines, title$value, this.cornerColor, autoSkip$value, sizeChangeAnimation$value, this.gradientOutlineColor, tooltipChatScreenOnly$value);
        }

        public String toString() {
            return "TooltipData.TooltipDataBuilder(lines=" + this.lines + ", title$value=" + this.title$value + ", cornerColor=" + this.cornerColor + ", autoSkip$value=" + this.autoSkip$value + ", sizeChangeAnimation$value=" + this.sizeChangeAnimation$value + ", gradientOutlineColor=" + this.gradientOutlineColor + ", tooltipChatScreenOnly$value=" + this.tooltipChatScreenOnly$value + ")";
        }
    }
}
