package dev.forward.casino.mixins;

import dev.forward.casino.event.impl.loop.GameTickPost;
import dev.forward.casino.event.impl.loop.GameTickPre;
import dev.forward.casino.event.impl.render.ScreenDisplay;
import dev.forward.casino.event.impl.render.RenderTickPost;
import dev.forward.casino.event.impl.render.RenderTickPre;
import dev.forward.casino.event.impl.render.WindowResize;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    private static MinecraftClient instance;
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;render(FJZ)V"))
    private void hookRenderPre(boolean tick, CallbackInfo ci) {
        RenderTickPre.BUS.fire(RenderTickPre.INSTANCE);
    }
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;render(FJZ)V", shift = At.Shift.AFTER))
    private void hookRenderPost(boolean tick, CallbackInfo ci) {RenderTickPost.BUS.fire(RenderTickPost.INSTANCE);
    }
    @Inject(method = "openScreen", at = @At("HEAD"), cancellable = true)
    private void hookOpenScreen(Screen screen, CallbackInfo ci) {
        ScreenDisplay event = ScreenDisplay.BUS.fire(ScreenDisplay.set(screen));
        if (event.isCancelled()) ci.cancel();
    }
    @Inject(method = "tick", at = @At("HEAD"))
    private void hookTickPre(CallbackInfo ci) {
        if (instance.world != null) {
            GameTickPre.BUS.fire(GameTickPre.INSTANCE);
        }
    }
    @Inject(method = "tick", at = @At("TAIL"))
    private void hookTickPost(CallbackInfo ci) {
        if (instance.world != null) {
            GameTickPost.BUS.fire(GameTickPost.INSTANCE);
        }
    }
    @Inject(method = "onResolutionChanged", at = @At("TAIL"))
    private void hookRescale(CallbackInfo ci) {
        WindowResize.BUS.fire(WindowResize.INSTANCE);
    }
    // disable
    @Inject(method = "isMultiplayerEnabled", at = @At("HEAD"), cancellable = true)
    private void hookMultiplayer(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
    //disable
    @Inject(method = "openChatScreen", at = @At("HEAD"), cancellable = true)
    private void hookChatScreen(String text, CallbackInfo ci) {
        openScreen(new ChatScreen(text));
        ci.cancel();
    }
    @Shadow
    public void openScreen(@Nullable Screen screen) {
    }
}
