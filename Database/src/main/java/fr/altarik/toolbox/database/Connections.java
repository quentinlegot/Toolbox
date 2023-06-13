package fr.altarik.toolbox.database;

import fr.altarik.toolbox.database.keyValue.KeyValueBuilder;
import fr.altarik.toolbox.database.keyValue.KeyValueTable;

import java.sql.SQLException;

public class Connections {

    /**
     * Create a new Connection object for a postgresql database server
     * @return postgresql connection
     */
    public static SqlConnection newPostgresConnection(ConnectionConfig config) throws SQLException {
        return new PostgresConnection(config);
    }

    /**
     * Create a new (key, value) table if not exist and use it through {@link KeyValueTable} interface
     * @param connection Postgresql connection
     * @param tableName name of the table to use
     * @return interface to control the table
     * @throws SQLException if connection is lost
     */
    public static KeyValueTable newKeyValueTable(SqlConnection connection, String tableName) throws SQLException {
        return KeyValueBuilder.builder().setConnection(connection).setTableName(tableName).build();
    }
}
