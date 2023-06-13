package fr.altarik.toolbox.pagination.precondition;

import java.util.function.Predicate;

/**
 * This predicate returns true if the String is not null or
 * if its content is not blank (empty or only contains whitespaces)
 */
public class ContentCondition implements Predicate<String> {
    @Override
    public boolean test(String s) {
        return s != null && !s.isBlank();
    }

}
