package dev.forward.casino.mixins.accessors;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MatrixStack.Entry.class)
public interface MatrixStackEntryAccessor {
    @Accessor("modelMatrix")
    void setEntry(Matrix4f matrix);
}
