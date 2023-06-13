package fr.altarik.toolbox.database.keyValue;

import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;

/**
 * <p>Implement of a key value table, abstract the actual representation of the table and its manipulation between this interface</p>
 * @see KeyValuePostgresql
 */
public interface KeyValueTable {

    /**
     * <p>Return the first value associated with the unique key.</p>
     *
     * @param key               String key of associated to the value
     * @return value associated with the key
     * @throws SQLException if connection is lost
     */
    @Nullable String getValue(String key) throws SQLException;

    /**
     * <p>Insert a new value in the table, associated with key</p>
     *
     * @param key String key which will be associated with the value, is unique
     * @param value String value which will be stored in the database
     * @throws SQLException if connection is lost or if {@code key} is not unique (already exist in database)
     */
    void insertValue(String key, String value) throws SQLException;

    /**
     * <p>Update value column of the row associated with the key by {@code value}</p>
     * <p>If {@code key} doesn't exist in table, will update no row without warning</p>
     * @param key String key which will is associated with the value, is unique
     * @param value new value
     * @throws SQLException if connection is lost
     */
    void updateValue(String key, String value) throws SQLException;

    /**
     * <p>Delete row with having {@code key} as unique key</p>
     * <p>If key doesn't exist in database, will delete no row without warning</p>
     * @param key the key of the row to delete
     * @throws SQLException if connection is lost
     */
    void deleteRow(String key) throws SQLException;

    /**
     * Will delete every data inside the table
     * @throws SQLException if connection is lost
     */
    void truncateTable() throws SQLException;

}
