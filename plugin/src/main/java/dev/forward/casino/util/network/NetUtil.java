package dev.forward.casino.util.network;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import org.apache.logging.log4j.util.TriConsumer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public final class NetUtil {
    private static final int[] VARINT_EXACT_BYTE_LENGTHS;

    public static int readVarInt(ByteBuf input) {
        int result = readVarIntSafely(input);
        if (result == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Buffer is empty!");
        }

        return result;
    }

    public static int readVarIntSafely(ByteBuf buf) {
        int i = 0;
        int maxRead = Math.min(5, buf.readableBytes());

        for(int j = 0; j < maxRead; ++j) {
            int k = buf.readByte();
            i |= (k & 127) << j * 7;
            if ((k & 128) != 128) {
                return i;
            }
        }

        return Integer.MIN_VALUE;
    }

    public static long readVarLong(ByteBuf input) {
        long out = 0L;
        int bytes = 0;
        int readable = input.readableBytes();

        byte in;
        do {
            if (readable-- == 0) {
                throw new IllegalArgumentException("Buffer is empty!");
            }

            in = input.readByte();
            out |= (long)(in & 127) << bytes++ * 7;
            if (bytes > 10) {
                throw new IllegalArgumentException("Long to big!");
            }
        } while((in & 128) == 128);

        return out;
    }

    public static void write21BitVarInt(int value, ByteBuf output) {
        int w = (value & 127 | 128) << 16 | (value >>> 7 & 127 | 128) << 8 | value >>> 14;
        output.writeMedium(w);
    }

    public static void write21BitVarInt(ByteBuf output, int value) {
        write21BitVarInt(value, output);
    }

    public static void writeVarInt(int value, ByteBuf output) {
        if ((value & -128) == 0) {
            output.writeByte(value);
        } else if ((value & -16384) == 0) {
            int w = (value & 127 | 128) << 8 | value >>> 7;
            output.writeShort(w);
        } else {
            writeVarIntFull(output, value);
        }

    }

    public static void writeVarInt(ByteBuf output, int value) {
        writeVarInt(value, output);
    }

    public static void writeVarLong(long value, ByteBuf output) {
        while(true) {
            long bits = value & -128L;
            if (bits == 0L) {
                output.writeByte((int)value);
                return;
            }

            output.writeByte((int)(value & 127L) | 128);
            value >>>= 7;
        }
    }

    public static void writeVarLong(ByteBuf output, long value) {
        writeVarLong(value, output);
    }

    public static UUID readId(ByteBuf input) {
        return new UUID(input.readLong(), input.readLong());
    }

    public static void writeId(UUID id, ByteBuf output) {
        output.ensureWritable(16).writeLong(id.getMostSignificantBits()).writeLong(id.getLeastSignificantBits());
    }

    public static void writeId(ByteBuf output, UUID id) {
        writeId(id, output);
    }

    public static byte[] readArray(ByteBuf input, int limit) {
        int len = readVarInt(input);
        if (len > limit) {
            throw new IllegalArgumentException("Cannot receive byte array longer than " + limit + " (got " + len + " bytes)");
        } else {
            byte[] bytes = new byte[len];
            input.readBytes(bytes);
            return bytes;
        }
    }

    public static byte[] readArray(ByteBuf input) {
        return readArray(input, input.readableBytes());
    }

    public static void writeArray(byte[] array, int start, int end, ByteBuf output) {
        writeVarInt(end - start, output);
        output.writeBytes(array, start, end);
    }

    public static void writeArray(byte[] array, ByteBuf output) {
        writeVarInt(array.length, output);
        output.writeBytes(array);
    }

    public static void writeArray(ByteBuf output, byte[] array) {
        writeArray(array, output);
    }

    public static ByteBuf readBuffer(ByteBuf input, int limit) {
        int len = readVarInt(input);
        if (len > limit) {
            throw new IllegalArgumentException("Cannot read buffer longer than " + limit + " (got " + len + " bytes)");
        } else {
            return input.readBytes(len);
        }
    }

    public static void writeBuffer(ByteBuf buf, ByteBuf output) {
        int readable = buf.readableBytes();
        output.ensureWritable(varIntSize(readable) + readable);
        writeVarInt(readable, output);
        int idx = buf.readerIndex();
        output.writeBytes(buf, idx, readable);
        buf.readerIndex(idx + readable);
    }

    public static void writeBuffer(ByteBuf buf, int start, int end, ByteBuf output) {
        writeVarInt(end - start, output);
        output.writeBytes(buf, start, end);
    }
    public static String readUtf8(ByteBuf input, int limit) {
        return readString0(input, limit, 4, StandardCharsets.UTF_8);
    }

    public static String readUtf8(ByteBuf input) {
        return readString0(input, Integer.MAX_VALUE, 4, StandardCharsets.UTF_8);
    }

    public static void writeUtf8(String s, ByteBuf output) {
        int size = ByteBufUtil.utf8Bytes(s);
        writeVarInt(size, output);
        output.writeCharSequence(s, StandardCharsets.UTF_8);
    }

    public static void writeUtf8(ByteBuf output, String s) {
        writeUtf8(s, output);
    }

    public static String readAscii(ByteBuf input, int limit) {
        return readString0(input, limit, 1, StandardCharsets.US_ASCII);
    }

    public static String readAscii(ByteBuf input) {
        return readAscii(input, Integer.MAX_VALUE);
    }

    public static void writeAscii(String s, ByteBuf output) {
        int size = s.length();
        writeVarInt(size, output);
        output.writeCharSequence(s, StandardCharsets.US_ASCII);
    }

    public static void writeAscii(ByteBuf output, String s) {
        writeAscii(s, output);
    }

    public static Property readProperty(ByteBuf input, int propertyNameLimit, int propertyValueLimit, int propertySignatureLimit) {
        String name = readUtf8(input, propertyNameLimit);
        String value = readUtf8(input, propertyValueLimit);
        return input.readBoolean() ? new Property(name, value, readUtf8(input, propertySignatureLimit)) : new Property(name, value);
    }

    public static GameProfile readGameProfile(ByteBuf input, int nameLimit, int propertyNameLimit, int propertyValueLimit, int propertySignatureLimit) {
        GameProfile profile = new GameProfile(readId(input), readUtf8(input, nameLimit));
        PropertyMap properties = profile.getProperties();
        int i = 0;

        for(int j = readVarInt(input); i < j; ++i) {
            Property property = readProperty(input, propertyNameLimit, propertyValueLimit, propertySignatureLimit);
            properties.put(property.getName(), property);
        }

        return profile;
    }

    public static GameProfile readGameProfile(ByteBuf input, int nameLimit) {
        return readGameProfile(input, nameLimit, 32767, 32767, 32767);
    }

    public static GameProfile readGameProfile(ByteBuf input) {
        return readGameProfile(input, 16);
    }

    public static void writeProperty(Property property, ByteBuf output) {
        writeUtf8(property.getName(), output);
        writeUtf8(property.getValue(), output);
        if (property.hasSignature()) {
            output.writeBoolean(true);
            writeUtf8(property.getSignature(), output);
        } else {
            output.writeBoolean(false);
        }

    }

    public static void writeProperty(ByteBuf output, Property property) {
        writeProperty(property, output);
    }

    public static void writeGameProfile(GameProfile profile, ByteBuf output) {
        writeId(profile.getId(), output);
        writeUtf8(profile.getName(), output);
        Collection<Property> properties = profile.getProperties().values();
        writeCollection(properties, NetUtil::writeProperty, output);
    }

    public static void writeGameProfile(ByteBuf output, GameProfile profile) {
        writeGameProfile(profile, output);
    }

    public static Date readDate(ByteBuf input) {
        return new Date(input.readLong());
    }

    public static void writeDate(Date date, ByteBuf output) {
        output.writeLong(date.getTime());
    }

    public static void writeDate(ByteBuf output, Date date) {
        writeDate(date, output);
    }

    public static List<String> readUtf8s(ByteBuf input, int climit, int slimit) {
        int count = readVarInt(input);
        if (count > climit) {
            throw new IllegalArgumentException("Cannot read amount of UTF-8s greater than " + climit + " (got " + count + " strings)");
        } else {
            ArrayList<String> list = new ArrayList(count);

            while(count-- > 0) {
                list.add(readUtf8(input, slimit));
            }

            return list;
        }
    }

    public static void writeUtf8s(List<String> strings, ByteBuf output) {
        int j = strings.size();
        writeVarInt(j, output);
        if (j != 0) {
            if (strings instanceof RandomAccess) {
                for(int i = 0; i < j; ++i) {
                    writeUtf8((String)strings.get(i), output);
                }
            } else {
                for(String str : strings) {
                    writeUtf8(str, output);
                }
            }
        }

    }

    public static void writeUtf8s(ByteBuf output, List<String> strings) {
        writeUtf8s(strings, output);
    }

    public static int[] readVarIntArray(ByteBuf input, int[] into, int limit) {
        int count = readVarInt(input);
        if (count > limit) {
            throw new IllegalArgumentException("Cannot read amount of ints greater than " + limit + " (got " + count + " ints)");
        } else {
            if (into == null) {
                into = new int[count];
            }

            for(int i = 0; i < count; ++i) {
                into[i] = readVarInt(input);
            }

            return into;
        }
    }

    public static int[] readVarIntArray(ByteBuf input, int limit) {
        return readVarIntArray(input, (int[])null, limit);
    }

    public static int[] readVarIntArray(ByteBuf input, int[] into) {
        return readVarIntArray(input, into, into.length);
    }

    public static int[] readVarIntArray(ByteBuf input) {
        return readVarIntArray(input, (int[])null, Integer.MAX_VALUE);
    }

    public static void writeVarIntArray(int[] ints, int from, int to, ByteBuf output) {
        int count = to - from;
        output.ensureWritable((to - from) * 4);
        writeVarInt(count, output);

        while(from < to) {
            writeVarInt(ints[from++], output);
        }

    }

    public static void writeVarIntArray(int[] ints, ByteBuf output) {
        int j = ints.length;
        output.ensureWritable((j + 1) * 4);
        writeVarInt(j, output);

        for(int i = 0; i < j; ++i) {
            writeVarInt(ints[i], output);
        }

    }

    public static void writeVarIntArray(ByteBuf output, int[] ints) {
        writeVarIntArray(ints, output);
    }

    public static long[] readLongArray(ByteBuf input, long[] into, int limit) {
        int count = readVarInt(input);
        if (count > limit) {
            throw new IllegalArgumentException("Cannot read amount of longs greater than " + limit + " (got " + count + " longs)");
        } else {
            if (into == null) {
                into = new long[count];
            }

            for(int i = 0; i < count; ++i) {
                into[i] = input.readLong();
            }

            return into;
        }
    }

    public static long[] readLongArray(ByteBuf input, int limit) {
        return readLongArray(input, (long[])null, limit);
    }

    public static long[] readLongArray(ByteBuf input, long[] into) {
        return readLongArray(input, into, into.length);
    }

    public static long[] readLongArray(ByteBuf input) {
        return readLongArray(input, (long[])null, Integer.MAX_VALUE);
    }

    public static void writeLongArray(long[] longs, int from, int to, ByteBuf output) {
        int count = to - from;
        output.ensureWritable(4 + count * 8);
        writeVarInt(count, output);

        while(from < to) {
            output.writeLong(longs[from++]);
        }

    }

    public static void writeLongArray(long[] longs, ByteBuf output) {
        int j = longs.length;
        output.ensureWritable(4 + j * 8);
        writeVarInt(j, output);

        for(int i = 0; i < j; ++i) {
            output.writeLong(longs[i]);
        }

    }

    public static void writeLongArray(ByteBuf output, long[] longs) {
        writeLongArray(longs, output);
    }

    public static <T extends Enum<T>> T readEnum(T[] values, ByteBuf buf) {
        return (T)values[readVarInt(buf)];
    }

    public static void writeEnum(Enum<?> value, ByteBuf buf) {
        writeVarInt(value.ordinal(), buf);
    }

    public static void writeEnum(ByteBuf buf, Enum<?> value) {
        writeEnum(value, buf);
    }

    public static String readOptionalUtf8(ByteBuf input, int limit) {
        return input.readBoolean() ? readUtf8(input, limit) : null;
    }

    public static String readOptionalUtf8(ByteBuf input) {
        return readOptionalUtf8(input, Integer.MAX_VALUE);
    }

    public static String readOptionalAscii(ByteBuf input, int limit) {
        return input.readBoolean() ? readAscii(input, limit) : null;
    }

    public static String readOptionalAscii(ByteBuf input) {
        return readOptionalAscii(input, Integer.MAX_VALUE);
    }

    public static void writeOptionalUtf8(String s, ByteBuf output) {
        output.writeBoolean(s != null);
        if (s != null) {
            writeUtf8(s, output);
        }

    }

    public static void writeOptionalUtf8(ByteBuf output, String s) {
        writeOptionalUtf8(s, output);
    }

    public static void writeOptionalAscii(String s, ByteBuf output) {
        output.writeBoolean(s != null);
        if (s != null) {
            writeAscii(s, output);
        }

    }

    public static void writeOptionalAscii(ByteBuf output, String s) {
        writeOptionalAscii(s, output);
    }

    public static <T> T[] readArray(ByteBuf input, int limit, IntFunction<T[]> array, Function<ByteBuf, T> reader) {
        int j = readVarInt(input);
        if (j > limit) {
            throw new IllegalArgumentException("Received array that is greater than " + limit + " (got " + j + " elements)");
        } else {
            T[] $array = (T[])((Object[])array.apply(j));

            for(int i = 0; i < j; ++i) {
                $array[i] = reader.apply(input);
            }

            return $array;
        }
    }

    public static <T> T[] readArray(ByteBuf input, IntFunction<T[]> array, Function<ByteBuf, T> reader) {
        return (T[])readArray(input, Integer.MAX_VALUE, array, reader);
    }

    public static <T> void writeArray(T[] array, int off, int len, BiConsumer<ByteBuf, T> writer, ByteBuf output) {
        int count = len - off;
        writeVarInt(count, output);

        while(off < len) {
            writer.accept(output, array[off]);
            ++off;
        }

    }

    public static <T> void writeArray(T[] array, BiConsumer<ByteBuf, T> writer, ByteBuf output) {
        writeArray(array, 0, array.length, writer, output);
    }

    public static <T, C extends Collection<T>> C readCollection(ByteBuf input, int limit, IntFunction<C> collection, Function<ByteBuf, T> reader) {
        int j = readVarInt(input);
        if (j > limit) {
            throw new IllegalArgumentException("Received collection that is greater than " + limit + " (got " + j + " elements)");
        } else {
            C $collection = (C)(collection.apply(j));

            for(int i = 0; i < j; ++i) {
                $collection.add(reader.apply(input));
            }

            return $collection;
        }
    }

    public static <T, C extends Collection<T>> C readCollection(ByteBuf input, IntFunction<C> collection, Function<ByteBuf, T> reader) {
        return (C)readCollection(input, Integer.MAX_VALUE, collection, reader);
    }

    public static <T> void writeCollection(Collection<T> collection, BiConsumer<ByteBuf, T> writer, ByteBuf output) {
        int j = collection.size();
        writeVarInt(j, output);
        if (j != 0) {
            Iterator<T> iterator = collection.iterator();

            while(j-- > 0) {
                writer.accept(output, iterator.next());
            }
        }

    }

    public static <T> void writeList(List<T> list, int off, int length, BiConsumer<ByteBuf, T> writer, ByteBuf output) {
        int j = length - off;
        writeVarInt(j, output);
        if (j != 0) {
            List<T> sub = off == 0 && length == list.size() ? list : list.subList(off, length);
            if (sub instanceof RandomAccess) {
                for(int i = 0; i < j; ++i) {
                    writer.accept(output, sub.get(i));
                }
            } else {
                for(T e : sub) {
                    writer.accept(output, e);
                }
            }
        }

    }

    public static <T> void writeList(List<T> list, BiConsumer<ByteBuf, T> writer, ByteBuf output) {
        writeList(list, 0, list.size(), writer, output);
    }

    public static <K, V, M extends Map<K, V>> M readMap(ByteBuf input, int limit, IntFunction<M> map, Function<ByteBuf, K> keyReader, Function<ByteBuf, V> valueReader, TriConsumer<M, K, V> put) {
        int j = readVarInt(input);
        if (j > limit) {
            throw new IllegalArgumentException("Received map that is greater than " + limit + " (got " + j + " elements)");
        } else {
            M $map = (M)(map.apply(j));

            while(j-- > 0) {
                K key = (K)keyReader.apply(input);
                V value = (V)valueReader.apply(input);
                put.accept($map, key, value);
            }

            return $map;
        }
    }

    public static <K, V, M extends Map<K, V>> M readMap(ByteBuf input, IntFunction<M> map, Function<ByteBuf, K> keyReader, Function<ByteBuf, V> valueReader, TriConsumer<M, K, V> put) {
        return (M)readMap(input, Integer.MAX_VALUE, map, keyReader, valueReader, put);
    }

    public static <K, V, M extends Map<K, V>> M readMap(ByteBuf input, int limit, IntFunction<M> map, Function<ByteBuf, K> keyReader, Function<ByteBuf, V> valueReader) {
        return (M)readMap(input, limit, map, keyReader, valueReader, Map::put);
    }

    public static <K, V, M extends Map<K, V>> M readMap(ByteBuf input, IntFunction<M> map, Function<ByteBuf, K> keyReader, Function<ByteBuf, V> valueReader) {
        return (M)readMap(input, Integer.MAX_VALUE, map, keyReader, valueReader);
    }

    public static <K, V> void writeMap(Map<K, V> map, BiConsumer<ByteBuf, K> keyWriter, BiConsumer<ByteBuf, V> valueWriter, ByteBuf output) {
        int j = map.size();
        writeVarInt(j, output);
        if (j != 0) {
            Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();

            while(j-- > 0) {
                Map.Entry<K, V> entry = (Map.Entry)iterator.next();
                keyWriter.accept(output, entry.getKey());
                valueWriter.accept(output, entry.getValue());
            }
        }

    }

    public static <T> void writeStream(Stream<T> stream, BiConsumer<ByteBuf, T> writer, ByteBuf output) {
        output.ensureWritable(4);
        int index = output.writerIndex();
        output.writerIndex(index + 4);
        int[] box = new int[]{0};
        stream.forEach((element) -> {
            writer.accept(output, element);
            int var10002 = box[0]++;
        });
        int count = box[0];
        int newIndex = output.writerIndex();
        output.writerIndex(index);
        output.writeInt(count);
        output.writerIndex(newIndex);
    }

    public static void writeIntStream(IntStream stream, ObjIntConsumer<ByteBuf> writer, ByteBuf output) {
        output.ensureWritable(4);
        int index = output.writerIndex();
        output.writerIndex(index + 4);
        int[] box = new int[]{0};
        stream.forEach((element) -> {
            writer.accept(output, element);
            int var10002 = box[0]++;
        });
        int count = box[0];
        int newIndex = output.writerIndex();
        output.writerIndex(index);
        output.writeInt(count);
        output.writerIndex(newIndex);
    }

    public static void writeLongStream(LongStream stream, ObjLongConsumer<ByteBuf> writer, ByteBuf output) {
        output.ensureWritable(4);
        int index = output.writerIndex();
        output.writerIndex(index + 4);
        int[] box = new int[]{0};
        stream.forEach((element) -> {
            writer.accept(output, element);
            int var10002 = box[0]++;
        });
        int count = box[0];
        int newIndex = output.writerIndex();
        output.writerIndex(index);
        output.writeInt(count);
        output.writerIndex(newIndex);
    }

    public static void writeDoubleStream(DoubleStream stream, ObjDoubleConsumer<ByteBuf> writer, ByteBuf output) {
        output.ensureWritable(4);
        int index = output.writerIndex();
        output.writerIndex(index + 4);
        int[] box = new int[]{0};
        stream.forEach((element) -> {
            writer.accept(output, element);
            int var10002 = box[0]++;
        });
        int count = box[0];
        int newIndex = output.writerIndex();
        output.writerIndex(index);
        output.writeInt(count);
        output.writerIndex(newIndex);
    }

    public static <T> Stream<T> readStream(ByteBuf input, int limit, Function<ByteBuf, T> reader) {
        int j = input.readInt();
        if (j > limit) {
            throw new IllegalArgumentException("Received stream that is greater than " + limit + " (got " + j + " elements)");
        } else {
            Stream.Builder<T> builder = Stream.builder();

            while(j-- > 0) {
                builder.accept(reader.apply(input));
            }

            return builder.build();
        }
    }

    public static <T> Stream<T> readStream(ByteBuf input, Function<ByteBuf, T> reader) {
        return readStream(input, Integer.MAX_VALUE, reader);
    }

    public static IntStream readIntStream(ByteBuf input, int limit, ToIntFunction<ByteBuf> reader) {
        int j = input.readInt();
        if (j > limit) {
            throw new IllegalArgumentException("Received stream that is greater than " + limit + " (got " + j + " elements)");
        } else {
            IntStream.Builder builder = IntStream.builder();

            while(j-- > 0) {
                builder.accept(reader.applyAsInt(input));
            }

            return builder.build();
        }
    }

    public static IntStream readIntStream(ByteBuf input, ToIntFunction<ByteBuf> reader) {
        return readIntStream(input, Integer.MAX_VALUE, reader);
    }

    public static LongStream readLongStream(ByteBuf input, int limit, ToLongFunction<ByteBuf> reader) {
        int j = input.readInt();
        if (j > limit) {
            throw new IllegalArgumentException("Received stream that is greater than " + limit + " (got " + j + " elements)");
        } else {
            LongStream.Builder builder = LongStream.builder();

            while(j-- > 0) {
                builder.accept(reader.applyAsLong(input));
            }

            return builder.build();
        }
    }

    public static LongStream readLongStream(ByteBuf input, ToLongFunction<ByteBuf> reader) {
        return readLongStream(input, Integer.MAX_VALUE, reader);
    }

    public static DoubleStream readDoubleStream(ByteBuf input, int limit, ToDoubleFunction<ByteBuf> reader) {
        int j = input.readInt();
        if (j > limit) {
            throw new IllegalArgumentException("Received stream that is greater than " + limit + " (got " + j + " elements)");
        } else {
            DoubleStream.Builder builder = DoubleStream.builder();

            while(j-- > 0) {
                builder.accept(reader.applyAsDouble(input));
            }

            return builder.build();
        }
    }

    public static DoubleStream readDoubleStream(ByteBuf input, ToDoubleFunction<ByteBuf> reader) {
        return readDoubleStream(input, Integer.MAX_VALUE, reader);
    }

    public static int varIntSize(int varint) {
        return VARINT_EXACT_BYTE_LENGTHS[Integer.numberOfLeadingZeros(varint)];
    }

    public static String hexDump(ByteBuf buf, int maxLen) {
        return ByteBufUtil.hexDump(buf, 0, Math.min(buf.writerIndex(), maxLen));
    }

    public static void close(Channel ch) {
        if (ch.isActive()) {
            ch.close();
        }

    }

    public static <T extends Throwable> void close(Channel ch, Throwable t) throws T {
        if (ch.isActive()) {
            ch.close();
        }

    }

    public static void inEventLoop(EventLoop eventLoop, Runnable command) {
        if (eventLoop.inEventLoop()) {
            command.run();
        } else {
            eventLoop.execute(command);
        }

    }


    static void invokeWriteAndFlush0(Channel ch, ByteBuf buf, boolean invokeFlush) {
        if (!ch.isActive()) {
            buf.release();
        } else {
            Channel.Unsafe unsafe = ch.unsafe();
            unsafe.write(buf, unsafe.voidPromise());
            if (invokeFlush) {
                unsafe.flush();
            }

        }
    }

    private static String readString0(ByteBuf input, int limit, int bytes, Charset charset) {
        int len = readVarInt(input);
        if (len > limit * (limit == Integer.MAX_VALUE ? 1 : bytes)) {
            throw new IllegalArgumentException("Cannot receive encoded string longer than " + len * 4 + " (got " + len + " characters)");
        } else {
            int idx = input.readerIndex();
            String s = input.toString(idx, len, charset);
            input.readerIndex(idx + len);
            int actual = s.length();
            if (actual > limit) {
                throw new IllegalArgumentException("Cannot receive string longer than " + len + " (got " + actual + " characters)");
            } else {
                return s;
            }
        }
    }

    private static void writeVarIntFull(ByteBuf buf, int value) {
        if ((value & -128) == 0) {
            buf.writeByte(value);
        } else if ((value & -16384) == 0) {
            int w = (value & 127 | 128) << 8 | value >>> 7;
            buf.writeShort(w);
        } else if ((value & -2097152) == 0) {
            int w = (value & 127 | 128) << 16 | (value >>> 7 & 127 | 128) << 8 | value >>> 14;
            buf.writeMedium(w);
        } else if ((value & -268435456) == 0) {
            int w = (value & 127 | 128) << 24 | (value >>> 7 & 127 | 128) << 16 | (value >>> 14 & 127 | 128) << 8 | value >>> 21;
            buf.writeInt(w);
        } else {
            int w = (value & 127 | 128) << 24 | (value >>> 7 & 127 | 128) << 16 | (value >>> 14 & 127 | 128) << 8 | value >>> 21 & 127 | 128;
            buf.writeInt(w);
            buf.writeByte(value >>> 28);
        }

    }

    private NetUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        VARINT_EXACT_BYTE_LENGTHS = new int[33];

        for(int i = 0; i <= 32; ++i) {
            VARINT_EXACT_BYTE_LENGTHS[i] = (int)Math.ceil(((double)31.0F - (double)(i - 1)) / (double)7.0F);
        }

        VARINT_EXACT_BYTE_LENGTHS[32] = 1;
    }
}
