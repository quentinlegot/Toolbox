package fr.altarik.toolbox.pagination.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.altarik.toolbox.pagination.Pagination;
import fr.altarik.toolbox.pagination.api.PaginationApi;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandsRegister {

    private final PaginationApi api;

    public CommandsRegister(Pagination instance) {
        this.api = instance.getApi();
    }

    public void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(literal("table")
                .then(literal("page")
                        .then(argument("page", IntegerArgumentType.integer())
                                .executes(this::selectPageCommand)
                        )
                )
        );
    }

    private int selectPageCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            int page = IntegerArgumentType.getInteger(context, "page");
            api.display(context.getSource().getPlayerOrThrow(), page);
        } catch(NullPointerException | IllegalArgumentException e) {
            context.getSource().sendFeedback(Text.literal("Error: " + e.getMessage()), false);
        }
        return 0;
    }

}
