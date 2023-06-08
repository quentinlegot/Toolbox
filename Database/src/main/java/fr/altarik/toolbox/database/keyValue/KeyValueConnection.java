package fr.altarik.toolbox.database.keyValue;

import fr.altarik.toolbox.database.SqlConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class KeyValueConnection implements KeyValueTable {

    private final SqlConnection connection;
    private final String tableName;

    public KeyValueConnection(@NotNull SqlConnection connection, @NotNull String tableName) throws SQLException {
        this.connection = connection;
        this.tableName = tableName;
        connection.checkConnection();

        try(Statement statement = connection.getConnection().createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + "(key VARCHAR(50) NOT NULL, value TEXT NOT NULL, PRIMARY KEY(key));");
        }
    }

    @Override
    public @Nullable String getValue(String key) throws SQLException {
        connection.checkConnection();
        try(PreparedStatement preparedStatement = connection.getConnection().prepareStatement("SELECT value FROM " + tableName + " WHERE key=?;")) {
            preparedStatement.setString(1, key);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
                return resultSet.getString(1);
            return null;
        }
    }

    @Override
    public void insertValue(String key, String value) throws SQLException {
        connection.checkConnection();
        try(PreparedStatement preparedStatement = connection.getConnection().prepareStatement("INSERT INTO " + tableName + "(key, value) VALUES (?, ?);")){
            preparedStatement.setString(1, key);
            preparedStatement.setString(2, value);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateValue(String key, String value) throws SQLException {
        connection.checkConnection();
        try(PreparedStatement preparedStatement = connection.getConnection().prepareStatement("UPDATE " + tableName + " SET value=? WHERE key=?;")) {
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, key);
            preparedStatement.executeUpdate();
        }
    }
}
