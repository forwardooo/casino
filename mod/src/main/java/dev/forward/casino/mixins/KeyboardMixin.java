package dev.forward.casino.mixins;

import dev.forward.casino.event.impl.input.KeyPress;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    private void hookKeyPress(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (MinecraftClient.getInstance().getWindow().getHandle() == window) {
            if (action == 1) {
                KeyPress keyPress = KeyPress.BUS.fire(KeyPress.set(key));
                if (keyPress.isCancelled()) {
                    ci.cancel();
                }
            }
        }
    }
}
