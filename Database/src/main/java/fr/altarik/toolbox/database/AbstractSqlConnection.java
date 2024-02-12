package fr.altarik.toolbox.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractSqlConnection implements SqlConnection {

    protected final ConnectionConfig config;
    protected Connection connection;

    protected AbstractSqlConnection(ConnectionConfig config) throws SQLException {
        this.config = config;
        DriverManager.setLoginTimeout(3);
        connect();
    }

    public void checkConnection() throws SQLException {
        if(connection == null || connection.isClosed() || !connection.isValid(1))
            connect();
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws Exception {
        if(!connection.isClosed()) {
            connection.close();
            connection = null;
        }
    }
}
