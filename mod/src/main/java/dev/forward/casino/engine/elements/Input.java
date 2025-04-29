//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.forward.casino.engine.elements;

import com.mojang.blaze3d.platform.GlStateManager;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.forward.casino.engine.event.*;
import dev.forward.casino.mixins.accessors.KeyboardAccessor;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.engine.animation.Animation;
import dev.forward.casino.engine.animation.AnimationClip;
import dev.forward.casino.util.color.Color;
import dev.forward.casino.util.color.Palette;
import dev.forward.casino.util.math.V3;
import dev.forward.casino.util.render.GLUtils;
import dev.forward.casino.util.render.SimpleDrawer;
import lombok.Getter;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

public class Input extends AbstractCarvedRectangle<Input> {
    public static final String CHARACTERS_FILTER;
    public static final String ADVANCED_FILTER;
    @Getter
    private String text = "";
    @Getter
    private String placeHolder = "";
    private String oldText = "";
    private String tabCompleteText = "";
    private int cursorPos;
    private int selectionPos;
    private float textOffset;
    @Getter
    private int[] filter;
    @Getter
    private int limit;
    private boolean isRepeatingEnableBefore;
    private long lastCursorSetTime;
    private long lastPasteTime;
    private long lastClickTime;
    private int clickCounter;
    private final Color normalTextColor;
    private final Color focusedTextColor;
    private final Color selectionPlaceholderColor;
    private final Color selectOutlineColor;
    private final String activeTabId;
    private int selectedTabIndex;
    private List<String> availableTabCompletions;
    private final boolean canPasteText;

