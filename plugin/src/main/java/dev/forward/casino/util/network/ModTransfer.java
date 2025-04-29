package dev.forward.casino.util.network;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import dev.forward.casino.event.EventBus;
import dev.forward.casino.event.impl.network.PluginMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutCustomPayload;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
public class ModTransfer {
    private final ByteBuf buffer;

    public ModTransfer() {
        this(Unpooled.buffer());
    }

    public ModTransfer(ByteBuf buffer) {
        this.buffer = buffer;
    }

    public ModTransfer writeInt(int i) {
        this.buffer.writeInt(i);
        return this;
    }
    public static void setupChannel(int priority, String channel, Consumer<PluginMessage> consumer) {
        EventBus.of(PluginMessage.class).register(priority, plm -> {
            if (plm.getChannel().equals(channel)) {
                consumer.accept(plm);
            }
        });
    }
    public static void setupChannel(String channel, Consumer<PluginMessage> consumer) {
        setupChannel(1, channel, consumer);
    }

    public void sendPayload(Player player, String channel) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        MinecraftKey key = new MinecraftKey(channel);
        PacketDataSerializer serializer = new PacketDataSerializer(Unpooled.buffer());
        serializer.writeBytes(buffer);
        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload(key, serializer);
        craftPlayer.getHandle().playerConnection.sendPacket(packet);
    }

    public void sendPayload(String channel) {
        MinecraftKey key = new MinecraftKey(channel);
        PacketDataSerializer serializer = new PacketDataSerializer(Unpooled.buffer());
        serializer.writeBytes(buffer);
        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload(key, serializer);
        for (Player player : Bukkit.getOnlinePlayers()) {
            CraftPlayer craftPlayer = (CraftPlayer) player;
            craftPlayer.getHandle().playerConnection.sendPacket(packet);
        }
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

    public ModTransfer writeDouble(double d) {
        this.buffer.writeDouble(d);
        return this;
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

    public ModTransfer writeByte(int i) {
        this.buffer.writeByte(i);
        return this;
    }

    public byte readByte() {
        return this.buffer.readByte();
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

    public GameProfile readGameProfile() {
        UUID uuid = this.readUUID();
        String name = this.readString();
        GameProfile gameProfile = new GameProfile(uuid, name);
        PropertyMap propertyMap = gameProfile.getProperties();
        int size = this.buffer.readInt();

        for(int i = 0; i < size; ++i) {
            String propertyName = this.readString();
            String value = this.readString();
            Property property;
            if (this.buffer.readBoolean()) {
                String signature = this.readString();
                property = new Property(propertyName, value, signature);
            } else {
                property = new Property(propertyName, value);
            }

            propertyMap.put(property.getName(), property);
        }

        return gameProfile;
    }

    public boolean isReadable() {
        return this.buffer.isReadable();
    }

}
