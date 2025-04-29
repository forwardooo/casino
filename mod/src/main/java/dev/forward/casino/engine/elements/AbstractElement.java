package dev.forward.casino.engine.elements;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.engine.event.*;
import dev.forward.casino.engine.event.AbstractEvent;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.engine.component.ComponentEvent;
import dev.forward.casino.engine.tooltip.TooltipData;
import dev.forward.casino.util.FastAccess;
import dev.forward.casino.util.color.Color;
import dev.forward.casino.util.color.Palette;
import dev.forward.casino.util.interactive.InteractionManager;
import dev.forward.casino.util.math.MathUtil;
import dev.forward.casino.util.math.MutableV3;
import dev.forward.casino.util.math.V3;
import dev.forward.casino.util.render.GLUtils;
import dev.forward.casino.util.render.SimpleDrawer;
import lombok.Getter;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public abstract class AbstractElement<T extends AbstractElement<T>>
        implements Parent<T>, FastAccess {
    protected final T genericThis = (T) this;
    protected final float[] bakedMatrix = new float[16];
    private final Map<Class<? extends AbstractEvent>, Set<Consumer<? extends AbstractEvent>>> eventsMap = new HashMap<>();
    @Getter
    protected String id = "";
    @Getter
    protected TooltipData tooltip;
    protected boolean glowing;
    protected List<AbstractElement<?>> childs = new ArrayList<>();
    protected V3 pos = new V3();
    protected V3 offset = new V3();
    @Getter
    protected V3 origin = new V3();
    @Getter
    protected V3 align = new V3();
    protected V3 size = new V3();
    protected V3 renderSize = new V3(16.0, 16.0, 16.0);
    @Getter
    protected V3 scale = new V3(1.0, 1.0, 1.0);
    protected V3 rotation = new V3(0.0, 0.0, 0.0);
    @Getter
    protected Color color = new Color(1.0, 1.0, 1.0, 1.0);
    @Getter
    protected double alpha = 1.0;
    @Getter
    protected long renderLayer = 0L;
    @Getter
    protected long hoverLayer = 0L;
    protected double worldRenderDistance = 100.0;
    @Getter
    protected boolean enabled = true;
    @Getter
    protected boolean interactive;
    protected boolean isHover;
    protected boolean isFocused;
    protected boolean isLeftPressed;
    protected boolean isRightPressed;
    @Deprecated
    protected boolean isPressed;
    protected boolean is3dRender = false;
    @Getter
    protected boolean enableFrustumCulling = true;
    protected AbstractElement<?> lastParent;
    protected InteractionManager lastInteractionManager;
    protected Box boundingBox;
    protected boolean isDirty = true;
    protected boolean hideFromHierarchy;
    protected boolean renderDebugBoxIfDisabled = true;
    protected boolean transformIfDisabled = false;

    public void markDirty() {
        this.isDirty = true;
        for (AbstractElement<?> child : this.childs) {
            child.markDirty();
        }
    }

    private void fireUpdateToParent() {
        if (this.lastParent != null) {
            this.lastParent.childUpdate(this);
        }
    }

    public T addChild(AbstractElement<?> ... elements) {
        this.childs = new ArrayList<>(this.childs);
        for (AbstractElement<?> element : elements) {
            if (this.childs.contains(element)) continue;
            this.childs.add(element);
            element.setLastParent(this);
        }
        this.markDirty();
        return this.genericThis;
    }

    public T addChild(int index, AbstractElement<?> element) {
        this.childs = new ArrayList<>(this.childs);
        if (!this.childs.contains(element)) {
            this.childs.add(index, element);
            element.setLastParent(this);
        }
        this.markDirty();
        return this.genericThis;
    }

    public T removeChild(AbstractElement<?> ... elements) {
        if (elements == null || elements.length == 0) {
            return this.genericThis;
        }
        Set<?> elementsToRemove = new HashSet<>(Arrays.asList(elements));
        boolean anyChildRemoved = false;
        Iterator<AbstractElement<?>> iterator = this.childs.iterator();
        while (iterator.hasNext()) {
            AbstractElement<?> child = iterator.next();
            if (!elementsToRemove.contains(child)) continue;
            if (child.isEnabled()) {
                this.prepareChildsForRemoval(child);
            }
            iterator.remove();
            anyChildRemoved = true;
        }
        if (anyChildRemoved) {
            this.markDirty();
        }
        return this.genericThis;
    }

    public T clearChilds() {
        if (this.childs.isEmpty()) {
            return this.genericThis;
        }
        for (AbstractElement<?> child : this.childs) {
            if (!child.isEnabled()) continue;
            this.prepareChildsForRemoval(child);
        }
        this.childs.clear();
        this.markDirty();
        return this.genericThis;
    }

    public void prepareChildsForRemoval(AbstractElement<?> element) {
        ArrayDeque<AbstractElement<?>> stack = new ArrayDeque<>();
        stack.push(element);
        while (!stack.isEmpty()) {
            AbstractElement<?> current = stack.pop();
            if (!current.isEnabled()) continue;
            if (current.isHover) {
                current.setHoverState(false);
            }
            if (current.isFocused) {
                Engine.getInteractionManager().setFocusedElement(null);
                current.setFocused(false, null);
            }
            Engine.getComponentManager().removeAllComponentEventsForElement(current);
            Engine.getInteractionManager().removeAttemptHoveredElement(current);
            stack.addAll(current.getChilds());
        }
    }

    public int getChildCount() {
        return this.childs.size();
    }

    public List<AbstractElement<?>> getChilds() {
        this.childs = new ArrayList<>(this.childs);
        return this.childs;
    }

    public T setId(String id) {
        this.id = id;
        return this.genericThis;
    }

    public T setTooltip(String tooltip) {
        return this.setTooltip(tooltip, false);
    }

    public T setTooltip(String tooltip, boolean isTooltipChatScreenOnly) {
        if (tooltip == null) {
            return this.setTooltip((TooltipData)null);
        }
        return this.setTooltip(TooltipData.builder().linesArray(tooltip).tooltipChatScreenOnly(isTooltipChatScreenOnly).build());
    }
    public void charTyped(char c) {
        this.fireEvent(new CharTypedEvent(this, c));
    }
    public T setTooltip(TooltipData tooltip) {
        this.tooltip = tooltip;
        if (this.tooltip != null && !this.tooltip.isEmpty()) {
            this.setInteractive(true);
        }
        return this.genericThis;
    }

    @Override
    public T setPos(V3 pos) {
        if (this.pos.equals(pos)) {
            return this.genericThis;
        }
        this.pos = pos;
        this.markDirty();
        this.fireUpdateToParent();
        return this.genericThis;
    }
    @Override
    public T setGlowing(boolean glow) {
        this.glowing = glow;
        return this.genericThis;
    }

    @Override
    public T setOffset(V3 offset) {
        if (this.offset.equals(offset)) {
            return this.genericThis;
        }
        this.offset = offset;
        this.markDirty();
        this.fireUpdateToParent();
        return this.genericThis;
    }

    @Override
    public T setOrigin(V3 origin) {
        if (this.origin.equals(origin)) {
            return this.genericThis;
        }
        this.origin = origin;
        this.markDirty();
        this.fireUpdateToParent();
        return this.genericThis;
    }

    @Override
    public T setAlign(V3 align) {
        if (this.align.equals(align)) {
            return this.genericThis;
        }
        this.align = align;
        this.markDirty();
        this.fireUpdateToParent();
        return this.genericThis;
    }

    @Override
    public T setSize(V3 size) {
        if (this.size.equals(size)) {
            return this.genericThis;
        }
        this.size = size;
        this.markDirty();
        this.fireUpdateToParent();
        return this.genericThis;
    }

    @Override
    public T setScale(V3 scale) {
        if (this.scale.equals(scale)) {
            return this.genericThis;
        }
        this.scale = scale;
        this.markDirty();
        this.fireUpdateToParent();
        return this.genericThis;
    }

    @Override
    public T setRotation(V3 rotation) {
        if (this.rotation.equals(rotation)) {
            return this.genericThis;
        }
        this.rotation = rotation;
        this.markDirty();
        this.fireUpdateToParent();
        return this.genericThis;
    }

    public T setColor(Color color) {
        color = color == null ? Palette.WHITE.alpha(0.0) : color;
        if (this.color.equals(color)) {
            return this.genericThis;
        }
        this.color = color;
        this.markDirty();
        return this.genericThis;
    }

    public void applyColor() {
        this.color.apply(this.getAbsoluteAlpha());
    }

    public T setAlpha(double alpha) {
        if (this.alpha == alpha) {
            return this.genericThis;
        }
        this.alpha = alpha;
        this.markDirty();
        return this.genericThis;
    }

    public double getAbsoluteAlpha() {
        return this.lastParent != null ? this.lastParent.getAbsoluteAlpha() * this.alpha : this.alpha;
    }

    public T setHoverLayer(long hoverLayer) {
        this.hoverLayer = hoverLayer;
        return this.genericThis;
    }

    public long getTotalRenderLayer() {
        return (this.lastParent != null ? this.lastParent.getTotalRenderLayer() : 0L) + this.renderLayer;
    }

    public T setRenderLayer(long renderLayer) {
        if (this.renderLayer == renderLayer) {
            return this.genericThis;
        }
        this.markDirty();
        this.renderLayer = renderLayer;
        return this.genericThis;
    }

    public int getHierarchyDeep() {
        return this.lastParent != null ? this.lastParent.getHierarchyDeep() + 1 : 1;
    }

    public T setWorldRenderDistance(double worldRenderDistance) {
        this.worldRenderDistance = worldRenderDistance;
        return this.genericThis;
    }

    public T setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return this.genericThis;
        }
        this.enabled = enabled;
        this.transformIfDisabled = !enabled && this.renderLayer > 0L;
        this.markDirty();
        this.fireUpdateToParent();
        return this.genericThis;
    }

    public T setInteractive(boolean isInteractive) {
        this.interactive = isInteractive;
        return this.genericThis;
    }

    public boolean isLeftPressed() {
        return this.isLeftPressed;
    }

    public boolean is3dRender() {
        return this.is3dRender;
    }

    public T setIs3dRender(boolean is3dRender) {
        this.is3dRender = is3dRender;
        for (AbstractElement<?> child : this.childs) {
            child.setIs3dRender(is3dRender);
        }
        return this.genericThis;
    }

    public T setFrustumCulling(boolean enableFrustumCulling) {
        return this.setEnableFrustumCulling(enableFrustumCulling);
    }

    public T setEnableFrustumCulling(boolean enableFrustumCulling) {
        this.enableFrustumCulling = enableFrustumCulling;
        return this.genericThis;
    }

    public T setRenderSize(V3 renderSize) {
        this.renderSize = renderSize;
        return this.genericThis;
    }

    public boolean isHover() {
        return this.isHover;
    }

    public boolean isFocused() {
        return this.isFocused;
    }

    public T setFocused(boolean isFocused, AbstractElement<?> newFocusedElement) {
        if (this.isFocused == isFocused) {
            return this.genericThis;
        }
        this.isFocused = isFocused;
        this.fireEvent(new FocusedEvent(this, isFocused, newFocusedElement));
        return this.genericThis;
    }

    public Box updateAndGetBoundingBox() {
        V3 scale = this.getAbsoluteScale().multiply(new V3(-1.0, -1.0, 1.0));
        V3 size = this.renderSize.multiply(scale);
        V3 minPos = this.getPos();
        minPos = minPos.subtract(this.origin.multiply(size));
        V3 maxPos = minPos.add(size);
        this.boundingBox = new Box(minPos.getX(), minPos.getY(), minPos.getZ(), maxPos.getX(), maxPos.getY(), maxPos.getZ());
        return this.boundingBox;
    }

    public boolean isWorldContext() {
        return false;
    }

    public boolean isHideFromHierarchy() {
        if (this.hideFromHierarchy) {
            return true;
        }
        if (this.lastParent != null) {
            return this.lastParent.isHideFromHierarchy();
        }
        return false;
    }

    public T setHideFromHierarchy(boolean hideFromHierarchy) {
        this.hideFromHierarchy = hideFromHierarchy;
        return this.genericThis;
    }

    public void keyPress(int code) {
        this.fireEvent(new KeyPressEvent(this, code));
    }

    public T setLastParent(AbstractElement<?> lastParent) {
        if (this.lastParent == lastParent) {
            return this.genericThis;
        }
        this.lastParent = lastParent;
        this.markDirty();
        return this.genericThis;
    }

    public AbstractElement<?> getUpperParent() {
        return this.lastParent != null ? this.lastParent.getUpperParent() : this;
    }

    public V3 getAbsoluteScale() {
        return this.lastParent != null ? this.scale.multiply(this.lastParent.getAbsoluteScale()) : this.scale;
    }

    public V3 getAbsolutePos() {
        double angle = 0.0;
        V3 absolutePos = new V3();
        V3 absolutScale = this.getAbsoluteScale();
        if (this.lastParent != null) {
            V3 parentSize = this.lastParent.getSize();
            absolutePos = absolutePos.add(parentSize.multiply(this.align).multiply(absolutScale));
            angle = this.lastParent.getAbsoluteRotation().getY();
        }
        absolutePos = this.lastParent != null && this.lastParent.isWorldContext() ? absolutePos.add(this.pos).add(this.offset) : absolutePos.add(this.pos.multiply(new V3(-0.0625, -0.0625, 0.0625))).add(this.offset.multiply(new V3(-0.0625, -0.0625, 0.0625)));
        absolutePos = absolutePos.rotateY(angle);
        V3 absoluteSize = this.size.multiply(-1.0).multiply(this.origin).multiply(absolutScale).rotateY(this.getAbsoluteRotation().getY());
        absolutePos = absolutePos.add(absoluteSize);
        return this.lastParent != null ? absolutePos.add(this.lastParent.getAbsolutePos()) : absolutePos;
    }

    public V3 getAbsoluteRotation() {
        return this.lastParent != null ? this.rotation.add(this.lastParent.getAbsoluteRotation()) : this.rotation;
    }

    protected void childUpdate(AbstractElement<?> child) {
    }

    public <B extends AbstractEvent> void registerEvent(Class<B> eventClass, Consumer<B> consumer) {
        Set eventsSet = this.eventsMap.computeIfAbsent(eventClass, k -> new HashSet<>());
        eventsSet.add(consumer);
        if (InteractiveEvent.class.isAssignableFrom(eventClass)) {
            this.setInteractive(true);
        }
        if (ComponentEvent.class.isAssignableFrom(eventClass)) {
            Class<ComponentEvent> componentEventClass = (Class<ComponentEvent>) eventClass.asSubclass(ComponentEvent.class);
            Engine.getComponentManager().registerComponentEvent(componentEventClass, this);
        }
    }

    public void clearEventListeners(Class<? extends AbstractEvent> eventClass) {
        this.eventsMap.remove(eventClass);
        if (ComponentEvent.class.isAssignableFrom(eventClass)) {
            Class<ComponentEvent> componentEventClass = (Class<ComponentEvent>) eventClass.asSubclass(ComponentEvent.class);
            Engine.getComponentManager().removeComponentEvent(componentEventClass, this);
        }
    }

    public <B extends AbstractEvent> void fireEvent(B event) {
        Set<Consumer<? extends AbstractEvent>> eventsSet = this.eventsMap.get(event.getClass());
        if (eventsSet == null) {
            return;
        }
        for (Consumer<? extends AbstractEvent> consumer : eventsSet) {
            Consumer<B> accept = (Consumer<B>) consumer;
            accept.accept(event);
        }
    }

    public boolean mouseClick(InteractionManager interactionManager, int mouse) {
        if (!this.enabled) {
            return false;
        }
        for (AbstractElement<?> child : this.childs) {
            if (!child.mouseClick(interactionManager, mouse)) continue;
            return true;
        }
        if (!this.isHover) {
            return false;
        }
        if (mouse == 0) {
            this.isPressed = true;
            this.isLeftPressed = true;
            interactionManager.setFocusedElement(this);
            this.fireEvent(new MouseLeftClickEvent(this));
        } else if (mouse == 1) {
            this.isRightPressed = true;
            this.fireEvent(new MouseRightClickEvent(this));
        }
        this.fireEvent(new MouseClickEvent(this, mouse));
        return true;
    }

    public void mouseRelease(int mouse) {
        for (AbstractElement<?> child : this.childs) {
            child.mouseRelease(mouse);
        }
        switch (mouse) {
            case 0: {
                if (!this.isPressed && !this.isLeftPressed) break;
                this.isPressed = false;
                this.isLeftPressed = false;
                this.fireEvent(new MouseLeftReleaseEvent(this));
                break;
            }
            case 1: {
                if (!this.isRightPressed) break;
                this.isRightPressed = false;
                this.fireEvent(new MouseRightReleaseEvent(this));
                break;
            }
        }
    }

    public void wheel(int dWheel) {
        if (this.isHover()) {
            this.fireEvent(new MouseWheelEvent(this, dWheel));
        }
        for (AbstractElement<?> child : this.childs) {
            child.wheel(dWheel);
        }
    }

    public void transformAndRender(MatrixStack stack, @Nullable AbstractElement<?> parent, double partialTicks, double mouseX, double mouseY) {
        this.transformAndRender(stack, parent, partialTicks, mouseX, mouseY, true);
    }

    public void transformAndRender(MatrixStack stack,@Nullable AbstractElement<?> parent, double partialTicks, double mouseX, double mouseY, boolean isInteractive) {
        this.transformAndRender(stack,parent, partialTicks, mouseX, mouseY, isInteractive, Engine.getInteractionManager());
    }

    public void transformAndRender(MatrixStack stack, @Nullable AbstractElement<?> parent, double partialTicks, double mouseX, double mouseY, boolean isInteractive, InteractionManager interactionManager) {
        if (!this.enabled) {
            this.handleDisabledState();
            return;
        }
        this.updateLastState(parent, interactionManager);
        if (this.shouldSkipWorldRender()) {
            return;
        }
        this.preTransform();
        this.transform();
        this.updateHoverState(interactionManager, mouseX, mouseY, isInteractive);
        this.preRender(stack, partialTicks, mouseX, mouseY);
        this.render(stack, partialTicks, mouseX, mouseY);
        this.postRender(stack,partialTicks, mouseX, mouseY, isInteractive, interactionManager);
        this.postTransform();
    }

    private void handleDisabledState() {
        if (this.isHover) {
            this.setHoverState(false);
        }
        if (this.isFocused) {
            Engine.getInteractionManager().setFocusedElement(null);
            this.setFocused(false, null);
        }
        if (this.transformIfDisabled && this.renderLayer > 0L) {
            this.updateTransform();
            this.transformIfDisabled = false;
        }
    }

    public void updateTransform() {
        this.preTransform();
        this.transform();
        this.postTransform();
    }

    private void updateLastState(@Nullable AbstractElement<?> parent, InteractionManager interactionManager) {
        this.setLastParent(parent);
        this.lastInteractionManager = interactionManager;
    }

    private boolean shouldSkipWorldRender() {
        if (this.lastParent != null && this.lastParent.isWorldContext() && this.worldRenderDistance >= 0.0) {
            MutableV3 playerPos = Engine.getGameSettings().getInterpolatedPlayerPos();
            return this.pos.squaredDistance(playerPos) >= this.worldRenderDistance * this.worldRenderDistance;
        }
        return false;
    }

    public void setHoverState(boolean hoverState) {
        if (this.isHover == hoverState) {
            return;
        }
        this.isHover = hoverState;
        this.fireEvent(new HoverEvent(this, this.isHover));
    }

    public void updateHoverState(InteractionManager interactionManager, double mouseX, double mouseY, boolean isInteractive) {
        boolean newHover;
        AbstractElement<?> upperParent = this.getUpperParent();
        if (!(interactionManager.isAlwaysAllowHover() || this.isInteractive() && isInteractive)) {
            newHover = false;
        } else {
            if (upperParent != null && upperParent.isWorldContext()) {
                this.setHoverState(this.getHover3d());
                return;
            }
            newHover = this.getHover2d(mouseX, mouseY);
        }
        if (newHover) {
            interactionManager.attemptSetHoveredElement(this, this.getHierarchyDeep());
            return;
        }
        this.setHoverState(false);
    }

    public boolean getHover2d(double mouseX, double mouseY) {
        V3 min = GLUtils.getRenderPos();
        V3 max = min.add(this.size.multiply(this.getAbsoluteScale()));
        return mouseX >= min.getX() && mouseX <= max.getX() && mouseY >= min.getY() && mouseY <= max.getY() && mc.isWindowFocused();
    }

    public boolean getHover3d() {
        Vec3d ray = player.getRotationVector();
        V3 point1 = new V3(player.getX(), player.getY() + (double)player.getStandingEyeHeight(), player.getZ());
        V3 point2 = point1.add(ray.getX() * 8.0, ray.getY() * 8.0, ray.getZ() * 8.0);
        V3 size = this.getSize().multiply(this.getAbsoluteScale());
        V3 square1 = new V3();
        V3 square2 = square1.add(size.getX(), 0.0, 0.0);
        V3 square3 = square1.add(0.0, size.getY(), 0.0);
        V3 absolutePos = this.getAbsolutePos();
        double angle = this.getAbsoluteRotation().getY();
        square1 = square1.rotateY(angle).add(absolutePos);
        square2 = square2.rotateY(angle).add(absolutePos);
        square3 = square3.rotateY(angle).add(absolutePos);
        return MathUtil.intersectRayWithSquare(point1, point2, square1, square2, square3);
    }

    protected void preRender(MatrixStack stack, double partialTicks, double mouseX, double mouseY) {
        this.fireEvent(new PreRenderEvent(this));
        if (glowing) {
            SimpleDrawer.drawGlow(0,4, (float) this.getSize().getX(), (float) this.getSize().getY() - 8, 20,  -1);
        }
        if (this.tooltip != null && this.isHover()) {
            if (this.tooltip.isTooltipChatScreenOnly() && !(mc.currentScreen instanceof ChatScreen)) {
                return;
            }
            Engine.getCustomTooltip().render(this.tooltip);
        }
    }

    public abstract void render(MatrixStack stack, double var1, double var3, double var5);

    public void postRender(MatrixStack stack,double partialTicks, double mouseX, double mouseY, boolean isInteractive, InteractionManager interactionManager) {
        for (AbstractElement<?> child : new ArrayList<>(this.childs)) {
            child.transformAndRender(stack,this, partialTicks, mouseX, mouseY, isInteractive, interactionManager);
        }

        this.fireEvent(new PostRenderEvent(this));
    }

    public void drawDebugBoundingBox() {
        if (!this.enabled && !this.renderDebugBoxIfDisabled) {
            return;
        }
        if (this.boundingBox == null || !mc.getEntityRenderDispatcher().shouldRenderHitboxes()) {
            return;
        }
        GlStateManager.depthMask(false);
        GlStateManager.disableTexture();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.pushMatrix();
        GlStateManager.scalef(-16.0f, -16.0f, 16.0f);
        Box box = this.boundingBox;
        Palette.WHITE.apply();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(3, VertexFormats.POSITION);
        buffer.vertex(box.minX, box.minY, box.minZ).next();
        buffer.vertex(box.maxX, box.minY, box.minZ).next();
        buffer.vertex(box.maxX, box.minY, box.maxZ).next();
        buffer.vertex(box.minX, box.minY, box.maxZ).next();
        buffer.vertex(box.minX, box.minY, box.minZ).next();
        tessellator.draw();
        buffer.begin(3, VertexFormats.POSITION);
        buffer.vertex(box.minX, box.maxY, box.minZ).next();
        buffer.vertex(box.maxX, box.maxY, box.minZ).next();
        buffer.vertex(box.maxX, box.maxY, box.maxZ).next();
        buffer.vertex(box.minX, box.maxY, box.maxZ).next();
        buffer.vertex(box.minX, box.maxY, box.minZ).next();
        tessellator.draw();
        buffer.begin(1, VertexFormats.POSITION);
        buffer.vertex(box.minX, box.minY, box.minZ).next();
        buffer.vertex(box.minX, box.maxY, box.minZ).next();
        buffer.vertex(box.maxX, box.minY, box.minZ).next();
        buffer.vertex(box.maxX, box.maxY, box.minZ).next();
        buffer.vertex(box.maxX, box.minY, box.maxZ).next();
        buffer.vertex(box.maxX, box.maxY, box.maxZ).next();
        buffer.vertex(box.minX, box.minY, box.maxZ).next();
        buffer.vertex(box.minX, box.maxY, box.maxZ).next();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.enableTexture();
        GlStateManager.enableCull();
        GlStateManager.enableBlend();
        GlStateManager.depthMask(true);
    }

    protected void drawDebugLayers(MatrixStack stack, double mouseX, double mouseY) {
        boolean hover;
        GlStateManager.lineWidth(2.0f);
        GlStateManager.color4f(1.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.disableTexture();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(3, VertexFormats.POSITION);
        buffer.vertex(0.0, 0.0, 0.0).next();
        buffer.vertex(this.size.getX(), 0.0, 0.0).next();
        buffer.vertex(this.size.getX(), this.size.getY(), 0.0).next();
        buffer.vertex(0.0, this.size.getY(), 0.0).next();
        buffer.vertex(0.0, 0.0, 0.0).next();
        tessellator.draw();
        GL11.glPointSize(4.0f);
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        buffer.begin(0, VertexFormats.POSITION_COLOR);
        buffer.vertex(this.size.getX() * this.align.getX(), this.size.getY() * this.align.getY(), 0.0).color(0, 0, 255, 255).next();
        buffer.vertex(this.size.getX() * this.origin.getX(), this.size.getY() * this.origin.getY(), 0.0).color(0, 255, 0, 255).next();
        tessellator.draw();
        GlStateManager.enableTexture();
        AbstractElement<?> upperParent = this.getUpperParent();
        boolean bl = hover = (upperParent == null || !upperParent.isWorldContext()) && this.getHover2d(mouseX, mouseY);
        if (hover) {
            GlStateManager.pushMatrix();
            GlStateManager.scalef(2.0f, 2.0f, 2.0f);
            GlStateManager.translatef(0.0f, 0.0f, 50.0f);
            TextRenderer fontRenderer = mc.textRenderer;
            fontRenderer.drawWithShadow(stack,String.valueOf(this.getHierarchyDeep()), 0.0f, 0.0f, Palette.WHITE.getDecimal(), true);
            GlStateManager.popMatrix();
        }
        if (this.isHover()) {
            String debugText = this + "\nHierarchy Index: " + this.getHierarchyDeep() + "\nRender Layer: " + this.getTotalRenderLayer() + "\nRender Pos: " + GLUtils.getRenderPos() + "\nisInteractive: " + this.isInteractive() + "\nhoverLayer: " + this.getHoverLayer();
            Engine.getCustomTooltip().render(new TooltipData(Collections.singleton(debugText), ""));
        }
    }
    protected void transform() {
        boolean needRound;
        boolean isWorldRender;
        if (!this.isDirty) {
            GLUtils.loadModelViewMatrix(this.bakedMatrix);
            AbstractElement<?> upperParent = this.getUpperParent();
            if (upperParent == null || !upperParent.isWorldContext()) {
            }
            return;
        }
        double translateX = 0.0;
        double translateY = 0.0;
        double translateZ = 0.0;
        if (this.lastParent != null) {
            V3 parentSize = this.lastParent.getSize();
            translateX += parentSize.getX() * this.align.getX();
            translateY += parentSize.getY() * this.align.getY();
            translateZ += parentSize.getZ() * this.align.getZ();
        }
        AbstractElement<?> upperParent = this.getUpperParent();
        isWorldRender = this.lastParent != null && this.lastParent.isWorldContext();
        if (isWorldRender) {
            translateX += (this.pos.getX() + this.offset.getX()) * -16.0;
            translateY += (this.pos.getY() + this.offset.getY()) * -16.0;
            translateZ += (this.pos.getZ() + this.offset.getZ()) * 16.0;
        } else {
            translateX += this.pos.getX() + this.offset.getX();
            translateY += this.pos.getY() + this.offset.getY();
            translateZ += this.pos.getZ() + this.offset.getZ();
            if (upperParent == null || !upperParent.isWorldContext()) {
            }
        }
        needRound = upperParent != null && upperParent.isWorldContext();
        if (needRound) {
            GlStateManager.translated(translateX, translateY, translateZ);
        } else {
            GlStateManager.translatef(Math.round(translateX), Math.round(translateY), Math.round(translateZ));
        }
        GlStateManager.scaled(this.scale.getX(), this.scale.getY(), this.scale.getZ());
        GlStateManager.rotatef((float)this.rotation.getY(), 0.0f, 1.0f, 0.0f);
        GlStateManager.rotatef((float)this.rotation.getX(), 1.0f, 0.0f, 0.0f);
        GlStateManager.rotatef((float)this.rotation.getZ(), 0.0f, 0.0f, 1.0f);
        if (needRound) {
            GlStateManager.translated(-this.size.getX() * this.origin.getX(), -this.size.getY() * this.origin.getY(), -this.size.getZ() * this.origin.getZ());
        } else {
            GlStateManager.translatef(Math.round(-this.size.getX() * this.origin.getX()), Math.round(-this.size.getY() * this.origin.getY()), Math.round(-this.size.getZ() * this.origin.getZ()));
        }
        GLUtils.getModelViewMatrix().get(this.bakedMatrix);
        this.isDirty = isWorldRender || this.is3dRender();
    }
    protected void preTransform() {
        this.fireEvent(new PreTransformEvent(this));
        GlStateManager.pushMatrix();
    }
    public void postTransform() {
        GlStateManager.popMatrix();
        this.fireEvent(new PostTransformEvent(this));
    }

    public void preTick() {
        this.fireEvent(new PreTickEvent(this));
        for (AbstractElement<?> child : this.childs) {
            child.preTick();
        }
    }

    public void postTick() {
        this.fireEvent(new PostTickEvent(this));
        for (AbstractElement<?> child : this.childs) {
            child.postTick();
        }
    }

    public void pasteText(String text) {
    }

    public T copy(T element) {
        element.setId(this.getId()).setPos(this.getPos()).setOrigin(this.getOrigin()).setAlign(this.getAlign()).setSize(this.getSize()).setScale(this.getScale()).setRotation(this.getRotation()).setColor(this.getColor()).setAlpha(this.getAlpha()).setEnabled(this.isEnabled()).setInteractive(this.isInteractive()).setIs3dRender(this.is3dRender()).setTooltip(this.getTooltip()).setRenderLayer(this.getRenderLayer()).setHideFromHierarchy(this.isHideFromHierarchy());
        for (AbstractElement<?> child : this.getChilds()) {
            if (child.isHideFromHierarchy()) continue;
            ((AbstractElement<?>)element).addChild(child.clone());
        }
        return element;
    }

    public T clone() {
        return this.copy((T) new Container());
    }

    @Override
    public V3 getPos() {
        return this.pos;
    }

    @Override
    public V3 getOffset() {
        return this.offset;
    }

    @Override
    public V3 getSize() {
        return this.size;
    }

    @Override
    public V3 getRotation() {
        return this.rotation;
    }

}
