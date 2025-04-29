package dev.forward.casino.engine.contexts;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.engine.event.PostRenderEvent;
import dev.forward.casino.event.impl.render.EntitiesRenderPost;
import dev.forward.casino.mixins.accessors.WorldRendererAccessor;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.engine.elements.AbstractElement;
import dev.forward.casino.engine.elements.Container;
import dev.forward.casino.util.interactive.InteractionManager;
import dev.forward.casino.util.math.MathUtil;
import dev.forward.casino.util.math.V3;
import dev.forward.casino.util.render.GLUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WorldContext
        extends Container
        implements IContext {
    private static WorldContext instance;

    private WorldContext() {
        double scale = 0.0625;
        this.setScale(new V3(-scale, -scale, scale));
        EntitiesRenderPost.BUS.register(event -> {
            if (this.childs.isEmpty()) {
                return;
            }
            V3 mousePos = GLUtils.getMousePos();
            float partialTicks = getPartialTicks();
            Entity entity = mc.getCameraEntity();
            if (entity != null) {
                V3 pos = new V3((entity.getX() - entity.prevX) * (double) partialTicks + entity.prevX, (entity.getY() - entity.prevY) * (double) partialTicks + entity.prevY, (entity.getZ() - entity.prevZ) * (double) partialTicks + entity.prevZ);
                this.setPos(pos.multiply(-1.0));
                GlStateManager.disableLighting();
                mc.gameRenderer.getLightmapTextureManager().disable();
//                this.transformAndRender(null, partialTicks, mousePos.getX(), mousePos.getY());
                GlStateManager.enableLighting();
                DiffuseLighting.enable();
                mc.gameRenderer.getLightmapTextureManager().enable();
                mc.getTextureManager().bindTexture(new Identifier("textures/atlas/blocks.png"));
                GlStateManager.disableBlend();
            }
        });
    }

    public static WorldContext get() {
        if (instance == null) {
            instance = new WorldContext();
        }
        return instance;
    }

    @Override
    public void unload() {
        this.clearChilds();
        instance = null;
    }

    @Override
    public V3 getAbsolutePos() {
        return new V3();
    }

    @Override
    public boolean isWorldContext() {
        return true;
    }
    @Override
    public void postRender(MatrixStack stack,double partialTicks, double mouseX, double mouseY, boolean isInteractive, InteractionManager interactionManager) {
        Frustum frustum = ((WorldRendererAccessor) (mc.worldRenderer)).capturedFrustum();
        this.markDirty();
        List<AbstractElement<?>> renderChilds = this.getChilds().stream().filter(child -> {
            child.setIs3dRender(true);
            Box boundingBox = child.updateAndGetBoundingBox();
            boundingBox = boundingBox.expand(0.5, 0.5, 0.5);
            if (frustum != null) {
                return !child.isEnableFrustumCulling() || frustum.isVisible(boundingBox);
            }
            return !child.isEnableFrustumCulling() ;
        }).sorted(Comparator.comparingDouble(child -> MathUtil.getElementSquaredDistanceToCamera((AbstractElement<?>)child)).reversed()).collect(Collectors.toList());
        for (AbstractElement<?> child2 : renderChilds) {
            child2.transformAndRender(stack, this, partialTicks, mouseX, mouseY, isInteractive, interactionManager);
            child2.drawDebugBoundingBox();
        }
        this.fireEvent(new PostRenderEvent(this));
    }
}
