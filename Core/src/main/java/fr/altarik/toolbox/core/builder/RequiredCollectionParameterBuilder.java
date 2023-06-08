package fr.altarik.toolbox.core.builder;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;

/**
 * Mostly same as {@link RequiredParamBuilder} but for list
 * @param <E> The type contained in the collection
 * @param <T> The returned type
 */
public class RequiredCollectionParameterBuilder<E, T extends Collection<E>> implements IParamBuilder<T> {

    private final T collection;
    private final boolean canBeEmpty;

    public RequiredCollectionParameterBuilder(@NotNull T collection, boolean canBeEmpty) {
        this.collection = Objects.requireNonNull(collection);
        this.canBeEmpty = canBeEmpty;
    }

    /**
     * <p>Return the list, if not empty</p>
     * <p>If empty, return the collection if {@code canBeEmpty} if true, otherwise throw a {@link NullPointerException}</p>
     * @return the collection
     * @throws NullPointerException if collection is empty and {@code canBeEmpty} is false
     */
    @Override
    public T get() throws NullPointerException {
        if(canBeEmpty) {
            return collection;
        } else if(!collection.isEmpty()) {
            return collection;
        } else {
            throw new EmptyCollectionException("Collection cannot be empty");
        }
    }

    @Override
    public void set(T parameter) {
        throw new UnsupportedOperationException("Use `add` in place of `set` for RequiredCollectionParameterBuilder");
    }

    /**
     * Add an element to the collection
     * @param element element to add to the list
     */
    public void add(E element) {
        collection.add(element);
    }
}
