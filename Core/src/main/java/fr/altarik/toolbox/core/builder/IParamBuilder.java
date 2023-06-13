package fr.altarik.toolbox.core.builder;

/**
 * Builder parameter, for more flexibility
 * @param <T> the parameter type
 * @see OptionalParamBuilder
 * @see RequiredParamBuilder
 * @see RequiredCollectionParameterBuilder
 */
public interface IParamBuilder<T> {

    /**
     * Get the given object, may return {@link NullPointerException} depending on the policy of implemented class
     * @return the parameter given by {@link IParamBuilder#set(Object)}
     * @throws NullPointerException may throw this error depending on the policy of implemented class
     */
    T get() throws NullPointerException;

    /**
     * Change/insert the value of the parameter
     * @param parameter the given parameter
     */
    void set(T parameter);
}
