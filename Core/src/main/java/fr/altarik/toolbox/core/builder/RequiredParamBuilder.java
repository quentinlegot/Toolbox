package fr.altarik.toolbox.core.builder;

import java.util.Objects;

/**
 * Throw a {@link NullPointerException} when using {@link IParamBuilder#get()} if the parameter doesn't have been initialized
 * @param <T> the returned type
 */
public class RequiredParamBuilder<T> implements IParamBuilder<T> {

    private T parameter;

    public RequiredParamBuilder(T parameter) {
        this.parameter = parameter;
    }

    public RequiredParamBuilder() {
        this(null);
    }

    @Override
    public T get() {
        return Objects.requireNonNull(parameter);
    }

    @Override
    public void set(T parameter) {
        this.parameter = parameter;
    }
}
