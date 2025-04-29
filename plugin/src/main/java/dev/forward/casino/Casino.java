package dev.forward.casino;

import dev.forward.casino.event.EventCaller;
import dev.forward.casino.modules.CommandModule;
import dev.forward.casino.modules.Module;
import dev.forward.casino.modules.NetworkModule;
import dev.forward.casino.modules.PlayerDataModule;
import dev.forward.casino.slots.SlotEnum;
import dev.forward.casino.util.FastAccess;
import dev.forward.casino.util.network.ModTransfer;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.IntConsumer;

public final class Casino extends JavaPlugin implements FastAccess {
    private final Map<Class<? extends Module>, Module> modules = new HashMap<>();

    @Getter
    private static Casino instance;

    @Override
    public void onEnable() {
        instance = this;
        register(new NetworkModule());
        register(new CommandModule());
        register(new PlayerDataModule());
        this.getServer().getPluginManager().registerEvents(new EventCaller(), this);
        ModTransfer.setupChannel("house:scroll", message -> {
            Player player = message.getPlayer();
            int bet = message.getData().readInt();
            ModTransfer buffer = new ModTransfer();

            List<List<SlotEnum>> panels = generatePanels(buffer);
            List<List<SlotEnum>> horizontal = transposePanels(panels);

            double winnings = 0;
            Set<Integer> winningIndexes = new HashSet<>();

            for (int i = 0; i < horizontal.size(); i++) {
                final int row = i;
                List<SlotEnum> line = horizontal.get(i);
                Map<SlotEnum, Integer> duplicates = getConsecutiveDuplicatesWithIndexes(line, index -> {
                    int absoluteIndex = index * 3 + row;
                    winningIndexes.add(absoluteIndex);
                } );
                for (Map.Entry<SlotEnum, Integer> entry : duplicates.entrySet()) {
                    winnings += entry.getKey().getSum(entry.getValue()) * bet;
                }
            }

            double currentMoney = (double) getData(player, "money") - bet;
            double finalMoney = currentMoney + winnings;

            setData(player, "money", finalMoney);

            buffer.writeInt((int) currentMoney);
            buffer.writeInt((int) finalMoney);

            buffer.writeInt(winningIndexes.size());
            for (int index : winningIndexes) {
                buffer.writeInt(index);
            }

            buffer.sendPayload(player, "house:scroll");
        });
    }

    private List<List<SlotEnum>> generatePanels(ModTransfer buffer) {
        List<List<SlotEnum>> panels = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<SlotEnum> column = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                SlotEnum slot = SlotEnum.getRandom().get();
                column.add(slot);
                buffer.writeString(slot.name());
            }
            panels.add(column);
        }
        return panels;
    }

    private List<List<SlotEnum>> transposePanels(List<List<SlotEnum>> panels) {
        List<List<SlotEnum>> horizontal = Arrays.asList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        for (List<SlotEnum> column : panels) {
            for (int i = 0; i < column.size(); i++) {
                horizontal.get(i).add(column.get(i));
            }
        }
        return horizontal;
    }

    public static Map<SlotEnum, Integer> getConsecutiveDuplicatesWithIndexes(List<SlotEnum> list, IntConsumer indexConsumer) {
        Map<SlotEnum, Integer> result = new HashMap<>();
        if (list.isEmpty()) return result;

        SlotEnum prev = list.get(0);
        int count = 1;
        int startIndex = 0;

        for (int i = 1; i < list.size(); i++) {
            SlotEnum current = list.get(i);
            if (current == prev) {
                count++;
            } else {
                if (count > 1) {
                    result.put(prev, count);
                    if (indexConsumer != null) {
                        for (int j = 0; j < count; j++) {
                            indexConsumer.accept(startIndex + j);
                        }
                    }
                }
                prev = current;
                count = 1;
                startIndex = i;
            }
        }

        if (count > 1) {
            result.put(prev, count);
            if (indexConsumer != null) {
                for (int j = 0; j < count; j++) {
                    indexConsumer.accept(startIndex + j);
                }
            }
        }

        return result;
    }

    private void register(Module module) {
        module.load();
        modules.put(module.getClass(), module);
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> moduleClass) {
        return (T) modules.get(moduleClass);
    }

    @Override
    public void onDisable() {
        modules.values().forEach(Module::unload);
    }

}
