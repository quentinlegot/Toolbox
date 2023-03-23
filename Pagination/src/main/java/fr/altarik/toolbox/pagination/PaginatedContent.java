package fr.altarik.toolbox.pagination;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PaginatedContent {

    private final List<Page> pages;
    private final String header;

    public PaginatedContent(String header, String content) {
        this.header = header;
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
        int line = 0;
        List<String> currentPage = new ArrayList<>();
        for(String elem : secondSplit) {
            line++;
            currentPage.add(elem);
            if(line == 8 || elem.isEmpty()) {
                pages.add(new Page(currentPage));
                line = 0;
                currentPage = new ArrayList<>();
            }
        }
    }

    public void display(ServerPlayerEntity playerEntity, int page) {
       if(page >= this.pages.size()) {
           throw new IllegalArgumentException("There's " + this.pages.size() + " paginated pages but you wanted page n°" + page);
       } else if(page < 0) {
           throw new IllegalArgumentException("argument page is lower than 0");
       } else {
           playerEntity.sendMessage(Text.literal(header));
           for(String s : pages.get(page).lines) {
               playerEntity.sendMessage(Text.literal(s));
           }
           MutableText left = Text.literal("<").styled(
                   style -> style
                           .withColor(Formatting.YELLOW)
                           .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/table page " + (page - 1)))
           );
           MutableText right = Text.literal(">").styled(
                   style -> style
                           .withColor(Formatting.YELLOW)
                           .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/table page " + (page + 1)))
           );
           playerEntity.sendMessage(left.append(" " + page + " ").append(right));
       }
    }

    private record Page(List<String> lines) {
    }

}
