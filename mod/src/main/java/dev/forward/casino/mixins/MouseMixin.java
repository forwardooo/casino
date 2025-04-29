package dev.forward.casino.mixins;

import dev.forward.casino.event.impl.input.MousePress;
import dev.forward.casino.event.impl.input.MouseWheel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    private void hookMousePress(long window, int button, int action, int mods, CallbackInfo ci) {
        if (MinecraftClient.getInstance().getWindow().getHandle() == window) {
            MousePress press = MousePress.BUS.fire(MousePress.of(button, action == 1));
            if (press.isCancelled()) {
                ci.cancel();
            }
        }
    }
    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void hookScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (MinecraftClient.getInstance().getWindow().getHandle() == window) {
            MouseWheel.BUS.fire(MouseWheel.of((int) vertical));
        }
    }
}
