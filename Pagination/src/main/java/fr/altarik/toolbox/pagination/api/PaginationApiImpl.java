package fr.altarik.toolbox.pagination.api;

import fr.altarik.toolbox.pagination.PaginatedContent;
import fr.altarik.toolbox.pagination.precondition.ContentCondition;
import fr.altarik.toolbox.pagination.precondition.HeaderCondition;
import fr.altarik.toolbox.pagination.precondition.NullPlayerCondition;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class PaginationApiImpl implements PaginationApi {
    /**
     * Integer represent relative tts of the paginated content, decreased by 1 every seconds
     */
    public final Map<ServerPlayerEntity, Pair<Integer, PaginatedContent>> paginatedContent = new HashMap<>();
    private final Predicate<ServerPlayerEntity> playerCondition = new NullPlayerCondition().negate();
    private final Predicate<String> headerCondition = new HeaderCondition().negate();
    private final Predicate<String> contentCondition = new ContentCondition().negate();

    public PaginationApiImpl() {
        ServerTickEvents.START_SERVER_TICK.register(this::serverTick);
    }

    @Override
    public void createTable(ServerPlayerEntity playerEntity, String content, String header, boolean display) {
        if(playerCondition.test(playerEntity) || headerCondition.test(header) || contentCondition.test(content)) {
            throw new IllegalArgumentException("Preconditions aren't satisfied");
        }
        PaginatedContent paginatedContent1 = new PaginatedContent(header, content);
        paginatedContent.put(playerEntity, new Pair<>(18000, paginatedContent1));
        if(display) {
            try {
                paginatedContent1.display(playerEntity, 0);
            } catch (PageIndexOutOfBoundException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    @Override
    public void createTable(ServerPlayerEntity playerEntity, List<Text> content, Text header, boolean display) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void display(ServerPlayerEntity player, int page) throws PageIndexOutOfBoundException {
        if(player == null)
            throw new NullPointerException("Player is null");
        Pair<Integer, PaginatedContent> pair = paginatedContent.get(player);
        if(pair == null)
            throw new NullPointerException("No paginated page for player " + player.getCustomName());
        pair.getRight().display(player, page);
    }

    private void serverTick(MinecraftServer server) {
        List<ServerPlayerEntity> toRemove = new ArrayList<>();
        for(Map.Entry<ServerPlayerEntity, Pair<Integer, PaginatedContent>> content : paginatedContent.entrySet()) {
            if(content.getValue().getLeft() == 0) {
                toRemove.add(content.getKey());
            } else {
                content.getValue().setLeft(content.getValue().getLeft() - 1);
            }
        }
        for(ServerPlayerEntity player : toRemove) {
            paginatedContent.remove(player);
        }
    }
}
