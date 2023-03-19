package fr.altarik.toolbox.pagination.api;

import fr.altarik.toolbox.pagination.PaginatedContent;
import fr.altarik.toolbox.pagination.precondition.ContentCondition;
import fr.altarik.toolbox.pagination.precondition.HeaderCondition;
import fr.altarik.toolbox.pagination.precondition.NullPlayerCondition;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class PaginationApiImpl implements PaginationApi {
    /**
     * Integer represent relative tts of the paginated content, decreased by 1 every seconds
     */
    private final Map<ServerPlayerEntity, Pair<Integer, PaginatedContent>> paginatedContent = new HashMap<>();
    private final Predicate<ServerPlayerEntity> playerCondition = new NullPlayerCondition().negate();
    private final Predicate<String> headerCondition = new HeaderCondition().negate();
    private final Predicate<String> contentCondition = new ContentCondition().negate();
    @Override
    public void createTable(ServerPlayerEntity playerEntity, String content, String header) {
        if(playerCondition.test(playerEntity) || headerCondition.test(header) || contentCondition.test(content)) {
            throw new IllegalArgumentException("Preconditions aren't satisfied");
        }
        paginatedContent.put(playerEntity, new Pair<>(900, new PaginatedContent(header, content)));
    }
}
