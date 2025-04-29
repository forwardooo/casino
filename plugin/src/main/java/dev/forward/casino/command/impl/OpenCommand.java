package dev.forward.casino.command.impl;

import dev.forward.casino.Casino;
import dev.forward.casino.command.Arguments;
import dev.forward.casino.command.CustomCommand;
import dev.forward.casino.slots.SlotEnum;
import dev.forward.casino.util.network.ModTransfer;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;

import java.util.Collections;
import java.util.List;

public class OpenCommand extends CustomCommand {
    public OpenCommand() {
        super("open", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, CraftPlayer player, Command command, Arguments args) {
        ModTransfer buffer = new ModTransfer(Unpooled.buffer());
        double d = getData(player, "money");
        buffer.writeInt((int)d);
        buffer.writeInt(30);
        for (int i = 0; i < 3; i++) {
            for (SlotEnum val : SlotEnum.values()) {
                buffer.writeString(val.name());
            }
        }
        buffer.sendPayload(player.getPlayer(), "house:open");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CraftPlayer player, Command command, Arguments args) {
        return Collections.emptyList();
    }
}
