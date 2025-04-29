package dev.forward.casino.mixins;

import dev.forward.casino.event.impl.input.TextPaste;
import dev.forward.casino.event.impl.render.ItemTooltipRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void hookKeyPress(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if ((Screen.hasControlDown() || Screen.hasShiftDown()) && keyCode == GLFW.GLFW_KEY_V) {
            String clipboard = MinecraftClient.getInstance().keyboard.getClipboard();
            TextPaste.BUS.fire(TextPaste.of(clipboard));
        }
    }
    @Inject(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V", at = @At("HEAD"), cancellable = true)
    private void hookTooltip(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo ci) {
        ItemTooltipRender tooltipRender = ItemTooltipRender.BUS.fire(ItemTooltipRender.of(matrices, stack, getTooltipFromItem(stack), x,y));
        if (tooltipRender.isCancelled()) {
            ci.cancel();
        } else {
            renderTooltip(matrices, tooltipRender.getContent(), tooltipRender.getX(),tooltipRender.getY());
        }
    }
    @Shadow
    public void renderTooltip(MatrixStack matrices, List<Text> lines, int x, int y) {
    }
    @Shadow
    public List<Text> getTooltipFromItem(ItemStack stack) {
        return null;
    }
}
