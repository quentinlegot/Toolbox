package fr.altarik.toolbox.core.builder;

public interface IBuilder<T> {

    /**
     * Build the builders parameters into T object
     * @return The created objects thanks to given parameters
     * @throws Exception if any error occur during creation of the built object
     */
    T build() throws Exception;
}