    public Input() {
        this.normalTextColor = Palette.WHITE;
        this.focusedTextColor = Palette.GREY_LIGHT;
        this.selectionPlaceholderColor = new Color(0.0F, 0.0F, 1.0F, 1.0F);
        this.selectOutlineColor = Palette.BLUE;
        this.activeTabId = "";
        this.availableTabCompletions = new ArrayList<>();
        this.canPasteText = true;
        this.setSize(new V3(128.0F, 38.0F));
        this.setColor(Palette.GREY.alpha(0.28));
        this.registerEvent(KeyPressEvent.class, (event) -> {
            int code = event.getKey();
            if (code == GLFW.GLFW_KEY_BACKSPACE) {
                this.deleteCharacter();
            } else if (code == GLFW.GLFW_KEY_ENTER) {
                Engine.getInteractionManager().setFocusedElement(null);
                this.setFocused(false, null);
            } else if (code == GLFW.GLFW_KEY_C && GLFW.glfwGetKey(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS) {
                String copyText = this.getSelectedText();
                if (copyText.isEmpty()) {
                    return;
                }
                GLFW.glfwSetClipboardString(mc.getWindow().getHandle(), copyText);
            } else if (code == GLFW.GLFW_KEY_X && GLFW.glfwGetKey(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS) {
                String copyText = this.getSelectedText();
                if (copyText.isEmpty()) {
                    return;
                }

                GLFW.glfwSetClipboardString(mc.getWindow().getHandle(), copyText);
                this.deleteCharacter();
            } else if (code == GLFW.GLFW_KEY_A && GLFW.glfwGetKey(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS) {
                this.selectionPos = 0;
                this.setCursorPos(this.text.length());
            } else if (code == GLFW.GLFW_KEY_LEFT) {
                if (GLFW.glfwGetKey(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS) {
                    this.moveWord(-1, true);
                } else {
                    this.moveCursor(-1, true);
                }
            } else if (code == GLFW.GLFW_KEY_RIGHT) {
                if (GLFW.glfwGetKey(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS) {
                    this.moveWord(1, true);
                } else {
                    this.moveCursor(1, true);
                }
            } else if (code != GLFW.GLFW_KEY_HOME ) {
                if (code != GLFW.GLFW_KEY_END) {
                    if (code == 15 && !this.activeTabId.isEmpty()) {
                        if (!this.tabCompleteText.equals(this.text)) {
                            this.setText(this.tabCompleteText);
                            this.moveCursor(this.text.length(), true);
                        } else {
                            if (this.availableTabCompletions.isEmpty()) {
                                return;
                            }

                            int tabSize = this.availableTabCompletions.size();
                            if (tabSize > 1) {
                                int newTab = this.selectedTabIndex + 1;
                                this.selectedTabIndex = newTab >= tabSize ? 0 : newTab;
                                this.tabCompleteText = this.availableTabCompletions.get(this.selectedTabIndex);
                                this.setText(this.tabCompleteText);
                                this.moveCursor(this.text.length(), true);
                            }
                        }
                    }
                } else {
                    this.moveCursor(this.text.length(), true);
                }
            } else {
                this.moveCursor(-this.text.length(), true);
            }

        });
        this.registerEvent(CharTypedEvent.class, (charr) -> {
            {
                this.typeCharacter(charr.getChar());
            }
        });
        this.registerEvent(FocusedEvent.class, (event) -> {
            this.setOutlineColor(this.isFocused ? this.selectOutlineColor : null);
            this.selectionPos = this.cursorPos;
            if (event.isFocused()) {
                this.isRepeatingEnableBefore = ((KeyboardAccessor) mc.keyboard).isRepeatEvents();
                mc.keyboard.setRepeatEvents(true);
            } else {
                mc.keyboard.setRepeatEvents(this.isRepeatingEnableBefore);
            }

        });
        this.registerEvent(MouseLeftClickEvent.class, (event) -> {
            float mouseX = (float) GLUtils.getMousePos().getX();
            int charIndex = this.getCharIndexFromMouse(mouseX);
            long currentMillis = Instant.now().toEpochMilli();
            if (currentMillis - this.lastClickTime > 400L || (charIndex < this.cursorPos || charIndex > this.selectionPos) && (charIndex > this.cursorPos || charIndex < this.selectionPos)) {
                this.clickCounter = 0;
            } else {
                ++this.clickCounter;
            }

            this.lastClickTime = currentMillis;
            if (this.clickCounter == 0) {
                this.moveCursorFromMouse(mouseX);
                this.selectionPos = this.cursorPos;
            } else if (this.clickCounter == 1) {
                this.selectCurrentWord();
            } else if (this.clickCounter == 2) {
                this.selectionPos = 0;
                this.setCursorPos(this.text.length());
            } else {
                this.clickCounter = 0;
            }

        });
        this.registerEvent(PreTickEvent.class, (event) -> {
            if (this.isPressed && this.clickCounter == 0) {
                this.moveCursorFromMouse((float)GLUtils.getMousePos().getX());
            }

        });
    }

    public static boolean isAllowedCharacter(char character) {
        return character != 167 && character >= ' ' && character != 127;
    }

    public Input setText(String text) {
        InputTextUpdateEvent event = new InputTextUpdateEvent(this, text);
        this.fireEvent(event);
        this.text = event.getText();
        if (this.cursorPos > text.length()) {
            this.setCursorPos(text.length());
            this.selectionPos = this.cursorPos;
        }

        return this;
    }

    public Input setPlaceHolder(String placeHolder) {
        this.placeHolder = I18n.translate(placeHolder);
        return this;
    }

    public Input setTabCompleteText(String tabCompleteText) {
        this.tabCompleteText = I18n.translate(tabCompleteText);
        return this;
    }

    public Input setFilter(int... filter) {
        this.filter = filter;
        return this;
    }
    public Input setFilter(String filter) {
        return this.setFilter(filter.chars().toArray());
    }
    public Input setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public Input setSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
        return this;
    }

    public Input setAvailableTabCompletions(List<String> availableTabCompletions) {
        this.availableTabCompletions = availableTabCompletions;
        return this;
    }

    public String getCurrentTabCompletion() {
        return this.selectedTabIndex >= 0 && this.selectedTabIndex < this.availableTabCompletions.size() ? this.availableTabCompletions.get(this.selectedTabIndex) : null;
    }

    public void deleteCharacter() {
        if (!this.text.isEmpty()) {
            boolean hasSelection = this.hasSelection();
            boolean repeatDelete = !hasSelection && GLFW.glfwGetKey(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS && !this.text.endsWith(" ");
            int pos1;
            int pos2;
            if (hasSelection) {
                pos1 = Math.min(this.cursorPos, this.selectionPos);
                pos2 = Math.max(this.cursorPos, this.selectionPos);
            } else {
                pos1 = Math.max(this.cursorPos - 1, 0);
                pos2 = this.cursorPos;
            }

            String s1 = this.text.substring(0, pos1);
            String s2 = this.text.substring(pos2);
            String newText = s1 + s2;
            this.setText(newText);
            this.setCursorPos(pos1);
            this.selectionPos = pos1;
            this.requestTabComplete();
            if (repeatDelete) {
                this.deleteCharacter();
            }

        }
    }

    public void typeText(String text) {
        char[] var2 = text.toCharArray();
        for (char character : var2) {
            this.typeCharacter(character);
        }
    }

    public void typeCharacter(char character) {

        if (isAllowedCharacter(character)) {
            if (this.hasSelection()) {
                this.deleteCharacter();
            }

            if (this.limit != 0 && this.text.length() >= this.limit) {
                this.playFailAnimation();
            } else if (this.filter == null || Arrays.stream(this.filter).anyMatch((i) -> i == character)) {
                if (this.cursorPos == this.text.length()) {

                    this.setText(this.text = this.text + character);
                } else {
                    String s1 = this.text.substring(0, this.cursorPos);
                    String s2 = this.text.substring(this.cursorPos);
                    this.setText(s1 + character + s2);
                }

                this.requestTabComplete();
                this.moveCursor(1, false);
            }
        }
    }

    public void pasteText(String text) {
        if (this.canPasteText) {
            long currentTime = Instant.now().toEpochMilli();
            if (currentTime - this.lastPasteTime >= 250L) {
                this.lastPasteTime = currentTime;
                this.typeText(text);
            }
        }
    }

    public void render(MatrixStack stack, double partialTicks, double mouseX, double mouseY) {
        super.render(stack, partialTicks, mouseX, mouseY);
        TextRenderer fontRenderer = mc.textRenderer;
        V3 absoluteScale = this.getAbsoluteScale();
        V3 holderSize = (new V3(this.size.getX() - 22.0, 18.0)).multiply(absoluteScale);
        float cursorX = this.text.isEmpty() ? 0.0F : (float)fontRenderer.getWidth(this.text.substring(0, this.cursorPos));
        GlStateManager.pushMatrix();
        GlStateManager.translated(11.0, (this.size.getY() - 16.0) / 2.0, 0.0);
        GLUtils.enableScissor(new V3(0.0, -3.0), holderSize);
        GlStateManager.scalef(2.0F, 2.0F, 2.0F);
        if (!this.text.isEmpty() || this.isFocused() && this.text.length() > 1) {
            if (!this.activeTabId.isEmpty() && !this.tabCompleteText.isEmpty()) {
                this.renderText(stack,fontRenderer, this.tabCompleteText, 0.0F, 0.0F, this.focusedTextColor);
            }

            this.renderText(stack,fontRenderer, this.text, -this.textOffset, 0.0F, this.normalTextColor);
        } else {
            this.renderText(stack,fontRenderer, this.placeHolder, -this.textOffset, 0.0F, this.focusedTextColor);
        }

        if (this.cursorPos != this.selectionPos) {
            this.renderSelection(fontRenderer, this.cursorPos, this.selectionPos, this.textOffset);
        }

        if (this.isFocused() && (Instant.now().toEpochMilli() - this.lastCursorSetTime) % 600L < 300L) {
            this.renderCursor(stack,fontRenderer, cursorX, this.textOffset);
        }

        GLUtils.disableScissor();
        GlStateManager.popMatrix();
    }

    private void renderText(MatrixStack stack,TextRenderer fontRenderer, String text, float x, float y, Color color) {
        if (!(color.getAlpha() * this.getAbsoluteAlpha() <= 0.05)) {
            double absoluteAlpha = this.getAbsoluteAlpha();
            int decimalColor = color.setAlpha(color.getAlpha() * absoluteAlpha).getDecimal();
            fontRenderer.draw(stack, text, x, y, decimalColor);
        }
    }

    private void renderSelection(TextRenderer fontRenderer, int cursorPos, int selectionPos, double textOffset) {
        int pos1 = Math.min(cursorPos, selectionPos);
        int pos2 = Math.max(cursorPos, selectionPos);
        double minX = (double)fontRenderer.getWidth(this.text.substring(0, pos1)) - textOffset;
        double maxX = (double)fontRenderer.getWidth(this.text.substring(0, pos2)) - textOffset;
        this.selectionPlaceholderColor.apply(0.28);
        SimpleDrawer.drawRect(minX, -2.0, maxX, 16.0);
    }

    private void renderCursor(MatrixStack stack,TextRenderer fontRenderer, float cursorX, double textOffset) {
        if (this.cursorPos == this.text.length()) {
            this.renderText(stack,fontRenderer, "_", (float)((double)cursorX - textOffset), 1.0F, this.normalTextColor);
        } else {
            double x = (double)cursorX - textOffset;
            Palette.WHITE.apply();
            SimpleDrawer.drawRect(x, -2.0, x + 1.0, 16.0);
        }

    }

    protected void setCursorPos(int cursorPos) {
        this.cursorPos = Math.max(Math.min(cursorPos, this.text.length()), 0);
        this.lastCursorSetTime = Instant.now().toEpochMilli();
        this.updateTextOffset();
    }

    protected void updateTextOffset() {
        TextRenderer fontRenderer = mc.textRenderer;
        float scaleX = (float)this.getAbsoluteScale().getX();
        float textWidth = (float)fontRenderer.getWidth(this.text) / scaleX;
        float fieldSize = (float)(this.size.getX() - 36.0);
        if (textWidth <= fieldSize) {
            this.textOffset = 0.0F;
        } else {
            float cursorX = this.text.isEmpty() ? 0.0F : (float)fontRenderer.getWidth(this.text.substring(0, this.cursorPos));
            cursorX /= scaleX;
            float minPos = this.textOffset;
            float maxPos = minPos + fieldSize;
            if (cursorX - minPos < minPos + 10.0F) {
                this.textOffset = Math.max(cursorX - 10.0F, 0.0F) * scaleX;
            } else if (cursorX - minPos > maxPos - 10.0F) {
                this.textOffset = (cursorX - fieldSize + 10.0F) * scaleX;
            }

        }
    }

    protected void moveCursor(int move, boolean select) {
        boolean makeSelect = select && GLFW.glfwGetKey(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS;
        int newCursorPos = Math.min(Math.max(this.cursorPos + move, 0), this.text.length());
        if (!makeSelect && this.hasSelection()) {
            if (move < 0) {
                newCursorPos = Math.min(this.cursorPos, this.selectionPos);
            } else {
                newCursorPos = Math.max(this.cursorPos, this.selectionPos);
            }
        }

        if (!makeSelect) {
            this.selectionPos = newCursorPos;
        }

        this.setCursorPos(newCursorPos);
    }

    protected void moveWord(int direction, boolean select) {
        char[] chars = this.text.toCharArray();
        int pos = this.cursorPos;

        while(direction > 0 && pos < chars.length || direction < 0 && pos > 0) {
            pos += direction;
            if (pos >= chars.length || pos < 0) {
                break;
            }

            char ch = chars[Math.max(pos + (direction < 0 ? -1 : 0), 0)];
            if (ch == ' ') {
                break;
            }
        }

        this.moveCursor(pos - this.cursorPos, select);
    }

    protected void selectCurrentWord() {
        this.moveWord(-1, false);
        int tempSelectionPos = this.selectionPos;
        this.moveWord(1, false);
        this.selectionPos = tempSelectionPos;
    }

    protected boolean hasSelection() {
        return this.cursorPos - this.selectionPos != 0;
    }

    protected String getSelectedText() {
        if (!this.text.isEmpty() && this.hasSelection()) {
            int pos1 = Math.min(this.cursorPos, this.selectionPos);
            int pos2 = Math.max(this.cursorPos, this.selectionPos);
            return this.text.substring(pos1, pos2);
        } else {
            return "";
        }
    }

    private void moveCursorFromMouse(float mouseX) {
        int charIndex = this.getCharIndexFromMouse(mouseX);
        if (charIndex != -1) {
            this.setCursorPos(charIndex);
        }

    }

    private int getCharIndexFromMouse(float mouseX) {
        TextRenderer fontRenderer = mc.textRenderer;
        mouseX -= this.bakedMatrix[12];
        float textWidth = mouseX + this.textOffset + 3.0F;
        String trimText = fontRenderer.trimToWidth(this.text + " ", (int)textWidth);
        return Math.max(trimText.length() - 1, 0);
    }

    protected void requestTabComplete() {
        if (!this.activeTabId.isEmpty() && !this.oldText.equals(this.text)) {
            this.oldText = this.text;
            if (this.text.isEmpty()) {
                this.setSelectedTabIndex(0);
                this.setAvailableTabCompletions(new ArrayList());
                this.tabCompleteText = "";
            }

            //Enginex.getChat().printChatMessage("/tbc " + this.activeTabId + " " + this.text);
        }

    }

    public void playFailAnimation() {
        if (!Animation.hasAnimation(this, "fail")) {
            Color failColor = new Color(169, 25, 37, this.color.getAlpha());
            Color failOutlineColor = new Color(231, 61, 75, this.outlineColor.getAlpha());
            Color initialColor = this.color;
            Color initialOutlineColor = this.outlineColor;
            Animation.play(this, "fail", 0.3, Animation.DEFAULT_OUT_ANIMATION, (element, progress) -> {
                element.setColor(initialColor.interpolateProgressColor(failColor, progress));
                element.setOutlineColor(initialOutlineColor.interpolateProgressColor(failOutlineColor, progress));
            }).playAfter(new AnimationClip<>(this, "fail", 0.3, Animation.DEFAULT_OUT_ANIMATION, (element, progress) -> {
                element.setColor(failColor.interpolateProgressColor(initialColor, progress));
                element.setOutlineColor(failOutlineColor.interpolateProgressColor(initialOutlineColor, progress));
            }));
        }
    }

    public Input copy(Input element) {
        super.copy(element);
        element.setPlaceHolder(this.getPlaceHolder()).setFilter(this.getFilter()).setLimit(this.getLimit());
        return element;
    }

    public Input clone() {
        return this.copy(new Input());
    }
    static {
        String alphabet = "abcdefghijklmnopqrstuvwxyzабвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        alphabet = alphabet + alphabet.toUpperCase();
        CHARACTERS_FILTER = alphabet;
        ADVANCED_FILTER = alphabet + "0123456789-_. ";
    }
}
