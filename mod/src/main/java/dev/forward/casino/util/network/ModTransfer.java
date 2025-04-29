package dev.forward.casino.util.network;

import dev.forward.casino.event.impl.network.PluginMessage;
import dev.forward.casino.mixins.accessors.CustomPayloadC2SPacketAccessor;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.slots.SlotEnum;
import dev.forward.casino.util.FastAccess;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.EncoderException;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtTagSizeTracker;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Consumer;

public class ModTransfer implements FastAccess {
    private final ByteBuf buffer;

    public ModTransfer() {
        this(Unpooled.buffer());
    }

    public ModTransfer(ByteBuf buffer) {
        this.buffer = buffer;
    }

    public static void registerChannel(String channel, Consumer<ModTransfer> consumer) {
        registerChannel(channel, 1, consumer);
    }

    public static void registerChannel(String channel, int priority, Consumer<ModTransfer> consumer) {
        PluginMessage.BUS.register(priority,event -> {
            if (!event.getChannel().equals(channel)) {
                return;
            }
            consumer.accept(new ModTransfer(event.getData().retainedSlice()));
        });
    }

    public ModTransfer copy() {
        return new ModTransfer(this.buffer.copy());
    }

    public ModTransfer send(String channel) {
        mc.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(new Identifier(channel), new PacketByteBuf(this.buffer)));
        return this;
    }
    public ItemStack readItem() {
        if (!this.readBoolean()) {
            return ItemStack.EMPTY;
        } else {
            int i = NetUtil.readVarInt(buffer);
            int j = buffer.readByte();
            ItemStack itemStack = new ItemStack(Item.byRawId(i), j);
            itemStack.setTag(this.readNbt());
            return itemStack;
        }
    }
    public NbtCompound readNbt() {
        return this.readNbt(new NbtTagSizeTracker(2097152L));
    }

    @Nullable
    public NbtCompound readNbt(NbtTagSizeTracker sizeTracker) {
        int i = buffer.readerIndex();
        byte b = buffer.readByte();
        if (b == 0) {
            return null;
        } else {
            buffer.readerIndex(i);

            try {
                return NbtIo.read(new ByteBufInputStream(buffer), sizeTracker);
            } catch (IOException iOException) {
                throw new EncoderException(iOException);
            }
        }
    }

    public ModTransfer writeInt(int i) {
        this.buffer.writeInt(i);
        return this;
    }

    public int readInt() {
        return this.buffer.readInt();
    }

    public long readLong() {
        return this.buffer.readLong();
    }

    public ModTransfer writeFloat(float f) {
        this.buffer.writeFloat(f);
        return this;
    }

    public float readFloat() {
        return this.buffer.readFloat();
    }

    public double readDouble() {
        return this.buffer.readDouble();
    }

    public ModTransfer writeBoolean(boolean b) {
        this.buffer.writeBoolean(b);
        return this;
    }

    public boolean readBoolean() {
        return this.buffer.readBoolean();
    }

    public ModTransfer writeString(String s) {
        NetUtil.writeUtf8(s, this.buffer);
        return this;
    }

    public String readString() {
        return NetUtil.readUtf8(this.buffer);
    }

    public String readJson() {
        byte[] buff = new byte[this.buffer.readableBytes()];
        this.buffer.readBytes(buff);
        return new String(buff, StandardCharsets.UTF_8);
    }

    public ModTransfer writeUUID(UUID uuid) {
        this.buffer.writeLong(uuid.getMostSignificantBits());
        this.buffer.writeLong(uuid.getLeastSignificantBits());
        return this;
    }

    public UUID readUUID() {
        return new UUID(this.buffer.readLong(), this.buffer.readLong());
    }

    public boolean isReadable() {
        return this.buffer.isReadable();
    }
}