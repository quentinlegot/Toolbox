package fr.altarik.toolbox.core.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

public interface Command {

    int run() throws CommandSyntaxException;

}
