package dev.forward.casino.util.render;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.mixins.accessors.NativeImageAccessor;
import dev.forward.casino.util.FastAccess;
import dev.forward.casino.util.color.Color;
import dev.forward.casino.util.color.ColorUtil;
import dev.forward.casino.util.color.Palette;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Objects;

public class SimpleDrawer implements FastAccess {
    private static double zLevel = 0.0F;
    public static void drawCarvedRect(double left, double top, double right, double bottom, double carved, boolean twoSideRender, Boolean[] carvedEnable, Color color) {
        if (!(right - left <= (double)0.0F) && !(bottom - top <= (double)0.0F)) {
            BufferBuilder buffer = tessellator.getBuffer();
            color.apply();
            GlStateManager.disableTexture();
            if (twoSideRender) {
                GlStateManager.disableCull();
            }

            double centerX = left + (right - left) / (double)2.0F;
            double centerY = top + (bottom - top) / (double)2.0F;
            if (carvedEnable[1] && carvedEnable[0] && right - left < carved * (double)2.0F || (carvedEnable[1] || carvedEnable[0]) && right - left < carved) {
                carvedEnable[1] = false;
                carvedEnable[0] = false;
                top += carved;
            }

            if (carvedEnable[2] && carvedEnable[3] && right - left < carved * (double)2.0F || (carvedEnable[2] || carvedEnable[3]) && right - left < carved) {
                carvedEnable[2] = false;
                carvedEnable[3] = false;
                bottom -= carved;
            }

            if (!carvedEnable[0] && !carvedEnable[1] && !carvedEnable[2] && !carvedEnable[3]) {
                buffer.begin(7, VertexFormats.POSITION);
                buffer.vertex(left, bottom, zLevel).next();
                buffer.vertex(right, bottom, zLevel).next();
                buffer.vertex(right, top, zLevel).next();
                buffer.vertex(left, top, zLevel).next();
            } else {
                buffer.begin(5, VertexFormats.POSITION);
                if (carvedEnable[3]) {
                    buffer.vertex(left + carved, bottom, zLevel).next();
                    buffer.vertex(centerX, centerY, (double)0.0F).next();
                    buffer.vertex(left + carved, bottom - carved, zLevel).next();
                    buffer.vertex(centerX, centerY, (double)0.0F).next();
                    buffer.vertex(left, bottom - carved, zLevel).next();
                } else {
                    buffer.vertex(left, bottom, zLevel).next();
                }

                buffer.vertex(centerX, centerY, (double)0.0F).next();
                if (carvedEnable[0]) {
                    buffer.vertex(left, top + carved, zLevel).next();
                    buffer.vertex(centerX, centerY, (double)0.0F).next();
                    buffer.vertex(left + carved, top + carved, zLevel).next();
                    buffer.vertex(centerX, centerY, (double)0.0F).next();
                    buffer.vertex(left + carved, top, zLevel).next();
                } else {
                    buffer.vertex(left, top, zLevel).next();
                }

                buffer.vertex(centerX, centerY, (double)0.0F).next();
                if (carvedEnable[1]) {
                    buffer.vertex(right - carved, top, zLevel).next();
                    buffer.vertex(centerX, centerY, (double)0.0F).next();
                    buffer.vertex(right - carved, top + carved, zLevel).next();
                    buffer.vertex(centerX, centerY, (double)0.0F).next();
                    buffer.vertex(right, top + carved, zLevel).next();
                } else {
                    buffer.vertex(right, top, zLevel).next();
                }

                buffer.vertex(centerX, centerY, (double)0.0F).next();
                if (carvedEnable[2]) {
                    buffer.vertex(right, bottom - carved, zLevel).next();
                    buffer.vertex(centerX, centerY, (double)0.0F).next();
                    buffer.vertex(right - carved, bottom - carved, zLevel).next();
                    buffer.vertex(centerX, centerY, (double)0.0F).next();
                    buffer.vertex(right - carved, bottom, zLevel).next();
                } else {
                    buffer.vertex(right, bottom, zLevel).next();
                }

                buffer.vertex(centerX, centerY, (double)0.0F).next();
                buffer.vertex(left + (carvedEnable[3] ? carved : (double)0.0F), bottom, zLevel).next();
            }

            tessellator.draw();
            Palette.WHITE.apply();
            GlStateManager.enableTexture();
            if (twoSideRender) {
                GlStateManager.enableCull();
            }

        }
    }
    public static void drawRect(double left, double top, double right, double bottom) {
        BufferBuilder builder = tessellator.getBuffer();
        GlStateManager.disableTexture();
        builder.begin(7, VertexFormats.POSITION);
        builder.vertex(left, bottom, 0.0).next();
        builder.vertex(right, bottom, 0.0).next();
        builder.vertex(right, top, 0.0).next();
        builder.vertex(left, top, 0.0).next();
        tessellator.draw();
        GlStateManager.enableTexture();
    }

