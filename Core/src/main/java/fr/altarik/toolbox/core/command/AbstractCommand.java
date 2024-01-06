package fr.altarik.toolbox.core.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;

public abstract class AbstractCommand implements Command {

    protected final ServerCommandSource source;
    protected final CommandContext<ServerCommandSource> context;

    protected AbstractCommand(CommandContext<ServerCommandSource> c) {
        this.context = c;
        this.source = c.getSource();
    }
}
