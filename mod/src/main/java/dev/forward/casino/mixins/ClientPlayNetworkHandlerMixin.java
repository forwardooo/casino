package dev.forward.casino.mixins;

import dev.forward.casino.event.impl.network.PluginMessage;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    private void hookCustomPayload(CustomPayloadS2CPacket packet, CallbackInfo ci) {
        if (packet.getChannel().toString().startsWith("house")) {
            PluginMessage.BUS.fire(PluginMessage.of(packet.getChannel().toString(), packet.getData().slice()));
            ci.cancel();
        }
    }
}
