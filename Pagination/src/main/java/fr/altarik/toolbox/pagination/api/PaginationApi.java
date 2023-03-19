package fr.altarik.toolbox.pagination.api;

import net.minecraft.server.network.ServerPlayerEntity;

public interface PaginationApi {

    /**
     * <p>Create a pagination table for player, content is separated into multiple pages.<br>
     * You can separate yourself content by adding *\n\n* between two pages.</p>
     * <p>Content have a time-to-live of 15 minutes (18,000 ticks)</p>
     * @param playerEntity The player who will be able to interact and see the paginated message
     * @param content Content you want to paginate
     * @param header Header/title you want to add to every page, empty space is filled with "=".
     *               <p>Special values are:
     *               <ul><li><b>null</b> if you doesn't want to add a header</li>
     *               <li><b>empty String</b> if you want just the header to be filled only with "="</li></ul>
     * @throws IllegalArgumentException if one of its conditions is met: <ol>
     *     <li><b>header</b> length is more than 50 characters</li>
     *     <li><b>content</b> is empty/blank</li>
     *     <li><b>playerEntity</b> or <b>content</b> are null</li>
     * </ol>
     */
    void createTable(ServerPlayerEntity playerEntity, String content, String header);

}
