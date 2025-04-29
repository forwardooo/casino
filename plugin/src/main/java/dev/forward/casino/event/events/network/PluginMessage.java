package dev.forward.casino.event.events.network;

import dev.forward.casino.util.network.ModTransfer;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class PluginMessage {
    private static final PluginMessage INSTANCE = new PluginMessage();
    private String channel;
    private ModTransfer data;
    private Player player;
    public static PluginMessage set(String channel, ByteBuf data, Player player) {
        INSTANCE.channel = channel;
        INSTANCE.data = new ModTransfer(data);
        INSTANCE.player = player;
        return INSTANCE;
    }
}
