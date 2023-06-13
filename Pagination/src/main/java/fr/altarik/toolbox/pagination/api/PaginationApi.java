package fr.altarik.toolbox.pagination.api;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PaginationApi {

    /**
     * <p>Create a pagination table for player, content is separated into multiple pages.<br>
     * You can separate yourself content between two pages by adding *\n\n*.</p>
     * <p>Content have a time-to-live of 15 minutes (18,000 ticks)</p>
     * @param playerEntity The player who will be able to interact and see the paginated message
     * @param content Content you want to paginate
     * @param header Header/title you want to add to every page, empty space is filled with "=".
     *               <p>Special values are:
     *               <ul><li><b>null</b> if you doesn't want to add a header</li>
     *               <li><b>empty String</b> if you want just the header to be filled only with "="</li></ul>
     * @param display true if you want the message to be displayed now, false otherwise if you want to display the
     *                message yourself
     * @throws IllegalArgumentException if one of its conditions is met: <ol>
     *     <li><b>header</b> length is more than 50 characters</li>
     *     <li><b>content</b> is empty/blank</li>
     *     <li><b>playerEntity</b> or <b>content</b> are null</li>
     * </ol>
     */
    void createTable(ServerPlayerEntity playerEntity, String content, String header, boolean display);

    /**
     * <p>Create a pagination table for player the same way than
     * {@link PaginationApi#createTable(ServerPlayerEntity, String, String, boolean)},
     * content is separated into multiple pages.<br />
     * You can separate yourself content between 2 pages by adding a null instance of Text in content list.</p>
     * <p>Content have a time-to-live of 15 minutes (18,000 ticks)</p>
     * @param playerEntity The player who will be able to interact and see the paginated message
     * @param content Content you want to paginate
     * @param header header/title you want to add to every page, empty space is filled with "=".
     *               <p>Special values are:</p>
     *               <ul><li><b>null</b> if you doesn't want to add a header</li>
     *               <li><b>Empty text</b>if you want just the header to be filled only with "="</li></ul>
     * @param display true if you want the message to be displayed now, false otherwise if you want to display the
     *                message yourself
     * @throws IllegalArgumentException if one of its conditions is met: <ol>
     *     <li><b>header</b> length is more than 50 characters</li>
     *     <li><b>content</b> is empty/blank</li>
     *     <li><b>playerEntity</b> or <b>content</b> are null</li>
     *     </ol>
     * @see Text#empty()
     */
    void createTable(ServerPlayerEntity playerEntity, List<Text> content, @Nullable Text header, boolean display);


    /**
     * Display the given page for the given player
     * @param player display the content of this player
     * @param page display this page
     * @throws IllegalArgumentException if page is invalid
     * @throws NullPointerException if player is null or paginated content for the player doesn't exist (or have expired)
     * @see fr.altarik.toolbox.pagination.PaginatedContent#display(ServerPlayerEntity, int)
     */
    void display(ServerPlayerEntity player, int page) throws PageIndexOutOfBoundException;

}
