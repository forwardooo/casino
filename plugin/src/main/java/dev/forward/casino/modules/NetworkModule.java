package dev.forward.casino.modules;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import dev.forward.casino.Casino;
import dev.forward.casino.event.EventBus;
import dev.forward.casino.event.events.network.PluginMessage;
import dev.forward.casino.slots.SlotEnum;
import dev.forward.casino.util.network.ModTransfer;
import net.minecraft.server.v1_16_R3.PacketPlayInCustomPayload;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.IntConsumer;

public class NetworkModule extends Module {

    @Override
    public void load() {
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(Casino.getInstance(), PacketType.Play.Client.CUSTOM_PAYLOAD) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        if (event.getPacket().getHandle() instanceof PacketPlayInCustomPayload ) {
                            PacketPlayInCustomPayload packet = (PacketPlayInCustomPayload) event.getPacket().getHandle();
                            String channel = packet.tag.toString();
                            EventBus.of(PluginMessage.class).fire(
                                    PluginMessage.set(channel, packet.data.slice(), event.getPlayer())
                            );
                        }
                    }
                });

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


    @Override
    public void unload() {
    }
}
