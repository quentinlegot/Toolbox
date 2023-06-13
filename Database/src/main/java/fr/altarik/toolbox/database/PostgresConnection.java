package fr.altarik.toolbox.database;

import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection extends AbstractSqlConnection {

    public PostgresConnection(ConnectionConfig config) throws SQLException {
        super(config);
    }

    @Override
    public void connect() throws SQLException {
        String host = config.host();
        int port = config.port();
        String username = config.username();
        String password = config.password();
        String database = config.database();
        this.connection = DriverManager.getConnection("jdbc:postgresql://"+ host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
    }
}
