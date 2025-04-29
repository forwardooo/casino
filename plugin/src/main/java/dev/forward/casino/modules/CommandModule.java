package dev.forward.casino.modules;

import com.google.common.collect.Lists;
import dev.forward.casino.Casino;
import dev.forward.casino.command.CustomCommand;
import dev.forward.casino.command.impl.BalanceCommand;
import dev.forward.casino.command.impl.OpenCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandModule extends Module{
    private final List<CustomCommand> commands = new ArrayList<>();

    @Override
    public void load() {
        commands.addAll(Arrays.asList(
                new OpenCommand(),
                new BalanceCommand()
        ));
        commands.forEach((cmd) -> cmd.register(Casino.getInstance()));
    }

    @Override
    public void unload() {

    }
}