    public static void drawRectTexture(double x, double y, double width, double height) {
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
        builder.vertex(x, y, 0).texture(0, 0).color(Palette.WHITE.getIntR(), Palette.WHITE.getIntG(), Palette.WHITE.getIntB(), Palette.WHITE.getIntAlpha()).next();
        builder.vertex(x, y + height, 0).texture(0, 1).color(Palette.WHITE.getIntR(), Palette.WHITE.getIntG(), Palette.WHITE.getIntB(), Palette.WHITE.getIntAlpha()).next();
        builder.vertex(x + width, y + height, 0).texture(1, 1).color(Palette.WHITE.getIntR(), Palette.WHITE.getIntG(), Palette.WHITE.getIntB(), Palette.WHITE.getIntAlpha()).next();
        builder.vertex(x + width, y, 0).texture(1, 0).color(Palette.WHITE.getIntR(), Palette.WHITE.getIntG(), Palette.WHITE.getIntB(), Palette.WHITE.getIntAlpha()).next();
        tessellator.draw();
    }
    private static final HashMap<Integer, Integer> shadowCache = new HashMap<Integer, Integer>();
    public static void drawGlow(float x, float y, float width, float height, int radius, int color) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.01f);
        GlStateManager.disableAlphaTest();

        x -= radius;
        y -= radius;
        width = width + radius * 2;
        height = height + radius * 2;
        x -= 0.25f;
        y += 0.25f;

        int identifier = Objects.hash(width, height, radius);
        int textureId;

        if (shadowCache.containsKey(identifier)) {
            textureId = shadowCache.get(identifier);
            GlStateManager.bindTexture(textureId);
        } else {
            if (width <= 0) {
                width = 1;
            }

            if (height <= 0) {
                height = 1;
            }

            BufferedImage originalImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D graphics = originalImage.createGraphics();
            graphics.setColor(java.awt.Color.WHITE);
            graphics.fillRect(radius, radius, (int) (width - radius * 2), (int) (height - radius * 2));
            graphics.dispose();

            GaussianFilter filter = new GaussianFilter(radius);
            BufferedImage blurredImage = filter.filter(originalImage, null);
            NativeImageBackedTexture texture = new NativeImageBackedTexture(toNativeImage(blurredImage));
            texture.setFilter(true, true);
            try {
                textureId = texture.getGlId();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            shadowCache.put(identifier, textureId);
        }

        float[] startColorComponents = ColorUtil.rgba(color);
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        buffer.vertex(x, y, 0.0f)
                .color(startColorComponents[0], startColorComponents[1], startColorComponents[2],
                        startColorComponents[3])
                .texture(0.0f, 0.0f)
                .next();

        buffer.vertex(x, y + (float) ((int) height), 0.0f)
                .color(startColorComponents[0], startColorComponents[1], startColorComponents[2],
                        startColorComponents[3])
                .texture(0.0f, 1.0f)
                .next();

        buffer.vertex(x + (float) ((int) width), y + (float) ((int) height), 0.0f)
                .color(startColorComponents[0], startColorComponents[1], startColorComponents[2],
                        startColorComponents[3])
                .texture(1.0f, 1.0f)
                .next();

        buffer.vertex(x + (float) ((int) width), y, 0.0f)
                .color(startColorComponents[0], startColorComponents[1], startColorComponents[2],
                        startColorComponents[3])
                .texture(1.0f, 0.0f)
                .next();

        tessellator.draw();
        GlStateManager.enableAlphaTest();
        GlStateManager.bindTexture(0);
        GlStateManager.disableBlend();
    }
    public static NativeImage toNativeImage(BufferedImage bi)
    {
        int i = bi.getWidth();
        int j = bi.getHeight();
        int[] aint = new int[i * j];
        bi.getRGB(0, 0, i, j, aint, 0, i);
        NativeImage nativeimage = new NativeImage(i, j, false);
        NativeImageAccessor accessor = (NativeImageAccessor) (Object)nativeimage;
        MemoryUtil.memIntBuffer(accessor.pointer(), (int)accessor.sizeBytes()).put(aint);
        return nativeimage;
    }
}
