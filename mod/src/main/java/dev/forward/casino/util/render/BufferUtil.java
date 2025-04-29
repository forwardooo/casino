//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.forward.casino.util.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;


public final class BufferUtil {
    private static final ByteOrder ORDER = ByteOrder.nativeOrder();

    public static ByteBuffer createDirectByteBuffer(int cap) {
        return ByteBuffer.allocateDirect(cap).order(ORDER);
    }

    public static ShortBuffer createDirectShortBuffer(int cap) {
        return createDirectByteBuffer(cap << 1).asShortBuffer();
    }

    public static CharBuffer createDirectCharBuffer(int cap) {
        return createDirectByteBuffer(cap << 1).asCharBuffer();
    }

    public static IntBuffer createDirectIntBuffer(int cap) {
        return createDirectByteBuffer(cap << 2).asIntBuffer();
    }

    public static FloatBuffer createDirectFloatBuffer(int cap) {
        return createDirectByteBuffer(cap << 2).asFloatBuffer();
    }

    public static DoubleBuffer createDirectDoubleBuffer(int cap) {
        return createDirectByteBuffer(cap << 3).asDoubleBuffer();
    }

    public static LongBuffer createDirectLongBuffer(int cap) {
        return createDirectByteBuffer(cap << 3).asLongBuffer();
    }

//    public static long bufferAddress(ByteBuffer buffer) {
//        return ((DirectBuffer)buffer).address() + (long)buffer.position();
//    }
//
//    public static long bufferAddress(LongBuffer buffer) {
//        return ((DirectBuffer)buffer).address() + (long)buffer.position();
//    }
//
//    public static long bufferAddress(DoubleBuffer buffer) {
//        return ((DirectBuffer)buffer).address() + (long)buffer.position();
//    }
//
//    public static long bufferAddress(IntBuffer buffer) {
//        return ((DirectBuffer)buffer).address() + (long)buffer.position();
//    }
//
//    public static long bufferAddress(FloatBuffer buffer) {
//        return ((DirectBuffer)buffer).address() + (long)buffer.position();
//    }
//
//    public static long bufferAddress(ShortBuffer buffer) {
//        return ((DirectBuffer)buffer).address() + (long)buffer.position();
//    }
//
//    public static long bufferAddress0(ByteBuffer buffer) {
//        return ((DirectBuffer)buffer).address();
//    }
//
//    public static long bufferAddress0(LongBuffer buffer) {
//        return ((DirectBuffer)buffer).address();
//    }
//
//    public static long bufferAddress0(DoubleBuffer buffer) {
//        return ((DirectBuffer)buffer).address();
//    }
//
//    public static long bufferAddress0(IntBuffer buffer) {
//        return ((DirectBuffer)buffer).address();
//    }
//
//    public static long bufferAddress0(FloatBuffer buffer) {
//        return ((DirectBuffer)buffer).address();
//    }
//
//    public static long bufferAddress0(ShortBuffer buffer) {
//        return ((DirectBuffer)buffer).address();
//    }
//
//    public static long bufferAddressSafe(ByteBuffer buffer) {
//        return buffer == null ? 0L : ((DirectBuffer)buffer).address() + (long)buffer.position();
//    }
//
//    public static long bufferAddressSafe(LongBuffer buffer) {
//        return buffer == null ? 0L : ((DirectBuffer)buffer).address() + (long)buffer.position();
//    }
//
//    public static long bufferAddressSafe(DoubleBuffer buffer) {
//        return buffer == null ? 0L : ((DirectBuffer)buffer).address() + (long)buffer.position();
//    }
//
//    public static long bufferAddressSafe(IntBuffer buffer) {
//        return buffer == null ? 0L : ((DirectBuffer)buffer).address() + (long)buffer.position();
//    }
//
//    public static long bufferAddressSafe(FloatBuffer buffer) {
//        return buffer == null ? 0L : ((DirectBuffer)buffer).address() + (long)buffer.position();
//    }
//
//    public static long bufferAddressSafe(ShortBuffer buffer) {
//        return buffer == null ? 0L : ((DirectBuffer)buffer).address() + (long)buffer.position();
//    }

    public static ByteBuffer nextSlice(ByteBuffer byteBuffer, int size, int componentSize) {
        int i = byteBuffer.limit();
        byteBuffer.position(i).limit(i + size * componentSize);
        return byteBuffer;
    }

    private BufferUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
