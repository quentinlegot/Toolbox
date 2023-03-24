package fr.altarik.toolbox.pagination;

import fr.altarik.toolbox.pagination.api.PageIndexOutOfBoundException;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PaginatedContent {

    private final List<Page> pages;
    private final Text header;

    public PaginatedContent(String header, String content) {
        this.header = buildHeader(header);
        pages = new ArrayList<>();
        List<String> secondSplit = new ArrayList<>();
        for(String elem : Stream.of(content.split("\n")).collect(Collectors.toCollection(ArrayList::new))) {
            if(elem.length() > 50) {
                secondSplit.add(elem.substring(0, 50));
                secondSplit.add(elem.substring(51, elem.length() - 1));
            } else {
                secondSplit.add(elem);
            }
        }
        List<Text> currentPage = new ArrayList<>();
        for(String elem : secondSplit) {
            if(!elem.isEmpty()) {
                currentPage.add(Text.literal(elem));
            }
            if(currentPage.size() == 8 || elem.isEmpty()) {
                pages.add(new Page(currentPage));
                currentPage = new ArrayList<>();
            }
        }
        pages.add(new Page(currentPage));
    }

    public PaginatedContent(@Nullable Text header, List<Text> content) {
        this.header = buildHeader(header);
        this.pages = new ArrayList<>();
        List<Text> currentPage = new ArrayList<>();
        for(Text elem : content) {
            if(elem != null)
                currentPage.add(elem);
            if(currentPage.size() == 8 || elem == null) {
                pages.add(new Page(currentPage));
                currentPage = new ArrayList<>();
            }
        }
        pages.add(new Page(currentPage));
    }

    private Text buildHeader(@Nullable String header) {
        int numberOfEq = (50 - (header != null ? header.length() : 0)) / 2;
        return Text.literal("=".repeat(numberOfEq) + " " + header + " " + "=".repeat(numberOfEq));
    }

    private Text buildHeader(@Nullable Text header) {
        int numberOfEq = (50 - (header != null ? header.getString().length() : 0)) / 2;
        return Text.literal("=".repeat(numberOfEq) + " ").append(header).append(" " + "=".repeat(numberOfEq));
    }

    public void display(ServerPlayerEntity playerEntity, int page) throws PageIndexOutOfBoundException {
       if(page >= this.pages.size()) {
           throw new PageIndexOutOfBoundException("api.pagination.page_higher_than_expected", this.pages.size(), (page + 1));
       } else if(page < 0) {
           throw new PageIndexOutOfBoundException("api.pagination.page_lower_than_0");
       } else {
           playerEntity.sendMessage(header);
           for(Text s : pages.get(page).lines) {
               playerEntity.sendMessage(s);
           }

           playerEntity.sendMessage(buildFooter(page));
       }
    }

    private Text buildFooter(int page) {
        String strPage = String.valueOf(page + 1);
        int numberOfEq = (46 - strPage.length()) / 2;
        MutableText left = Text.literal(" <").styled(
                style -> style
                        .withColor(Formatting.YELLOW)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/table page " + (page - 1)))
        );
        MutableText middle = Text.literal(" " + strPage + " ").styled(style -> style.withColor(Formatting.RESET));
        MutableText right = Text.literal("> ").styled(
                style -> style
                        .withColor(Formatting.YELLOW)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/table page " + (page + 1)))
        );
        return Text.literal("=".repeat(numberOfEq))
                .append(left)
                .append(middle)
                .append(right)
                .append(
                        Text.literal("=".repeat(numberOfEq))
                                .styled(style -> style.withColor(Formatting.RESET))
                );
    }

    private record Page(List<Text> lines) {
    }

}
