package dev.forward.casino.event.impl.render;

import dev.forward.casino.event.Event;
import dev.forward.casino.event.EventBus;
import lombok.Getter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public class ItemTooltipRender extends TooltipRender implements Event {
    public static EventBus<ItemTooltipRender> BUS = new EventBus<>();
    private static final ItemTooltipRender INSTANCE = new ItemTooltipRender();
    @Getter
    private ItemStack stack;
    public static ItemTooltipRender set(MatrixStack stack, ItemStack itemStack, List<Text> list, int n, int n2) {
        INSTANCE.setMatrices(stack);
        INSTANCE.stack = itemStack;
        INSTANCE.content = list;
        INSTANCE.x = n;
        INSTANCE.y = n2;
        INSTANCE.setCancelled(false);
        return INSTANCE;
    }
}
