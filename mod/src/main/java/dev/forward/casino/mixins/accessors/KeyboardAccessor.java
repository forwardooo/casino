package dev.forward.casino.mixins.accessors;

import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Keyboard.class)
public interface KeyboardAccessor {
    @Accessor("repeatEvents")
    boolean isRepeatEvents();
}
