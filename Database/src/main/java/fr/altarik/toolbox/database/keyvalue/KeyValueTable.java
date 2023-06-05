package fr.altarik.toolbox.database.keyvalue;

import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;

public interface KeyValueTable {

    /**
     * Return the value which correspond to the given id
     * @param id primary key, unique id of the key value table
     * @return the correspond value
     * @throws SQLException when connection is lost
     */
    @Nullable String getValueById(int id) throws SQLException;

    /**
     * <p>Return the first value associated with the key and all the additional columns values given.</p>
     * <p>In a perfect context, key and additional columns combinaison return a pseudo unique result.</p>
     *
     * @param key               String key of associated to the value, doesn't require to be unique
     * @param additionalColumns Object keys of associated to the value, doesn't require to be unique
     * @return first (id, value) pair associated with the key and additonal columns combinaison
     * @throws SQLException if connection is lost, or if additional columns name or value format is incorrect
     */
    @Nullable Result getValueByAdditionalColumnAndKey(String key, List<Pair<KeyValueBuilder.AdditionalColumn, Object>> additionalColumns) throws SQLException;

    /**
     * <p>Insert a new value in the table, associate key and additional columns with the value</p>
     * <p>Key and additional columns doesn't need to be unique, the combinaison of them have the goal to be unique or it isn't a hard requirement</p>
     * @param key String key which will be associated with the value, doesn't require to be unique
     * @param value String value which will be stored in the database
     * @param additionalColumns Additional columns which will be associated with the value, doesn't require to be unique
     * @return the id (unique id) of the newly inserted value
     * @throws SQLException if connection is lost, or if additional columns name or value format is incorrect
     */
    long insertValue(String key, String value, List<Pair<KeyValueBuilder.AdditionalColumn, Object>> additionalColumns) throws SQLException;

    /**
     * Update a row associated with the unique key {@code id} with the new value given in parameter
     * @param key unique key associated with the value
     * @param value new value
     * @throws SQLException if connection is lost
     */
    void updateValueById(String key, String value) throws SQLException;

    /**
     * Update all rows associated with the key and additional columns combinaisons with the new value given in parameter
     * @param key String key which will is associated with the value, doesn't require to be unique
     * @param value new value
     * @param additionalColumns Additional columns which are associated with the value, doesn't require to be unique
     * @throws SQLException if connection is lost, or if additional columns name or value format is incorrect
     */
    void updateValue(String key, String value, List<Pair<KeyValueBuilder.AdditionalColumn, Object>> additionalColumns) throws SQLException;

    /**
     * Give the declared additional columns given during initialisation of the class
     * @return list of declared additional columns
     */
    List<KeyValueBuilder.AdditionalColumn> getAdditionColumns();

    /**
     * Result unique id and value pair stored in the table when using {@link KeyValueTable#getValueByAdditionalColumnAndKey(String, List)}
     * @param id unique id the row
     * @param value value store in the same row as {@code id}
     */
    record Result(String id, String value) {

    }
}
