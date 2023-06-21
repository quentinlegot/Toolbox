package fr.altarik.toolbox.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public abstract class Registry<T, U> {

    protected final HashMap<T, U> registry = new HashMap<>();

    /**
     * Return a set view of the keys contained in the registry.
     * @return An iterator view of the keys contained in the registry
     */
    public Iterator<T> keySet() {
        return registry.keySet().iterator();
    }

    /**
     * Returns true if this registry contains the key.
     * @param key the key whose presence in the registry is to be tested
     * @return Returns true if this registry contains the key, false otherwise
     */
    public boolean containsKey(T key) {
        return registry.containsKey(key);
    }

    /**
     * Returns the containing value represented by a specific key, or {@code null} if the registry do not contain the key
     * @param key the key whose associated value is to be returned
     * @return The value to which the specified key is represented, or {@code null} if the registry do not contain the key
     */
    public @Nullable U getValue(T key) {
        return registry.get(key);
    }

    /**
     * If the specified key is not already associated with a value, associate it with the given value.
     * @param key Key to which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @throws NullPointerException if key or value is null
     */
    public void register(@NotNull T key, @NotNull U value) {
        registry.putIfAbsent(Objects.requireNonNull(key), Objects.requireNonNull(value));
    }

}

