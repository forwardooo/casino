package dev.forward.casino;

import dev.forward.casino.engine.Engine;
import dev.forward.casino.slots.HouseScreen;
import dev.forward.casino.slots.SlotEnum;
import dev.forward.casino.util.FastAccess;
import dev.forward.casino.util.network.ModTransfer;
import dev.forward.casino.util.network.NetUtil;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CasinoSlot implements ModInitializer, FastAccess {

    @Override
    public void onInitialize() {
        mc.execute(Engine::internalInitialize);
        ModTransfer.registerChannel("house:open", modTransfer -> {
            mc.execute(() -> {
                int bal = modTransfer.readInt();
                int itemsCanDrop = modTransfer.readInt();
                List<SlotEnum> stacks = new ArrayList<>();
                for (int i = 0; i < itemsCanDrop; i++) {
                    stacks.add(SlotEnum.valueOf(modTransfer.readString()));
                }

                new HouseScreen(stacks,bal).open();
            });
        });
        ModTransfer.registerChannel("house:scroll", modTransfer -> {
            HouseScreen screen = Engine.getCurrentScreen();
            if (screen != null) {
                int winningItemsCount = 15;
                List<SlotEnum> stacks = new ArrayList<>();
                for (int i = 0; i < winningItemsCount; i++) {
                    stacks.add(SlotEnum.valueOf(modTransfer.readString()));
                }
                screen.updateBalance(modTransfer.readInt(), 0, false);
                int afBalance = modTransfer.readInt();

                int size = modTransfer.readInt();
                List<Integer> integers = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    integers.add(modTransfer.readInt());
                }
                screen.scroll(stacks, afBalance, integers);
            }
        });
    }
}
