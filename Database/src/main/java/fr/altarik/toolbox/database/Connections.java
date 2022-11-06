package fr.altarik.toolbox.database;

import java.sql.SQLException;

public class Connections {

    /**
     * Create a new Connection object for a postgresql database server
     * @return
     */
    public static SqlConnection newPostgresConnection(ConnectionConfig config) throws SQLException {
        return new PostgresConnection(config);
    }
}
