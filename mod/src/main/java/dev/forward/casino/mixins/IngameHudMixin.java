package dev.forward.casino.mixins;

import dev.forward.casino.event.impl.render.GuiOverlayRender;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class IngameHudMixin {
    @Inject(method = "render", at = @At("RETURN"))
    private void hookGuiOverlayRender(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        GuiOverlayRender.BUS.fire(GuiOverlayRender.of(matrices, tickDelta));
    }
}
