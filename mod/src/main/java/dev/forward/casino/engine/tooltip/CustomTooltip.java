package dev.forward.casino.engine.tooltip;
// Decompiled with: CFR 0.152
// Class Version: 8
import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.engine.elements.*;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.engine.animation.EasingFunc;
import dev.forward.casino.engine.animation.SimpleAnimation;
import dev.forward.casino.engine.contexts.PostOverlay;
import dev.forward.casino.event.impl.render.ItemTooltipRender;
import dev.forward.casino.util.color.Color;
import dev.forward.casino.util.color.Palette;
import dev.forward.casino.util.math.Relative;
import dev.forward.casino.util.math.V3;
import dev.forward.casino.util.render.GLUtils;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomTooltip
        extends AbstractTooltip<Mask> {
    private final VerticalLayout layout;
    private final Text titleText;
    private final Text text;
    private final Container line;
    private Color cornersColor;
    private Image[] corners;
    private final long lastUpdateSizeTime = 0L;
    private V3 currentSize;
    private boolean enableCustomTooltip = false;

    public CustomTooltip() {
        super(new Mask());
        this.element.setOutline(2.0).setCarveSize(4.0).setShadowSize(35.0f).setSize(new V3(26.0, 26.0, 0.0)).setColor(Palette.GREY_DARK).setEnabled(false);
        PostOverlay.get().addChild(this.element);
        this.initCorners();
        this.layout = new VerticalLayout(8.0);
        this.layout.setPos(new V3(18.0, 18.0));
        this.element.addChild(this.layout);
        this.titleText = new Text().setShadow(true).setLineHeight(24.0);
        this.layout.addChild(this.titleText);
        this.line = new Container(){

            @Override
            public void render(MatrixStack stack, double partialTicks, double mouseX, double mouseY) {
                this.color.apply();
                GlStateManager.disableTexture();
                GlStateManager.lineWidth(2.0f);
                GL11.glDisable(2848);
                BufferBuilder builder = tessellator.getBuffer();
                builder.begin(1, VertexFormats.POSITION);
                builder.vertex(0.0, 0.0, 0.0).next();
                builder.vertex(CustomTooltip.this.layout.getSize().getX(), 0.0, 0.0).next();
                tessellator.draw();
                GL11.glEnable(2848);
                GlStateManager.enableTexture();
                Palette.WHITE.apply();
            }
        }.setColor(Palette.GREY_MIDDLE).setSize(new V3(1.0, 1.0));
        this.layout.addChild(this.line);
        this.text = new Text().setShadow(true).setLineHeight(24.0);
        this.layout.addChild(this.text);
        ItemTooltipRender.BUS.register(event -> {
            if (this.enableCustomTooltip) {
                event.setCancelled(true);
                List<String> strings = event.getContent()
                        .stream()
                        .map(net.minecraft.text.Text::asString)
                        .collect(Collectors.toList());
                this.render(new TooltipData(strings, ""));
            }
        });

    }

    private void initCorners() {
        Image topLeftCorner = new Image("enginex:textures/corner");
        topLeftCorner.setAlign(Relative.TOP_LEFT).setPos(new V3(0.0, 64.0)).setSize(new V3(64.0, 64.0)).setRotation(new V3(0.0, 0.0, 270.0)).setColor(this.cornersColor).setEnabled(false);
        this.element.addChild(topLeftCorner);
        Image topRightCorner = new Image("enginex:textures/corner");
        topRightCorner.setAlign(Relative.TOP_RIGHT).setPos(new V3(-64.0, 0.0)).setSize(new V3(64.0, 64.0)).setColor(this.cornersColor).setEnabled(false);
        this.element.addChild(topRightCorner);
        Image bottomLeftCorner = new Image("enginex:textures/corner");
        bottomLeftCorner.setAlign(Relative.BOTTOM_LEFT).setPos(new V3(64.0, 0.0)).setSize(new V3(64.0, 64.0)).setRotation(new V3(0.0, 0.0, 180.0)).setColor(this.cornersColor).setEnabled(false);
        this.element.addChild(bottomLeftCorner);
        Image bottomRightCorner = new Image("enginex:textures/corner");
        bottomRightCorner.setAlign(Relative.BOTTOM_RIGHT).setPos(new V3(0.0, -64.0)).setSize(new V3(64.0, 64.0)).setRotation(new V3(0.0, 0.0, 90.0)).setColor(this.cornersColor).setEnabled(false);
        this.element.addChild(bottomRightCorner);
        this.corners = new Image[]{topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner};
    }

    public void render(TooltipData tooltipData) {
        this.doRender(tooltipData, GLUtils.getMousePos());
    }

    private void doRender(TooltipData tooltipData, V3 renderPos) {
        String title = tooltipData.getTitle();
        Collection<String> lines = tooltipData.getLines();
        Color cornersColor = tooltipData.getCornerColor();
        if (tooltipData.isEmpty()) {
            return;
        }
        this.setupGradientOutlineColor(tooltipData);
        this.titleText.setAutoSkip(tooltipData.getAutoSkip());
        this.text.setAutoSkip(tooltipData.getAutoSkip());
        if (!title.isEmpty()) {
            this.titleText.setValue(title).setEnabled(true);
        } else {
            this.titleText.setEnabled(false);
        }
        if (lines != null && !lines.isEmpty()) {
            this.text.setValue(lines.toArray(new String[0])).setEnabled(true);
        } else {
            this.text.setEnabled(false);
        }
        if (tooltipData.canRenderLine() && cornersColor != null) {
            this.line.setColor(cornersColor).setEnabled(true);
        } else {
            this.line.setEnabled(false);
        }
        this.cornersColor = cornersColor;
        boolean hasCorners = cornersColor != null;
        double inset = hasCorners ? 18.0 : 10.0;
        this.layout.setPos(new V3(inset, inset));
        V3 layoutSize = this.layout.getSize().add(inset * 2.0, inset * 2.0, 0.0);
        if (!layoutSize.equals(this.currentSize)) {
            this.currentSize = layoutSize;
            if (tooltipData.isSizeChangeAnimation()) {
                SimpleAnimation.playSize(this.element, layoutSize, 0.13, EasingFunc.EASE_OUT_CIRC);
            } else {
                this.element.setSize(layoutSize);
            }
        }
        this.element.setOutline(hasCorners ? 0.0 : 2.0);
        for (Image corner : this.corners) {
            corner.setColor(cornersColor).setEnabled(hasCorners);
        }
        this.updateTooltipPosition(renderPos);
    }

    private void setupGradientOutlineColor(TooltipData hoveredTooltipData) {
        if (hoveredTooltipData.canRenderGradientOutline()) {
            this.element.setOutlineGradientColor(hoveredTooltipData.getGradientOutlineColor());
        } else {
            this.element.setOutlineGradientColor(null);
        }
    }

    public void setEnableCustomTooltip(boolean enableCustomTooltip) {
        this.enableCustomTooltip = enableCustomTooltip;
    }
}
