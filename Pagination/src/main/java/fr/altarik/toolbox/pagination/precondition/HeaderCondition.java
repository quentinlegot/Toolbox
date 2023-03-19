package fr.altarik.toolbox.pagination.precondition;

import java.util.function.Predicate;

/**
 * This predicate returns true if header is not null, not blank (not empty excluding whitespaces)
 * and if its length doesn't exceed 50 characters.
 */
public class HeaderCondition implements Predicate<String> {
    @Override
    public boolean test(String header) {
        return header != null && !header.isBlank() && header.length() <= 50;
    }
}
