package dev.forward.casino.command.impl;

import dev.forward.casino.command.Arguments;
import dev.forward.casino.command.CustomCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BalanceCommand extends CustomCommand {
    public BalanceCommand() {
        super("balance", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, CraftPlayer player, Command command, Arguments args) {
        if (args.isEmpty()) {
            double d = getData(player, "money");
            sendMessage(player, String.format("Ваш баланс: %s$", (int) d));
        } else {
            if (args.get(0).equals("set")) {
                Player pl = getPlayer(args.get(1));
                if (pl == null) {
                    sendMessage(player,"Вы ввели неправильный никнейм!");
                } else {
                    try {
                        double money = Double.parseDouble(args.get(2));
                        setData(pl, "money", money);
                    }catch (Throwable t) {
                        sendMessage(player,"Вы некорректно ввели сумму!");
                    }
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CraftPlayer player, Command command, Arguments args) {
        List<String> completions = new ArrayList<>();

        String[] arguments = args.getArgs();
        int argLength = arguments.length;

        if (argLength == 1) {
            completions.add("set");
        }

        if (argLength == 2) {
            String firstArg = args.get(0).toLowerCase();
            if (firstArg.equals("set")) {
                completions.addAll(Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .collect(Collectors.toList()));

            }
        }
        if (argLength == 3) {
            completions.addAll(Arrays.asList("1000", "0"));
        }
        String lastArg = argLength > 0 ? arguments[argLength - 1].toLowerCase() : "";
        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(lastArg))
                .collect(Collectors.toList());
    }
}
