package fr.altarik.toolbox.pagination.api;

import fr.altarik.toolbox.pagination.PaginatedContent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class PaginationApiImpl implements PaginationApi {

    Map<ServerPlayerEntity, Pair<Integer, PaginatedContent>> paginatedContent = new HashMap<>();

    @Override
    public void createTable(ServerPlayerEntity playerEntity, String content, String header) {
        // TODO: 01/03/2023
    }
}
