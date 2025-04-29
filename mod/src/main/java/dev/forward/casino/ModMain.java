package dev.forward.casino;

import dev.forward.casino.engine.Engine;
import dev.forward.casino.element.MainScreen;
import dev.forward.casino.element.SlotEnum;
import dev.forward.casino.util.FastAccess;
import dev.forward.casino.util.network.ModTransfer;
import net.fabricmc.api.ModInitializer;

import java.util.ArrayList;
import java.util.List;

public class ModMain implements ModInitializer, FastAccess {

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

                new MainScreen(stacks,bal).open();
            });
        });
        ModTransfer.registerChannel("house:scroll", modTransfer -> {
            MainScreen screen = Engine.getCurrentScreen();
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
