package fr.altarik.toolbox.pagination.precondition;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.Predicate;

/**
 * This predicate returns true if the player isn't null, false otherwise
 */
public class NullPlayerCondition implements Predicate<ServerPlayerEntity> {
    @Override
    public boolean test(ServerPlayerEntity player) {
        return player != null;
    }
}
