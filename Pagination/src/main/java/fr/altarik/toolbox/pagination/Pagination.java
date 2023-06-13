package fr.altarik.toolbox.pagination;

import fr.altarik.toolbox.pagination.api.PaginationApi;
import fr.altarik.toolbox.pagination.api.PaginationApiImpl;
import fr.altarik.toolbox.pagination.command.CommandsRegister;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.jetbrains.annotations.NotNull;

public class Pagination implements ModInitializer {

    private static Pagination instance;
    private final PaginationApi api;

    public Pagination() {
        instance = this;
        this.api = new PaginationApiImpl();
    }

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> new CommandsRegister(this).register(dispatcher));
    }

    public @NotNull PaginationApi getApi() {
        return api;
    }

    @SuppressWarnings("unused")
    public static @NotNull Pagination getInstance() {
        return instance;
    }
}
