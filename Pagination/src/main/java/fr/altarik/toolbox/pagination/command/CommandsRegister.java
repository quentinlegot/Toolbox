package fr.altarik.toolbox.pagination.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import fr.altarik.toolbox.pagination.Pagination;
import fr.altarik.toolbox.pagination.api.PageIndexOutOfBoundException;
import fr.altarik.toolbox.pagination.api.PaginationApi;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandsRegister {

    private final PaginationApi api;

    public CommandsRegister(Pagination instance) {
        this.api = instance.getApi();
    }

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("table")
                .then(literal("page")
                        .then(argument("page", IntegerArgumentType.integer())
                                .executes(this::selectPageCommand)
                        )
                ).then(literal("test")
                        .requires(source -> source.isExecutedByPlayer() && source.hasPermissionLevel(3))
                        .executes(this::testPageCommand)
                ).then(literal("testText")
                        .requires(source -> source.isExecutedByPlayer() && source.hasPermissionLevel(3))
                        .executes(this::testPageTextCommand)
                )
        );
    }

    /**
     * Simply a debug command
     */
    private int testPageCommand(CommandContext<ServerCommandSource> context) {
        api.createTable(context.getSource().getPlayer(), """
                first line, string version
                Second line
                
                second page
                dqdq
                dqdqd
                qdqdq
                dqdq
                dqdq
                dqdq
                dqdqd
                third page
                dqdqd
                dqdqd
                d""", "My header", true);
        return 0;
    }

    private int testPageTextCommand(CommandContext<ServerCommandSource> context) {
        List<Text> content = new ArrayList<>();
        content.add(Text.literal("first line, text version"));
        content.add(Text.literal("Second line"));
        content.add(null);
        content.add(Text.literal("second page"));
        content.add(Text.literal("dqdq"));
        content.add(Text.literal("dqdqd"));
        content.add(Text.literal("dqdqd"));
        content.add(Text.literal("dqdq"));
        content.add(Text.literal("dqdq"));
        content.add(Text.literal("dqdq"));
        content.add(Text.literal("dqdqd"));
        content.add(Text.literal("third page"));
        content.add(Text.literal("dqdqd"));
        content.add(Text.literal("dqdqd"));
        api.createTable(context.getSource().getPlayer(), content, Text.literal("My Text Header"), true);
        return 0;
    }

    private int selectPageCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            int page = IntegerArgumentType.getInteger(context, "page");
            api.display(context.getSource().getPlayerOrThrow(), page);
        } catch(PageIndexOutOfBoundException e) {
            throw new CommandSyntaxException(new SimpleCommandExceptionType(e.getText()), e.getText());
        }
        return 0;
    }

    private enum TestType {
        String,
        Text;
    }

}
