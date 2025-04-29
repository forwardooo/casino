package dev.forward.casino.engine.elements;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.engine.event.PreTickEvent;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.util.color.Color;
import dev.forward.casino.util.math.V3;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Text
        extends AbstractElement<Text> {
    protected static final Pattern COLOR_PATTERN = Pattern.compile("((?<!&)§([0-9a-fklmnor]))+|(¨[0-9abcdef]{6})");
    protected String[] originalLines;
    protected String[] lines;
    protected boolean shadow;
    protected double lineHeight;
    protected double[] lineWidthCache;
    protected double autoSkip;
    protected String lastLang;
    protected boolean convertAmpCodes;
    private boolean bold;
    private boolean italic;
    private boolean underlined;
    private boolean strikethrough;
    private boolean upperCase;

    public Text() {
        this(" ");
    }

    public Text(String text) {
        this(text == null ? new String[]{"null"} : text.split("\n"));
    }

    public Text(String ... lines) {
        this.shadow = false;
        this.lineHeight = 24.0;
        this.autoSkip = -1.0;
        this.lastLang = "";
        this.convertAmpCodes = true;
        this.bold = false;
        this.italic = false;
        this.underlined = false;
        this.strikethrough = false;
        this.upperCase = false;
        this.setValue(lines);
        this.setColor(new Color(1.0, 1.0, 1.0, 1.0));
        this.registerEvent(PreTickEvent.class, event -> {
            String lang = mc.getLanguageManager().getLanguage().getCode();
            if (!lang.equals(this.lastLang)) {
                this.setLines(this.originalLines);
            }
        });
    }

    public static String convertAmpCodes(String text) {
        return text.replaceAll("(?<!&)&([0-9a-fklmnor])", "§$1").replaceAll("&&", "&").replaceAll("(?<!#)#([0-9a-fA-F]{6})", "¨$1");
    }

    protected void autoSkipLines() {
        ArrayList<String> skippedLines = new ArrayList<String>();
        for (String line : this.lines) {
            skippedLines.addAll(this.autoSkipLine(line));
        }
        this.lines = skippedLines.toArray(new String[0]);
    }

    protected List<String> autoSkipLine(String line) {
        TextRenderer fontRenderer = mc.textRenderer;
        ArrayList<String> skippedLines = new ArrayList<String>();
        double autoSkip = this.autoSkip * this.getScale().getX() * 0.5;
        double width = fontRenderer.getWidth(line);
        if (width > autoSkip) {
            String[] words = line.split("\\s");
            StringBuilder newLine = new StringBuilder();
            for (String word : words) {
                StringBuilder stringBuilder = new StringBuilder();
                if ((double)fontRenderer.getWidth(stringBuilder.append((Object)newLine).append(" ").append(word).toString()) > autoSkip && !newLine.toString().isEmpty()) {
                    skippedLines.add(newLine.toString().trim());
                    newLine = new StringBuilder();
                }
                newLine.append(" ").append(word);
            }
            if (!newLine.toString().isEmpty()) {
                skippedLines.add(newLine.toString().trim());
            }
        } else {
            skippedLines.add(line);
        }
        ArrayList<String> formattedLines = new ArrayList<String>();
        String colorCode = "";
        for (String text : skippedLines) {
            text = colorCode + text;
            formattedLines.add(text);
            Matcher matcher = COLOR_PATTERN.matcher(text);
            while (matcher.find()) {
                colorCode = matcher.group();
            }
        }
        return formattedLines;
    }

    public String getText() {
        return String.join((CharSequence)"\n", this.originalLines);
    }

    public Text setValue(String text) {
        if (text == null) {
            text = "null";
        }
        return this.setValue(text.split("\n"));
    }

    public Text setValue(Collection<String> lines) {
        return this.setLines(lines.toArray(new String[0]));
    }

    public Text setValue(String ... lines) {
        this.originalLines = lines.clone();
        return this.setLines(lines);
    }

    protected Text setLines(String ... newLines) {
        ArrayList<String> linesList = new ArrayList<String>();
        for (String line : newLines) {
            line = I18n.translate(line);
            String stylePrefix = this.getMinecraftFormattingCodes();
            line = stylePrefix + line;
            if (this.convertAmpCodes) {
                line = Text.convertAmpCodes(line);
            }
            if (this.upperCase) {
                line = line.toUpperCase();
            }
            linesList.addAll(Arrays.asList(line.split("\\\\n|\n")));
        }
        this.lines = linesList.toArray(new String[0]);
        if (this.autoSkip != -1.0) {
            this.autoSkipLines();
        }
        this.lineWidthCache = new double[this.lines.length];
        TextRenderer fontRenderer = mc.textRenderer;
        for (int i = 0; i < this.lines.length; ++i) {
            this.lineWidthCache[i] = fontRenderer.getWidth(this.lines[i]);
        }
        this.setSize(new V3(Arrays.stream(this.lineWidthCache).max().getAsDouble() * 2.0, (double)(this.lines.length - 1) * this.lineHeight + 16.0, 0.0));
        this.lastLang = mc.getLanguageManager().getLanguage().getCode();
        this.markDirty();
        return this;
    }

    public String getOriginalText() {
        return String.join((CharSequence)"\n", this.originalLines);
    }

    public Text setShadow(boolean shadow) {
        if (this.shadow == shadow) {
            return this;
        }
        this.shadow = shadow;
        return this;
    }

    public Text setAutoSkip(double autoSkip) {
        if (this.autoSkip == autoSkip) {
            return this;
        }
        this.autoSkip = autoSkip;
        this.setLines(this.originalLines);
        return this;
    }

    public Text setLineHeight(double lineHeight) {
        if (this.lineHeight == lineHeight) {
            return this;
        }
        this.lineHeight = lineHeight;
        this.setLines(this.originalLines);
        return this;
    }

    @Override
    public Text setLastParent(AbstractElement<?> lastParent) {
        if (this.lastParent == lastParent) {
            return this;
        }
        this.lastParent = lastParent;
        this.setLines(this.originalLines);
        return this;
    }

    @Override
    public void render(MatrixStack stack, double partialTicks, double mouseX, double mouseY) {
        if (this.color.getAlpha() * this.getAbsoluteAlpha() <= 0.05) {
            return;
        }
        TextRenderer fontRenderer = mc.textRenderer;
        double absoluteAlpha = this.getAbsoluteAlpha();
        int decimalColor = ((Color)this.color.setAlpha(this.color.getAlpha() * absoluteAlpha)).getDecimal();
        this.color.apply(absoluteAlpha);
        GlStateManager.pushMatrix();
        GlStateManager.scalef(2.0f, 2.0f, 2.0f);
        GlStateManager.translated(0.0, 0.5, 0.0);
        for (int i = 0; i < this.lines.length; ++i) {
            fontRenderer.draw(stack, this.lines[i], (float)((this.getSize().getX() / 2.0 - this.lineWidthCache[i]) * this.origin.getX()), (float)(this.lineHeight / 2.0 * (double)i), decimalColor);
        }
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private String getMinecraftFormattingCodes() {
        String style = "";
        if (this.bold) {
            style = style + "§l";
        }
        if (this.italic) {
            style = style + "§o";
        }
        if (this.underlined) {
            style = style + "§n";
        }
        if (this.strikethrough) {
            style = style + "§m";
        }
        return style;
    }

    @Override
    public Text copy(Text element) {
        super.copy(element);
        element.setAutoSkip(this.getAutoSkip()).setValue(this.getOriginalText()).setShadow(this.isShadow()).setLineHeight(this.getLineHeight());
        return element;
    }

    @Override
    public Text clone() {
        return this.copy(new Text());
    }

    public boolean isShadow() {
        return this.shadow;
    }

    public double getLineHeight() {
        return this.lineHeight;
    }

    public double getAutoSkip() {
        return this.autoSkip;
    }
}