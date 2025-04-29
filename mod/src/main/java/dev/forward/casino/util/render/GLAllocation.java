package dev.forward.casino.util.render;

import org.lwjgl.opengl.GL20;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class GLAllocation {
    private GLAllocation() {
    }

    public static void deleteDisplayLists(int var0, int var1) {
        GL20.glDeleteLists(var0, var1);
    }

    public static void deleteDisplayLists(int var0) {
        deleteDisplayLists(var0, 1);
    }

    public static ByteBuffer createDirectByteBuffer(int var0) {
        return BufferUtil.createDirectByteBuffer(var0);
    }

    public static IntBuffer createDirectIntBuffer(int var0) {
        return BufferUtil.createDirectIntBuffer(var0);
    }

    public static FloatBuffer createDirectFloatBuffer(int var0) {
        return BufferUtil.createDirectFloatBuffer(var0);
    }

    public static void freeBuffer(Buffer var0) {
    }

    public static ByteBuffer nextBuffer(ByteBuffer var0, int var1, int var2) {
        int var3 = ((Buffer)var0).limit();
        var0.position(var3).limit(var3 + var1 * var2);
        return var0;
    }
}
