package fr.altarik.toolbox.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface SqlConnection {

    void connect() throws SQLException;

    Connection getConnection();

    void checkConnection() throws SQLException;

    void closeConnection();

}
