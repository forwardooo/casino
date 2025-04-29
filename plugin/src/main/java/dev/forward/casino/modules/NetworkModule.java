package dev.forward.casino.modules;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import dev.forward.casino.Casino;
import dev.forward.casino.event.EventBus;
import dev.forward.casino.event.impl.network.PluginMessage;
import net.minecraft.server.v1_16_R3.PacketPlayInCustomPayload;

public class NetworkModule extends Module {

    @Override
    public void load() {
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(Casino.getInstance(), PacketType.Play.Client.CUSTOM_PAYLOAD) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        if (event.getPacket().getHandle() instanceof PacketPlayInCustomPayload ) {
                            PacketPlayInCustomPayload packet = (PacketPlayInCustomPayload) event.getPacket().getHandle();
                            String channel = packet.tag.toString();
                            EventBus.of(PluginMessage.class).fire(
                                    PluginMessage.of(channel, packet.data.slice(), event.getPlayer())
                            );
                        }
                    }
                });
    }

    @Override
    public void unload() {
    }
}
