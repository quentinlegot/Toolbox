package fr.altarik.toolbox.core.builder;

/**
 * Doesn't throw a {@link NullPointerException} when using {@link IParamBuilder#get()} in any case
 * @param <T> the returned type
 * @see IParamBuilder
 */
public class OptionalParamBuilder<T> implements IParamBuilder<T> {

    private T parameter;

    public OptionalParamBuilder(T param) {
        this.parameter = param;
    }
    @Override
    public T get() throws NullPointerException {
        return parameter;
    }

    @Override
    public void set(T parameter) {
        this.parameter = parameter;
    }
}
