package dev.forward.casino.util.render;
import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.util.FastAccess;
import dev.forward.casino.util.math.Matrix4f;
import dev.forward.casino.util.math.V3;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class GLUtils implements FastAccess {
    private static final IntBuffer TEMP_INT_BUFFER = GLAllocation.createDirectIntBuffer(64);
    private static final FloatBuffer TEMP_FLOAT_BUFFER = GLAllocation.createDirectFloatBuffer(64);
    private static final FloatBuffer MODEL_VIEW_MATRIX = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer PROJECT_MATRIX = GLAllocation.createDirectFloatBuffer(16);
    private static final List<Map.Entry<V3, V3>> SCISSOR_LIST = new ArrayList<Map.Entry<V3, V3>>();

    public static FloatBuffer wrapFloatBuffer(float[] array) {
        FloatBuffer buffer = GLAllocation.createDirectFloatBuffer(array.length);
        buffer.put(array);
        buffer.flip();
        return buffer;
    }

    public static FloatBuffer wrapTempFloatBuffer(float[] array) {
        TEMP_FLOAT_BUFFER.clear();
        TEMP_FLOAT_BUFFER.put(array);
        TEMP_FLOAT_BUFFER.flip();
        return TEMP_FLOAT_BUFFER;
    }

    public static IntBuffer wrapIntBuffer(int[] array) {
        IntBuffer buffer = GLAllocation.createDirectIntBuffer(array.length);
        buffer.put(array);
        buffer.flip();
        return buffer;
    }

    public static IntBuffer wrapTempIntBuffer(int[] array) {
        TEMP_INT_BUFFER.clear();
        TEMP_INT_BUFFER.put(array);
        TEMP_INT_BUFFER.flip();
        return TEMP_INT_BUFFER;
    }

    public static void checkFramebufferComplete() {
        int i = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
        if (i != GL30.GL_FRAMEBUFFER_COMPLETE) {
            if (i == GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
            } else if (i == GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
            } else if (i == GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER");
            } else if (i == GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER");
            } else {
                throw new RuntimeException("glCheckFramebufferStatus returned unknown status:" + i);
            }
        }
    }

    public static void unbindFrameBuffer() {
        mc.getFramebuffer().endWrite();
    }

    public static void clearBuffer() {
        GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GlStateManager.clearDepth(1.0);
        GlStateManager.clear(17664, false);
    }

    public static FloatBuffer getModelViewMatrix() {
        MODEL_VIEW_MATRIX.clear();
        GL11.glGetFloatv(2982, MODEL_VIEW_MATRIX);
        return MODEL_VIEW_MATRIX;
    }

    public static void loadModelViewMatrix(float[] matrix) {
        GL11.glLoadMatrixf(GLUtils.wrapFloatBuffer(matrix));
    }

    public static FloatBuffer getProjectionMatrix() {
        PROJECT_MATRIX.clear();
        GL11.glGetFloatv(2983, PROJECT_MATRIX);
        return PROJECT_MATRIX;
    }

    public static boolean isScissorEnabled() {
        return !SCISSOR_LIST.isEmpty();
    }

    public static void enableScissor(V3 startPos, V3 endPos) {
        Window window = mc.getWindow();
        double scaleFactor = window.getScaleFactor();
        if (scaleFactor == 1 && window.getScaledWidth() == 1 && window.getScaledHeight() == 1) {
            return;
        }
        V3 currentRenderPos = GLUtils.getRenderPos();
        startPos = startPos.add(currentRenderPos);
        endPos = endPos.add(currentRenderPos);
        if (!SCISSOR_LIST.isEmpty()) {
            Map.Entry<V3, V3> lastScissor = SCISSOR_LIST.get(SCISSOR_LIST.size() - 1);
            V3 minPos = lastScissor.getKey();
            V3 maxPos = lastScissor.getValue();
            startPos = startPos.setX(Math.min(Math.max(startPos.getX(), minPos.getX()), maxPos.getX())).setY(Math.min(Math.max(startPos.getY(), minPos.getY()), maxPos.getY()));
            endPos = endPos.setX(Math.min(Math.max(endPos.getX(), minPos.getX()), maxPos.getX())).setY(Math.min(Math.max(endPos.getY(), minPos.getY()), maxPos.getY()));
        }
        SCISSOR_LIST.add(new AbstractMap.SimpleEntry<V3, V3>(startPos, endPos));
        endPos = endPos.subtract(startPos).multiply(scaleFactor);
        startPos = startPos.multiply(scaleFactor);
        startPos = startPos.setY((int)(window.getHeight() - endPos.getY() - startPos.getY()));
        GL11.glEnable(3089);
        GL11.glScissor((int)startPos.getX(), (int)startPos.getY(), (int)endPos.getX() + 4, (int)endPos.getY() + 4);
    }

    public static void disableScissor() {
        if (SCISSOR_LIST.isEmpty()) {
            return;
        }
        Window window = mc.getWindow();
        double scaleFactor = window.getScaleFactor();
        if (scaleFactor == 1 && window.getScaledWidth() == 1 && window.getScaledHeight() == 1) {
            return;
        }
        SCISSOR_LIST.remove(SCISSOR_LIST.size() - 1);
        if (SCISSOR_LIST.isEmpty()) {
            GL11.glDisable(3089);
        } else {
            GL11.glEnable(3089);
            Map.Entry<V3, V3> lastScissor = SCISSOR_LIST.get(SCISSOR_LIST.size() - 1);
            V3 minPos = lastScissor.getKey();
            V3 maxPos = lastScissor.getValue();
            maxPos = maxPos.subtract(minPos).multiply(scaleFactor);
            minPos = minPos.multiply(scaleFactor);
            minPos = minPos.setY((int)(mc.getWindow().getHeight() - maxPos.getY() - minPos.getY()));
            GL11.glScissor((int)minPos.getX(), (int)minPos.getY(), (int)maxPos.getX() + 4, (int)maxPos.getY() + 4);
        }
    }

    public static V3 getMousePos() {
        int i = (int)(mc.mouse.getX() * (double)mc.getWindow().getScaledWidth() / (double)mc.getWindow().getWidth());
        int j = (int)(mc.mouse.getY() * (double)mc.getWindow().getScaledHeight() / (double)mc.getWindow().getHeight());
//        ScaledResolution resolution = Minecraft.getInstance().getResolution();
//        int scaleFactor = resolution.getScaleFactor();
//        double mouseX = (double) Minecraft.getInstance().mouseHelper.getMouseX() / (double)scaleFactor;
//        double mouseY = (double) resolution.getScaledHeight() - (double) Minecraft.getInstance().mouseHelper.getMouseY() / (double)scaleFactor;
        return new V3(i, j, 0.0);
    }

    public static V3 getRenderPos() {
        Matrix4f mat = new Matrix4f();
        mat.load(GLUtils.getModelViewMatrix());
        return new V3(mat.m30, mat.m31, mat.m32);
    }
}
