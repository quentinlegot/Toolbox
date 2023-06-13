package fr.altarik.toolbox.pagination.api;

import net.minecraft.text.Text;

public class PageIndexOutOfBoundException extends Exception {

    private final Text text;

    public PageIndexOutOfBoundException(String s) {
        this.text = Text.translatable(s);
    }

    public PageIndexOutOfBoundException(String s, int size, int currentPage) {
        this.text = Text.translatable(s, size, currentPage);
    }

    public Text getText() {
        return text;
    }
}
