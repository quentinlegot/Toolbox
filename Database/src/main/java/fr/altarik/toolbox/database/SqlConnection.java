package fr.altarik.toolbox.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface SqlConnection extends AutoCloseable {

    /**
     * Start the connection to sql database
     * @throws SQLException if unable to connect to database
     */
    void connect() throws SQLException;

    /**
     * Get the sql connection
     * @return the connection session
     */
    Connection getConnection();

    /**
     * Reconnect you to database if it has closed or lost.
     * @throws SQLException if unable to reconnect you
     */
    void checkConnection() throws SQLException;

    /**
     * @deprecated replaced with {@link AutoCloseable#close()}
     */
    @Deprecated(forRemoval = true)
    void closeConnection();

}
