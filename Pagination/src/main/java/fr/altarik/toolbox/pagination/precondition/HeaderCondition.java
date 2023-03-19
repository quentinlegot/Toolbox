package fr.altarik.toolbox.pagination.precondition;

import java.util.function.Predicate;

/**
 * This predicate returns true if its length doesn't exceed 50 characters.
 */
public class HeaderCondition implements Predicate<String> {
    @Override
    public boolean test(String header) {
        return header.length() <= 50;
    }
}
