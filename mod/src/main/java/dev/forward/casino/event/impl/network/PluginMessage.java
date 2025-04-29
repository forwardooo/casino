package dev.forward.casino.event.impl.network;

import dev.forward.casino.event.Event;
import dev.forward.casino.event.EventBus;
import io.netty.buffer.ByteBuf;
import lombok.Getter;

@Getter
public class PluginMessage implements Event {
    public static EventBus<PluginMessage> BUS = new EventBus<PluginMessage>();
    private static final PluginMessage INSTANCE = new PluginMessage();
    private ByteBuf data;
    private String channel;
    public static PluginMessage set(String channel, ByteBuf data) {
        INSTANCE.channel = channel;
        INSTANCE.data = data;
        return INSTANCE;
    }

}
