package dev.forward.casino.engine.elements;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.engine.tooltip.TooltipData;
import dev.forward.casino.util.color.Color;
import dev.forward.casino.util.color.Palette;
import dev.forward.casino.util.math.V3;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL30;

public class ItemRender
        extends AbstractElement<ItemRender> {
    private ItemStack item = null;
    private boolean renderTooltip = false;
    private boolean renderOverlay = false;

    public ItemRender() {
        this(null);
    }

    public ItemRender(ItemStack item) {
        this(item, false, false);
    }

    public ItemRender(ItemStack item, boolean renderOverlay, boolean renderTooltip) {
        this.item = item;
        this.setRenderOverlay(renderOverlay);
        this.setRenderTooltip(renderTooltip);
        this.setSize(new V3(16.0, 16.0, 16.0));
        this.setColor(Palette.WHITE);
    }

    public ItemStack getItem() {
        return this.item;
    }

    public ItemRender setItem(ItemStack item) {
        this.item = item;
        return this;
    }

    public boolean isRenderOverlay() {
        return this.renderOverlay;
    }

    public ItemRender setRenderOverlay(boolean renderOverlay) {
        this.renderOverlay = renderOverlay;
        return this;
    }

    public boolean isRenderTooltip() {
        return this.renderTooltip;
    }

    public ItemRender setRenderTooltip(boolean renderTooltip) {
        this.renderTooltip = renderTooltip;
        if (renderTooltip) {
            this.setInteractive(true);
        }
        return this;
    }

    @Override
    public void render(MatrixStack stack, double partialTicks, double mouseX, double mouseY) {
        if (this.item == null) {
            return;
        }
        GlStateManager.pushMatrix();
        GlStateManager.scaled(this.getSize().getX() / 16.0, this.getSize().getY() / 16.0, 1.0);
        DiffuseLighting.enable();
        Palette.WHITE.apply();
        Color color = this.color.alpha(this.getAbsoluteAlpha());
//        int decimalColor = color.getDecimal();
        boolean isDepth = GL30.glGetBoolean(2929);
        GlStateManager.enableDepthTest();
        GlStateManager.enableBlend();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlphaTest();
        GlStateManager.blendFunc(770, 771);
        ItemRenderer renderItem = mc.getItemRenderer();
        renderItem.renderInGuiWithOverrides(this.item, 0, 0);
        if (this.renderOverlay) {
            int count = this.item.getCount();
            String countText = "";
            if (count > 1) {
                countText = String.valueOf(count >= 1000 ? (int)Math.floor((double)count / 1000.0) + "К" : Integer.valueOf(count));
            }
            renderItem.renderGuiItemOverlay(mc.textRenderer, this.item, 0, 0, countText);
        }
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
        if (!isDepth) {
            GlStateManager.disableDepthTest();
        }
        DiffuseLighting.disable();
        if (this.renderTooltip && this.isHover) {
            TooltipData tooltipData = TooltipData.builder().linesAsText(this.item.getTooltip(player, TooltipContext.Default.NORMAL)).build();
            Engine.getCustomTooltip().render(tooltipData);
        }
    }

    @Override
    public ItemRender copy(ItemRender element) {
        super.copy(element);
        element.setItem(this.getItem() != null ? this.getItem().copy() : null).setRenderOverlay(this.isRenderOverlay()).setRenderTooltip(this.isRenderTooltip());
        return element;
    }

    @Override
    public ItemRender clone() {
        return this.copy(new ItemRender());
    }
}