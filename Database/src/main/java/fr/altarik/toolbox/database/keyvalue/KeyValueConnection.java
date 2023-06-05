package fr.altarik.toolbox.database.keyvalue;

import fr.altarik.toolbox.database.SqlConnection;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class KeyValueConnection implements KeyValueTable {

    private final SqlConnection connection;
    private final String tableName;
    private final List<KeyValueBuilder.AdditionalColumn> additionColumns;

    public KeyValueConnection(@NotNull SqlConnection connection, @NotNull String tableName, @NotNull List<KeyValueBuilder.AdditionalColumn> additionalColumns) throws SQLException {
        this.connection = connection;
        this.tableName = tableName;
        this.additionColumns = additionalColumns;
        connection.checkConnection();

        try(Statement statement = connection.getConnection().createStatement()) {
            StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(tableName).append("(id SERIAL,");
            for(KeyValueBuilder.AdditionalColumn additionalColumn : additionalColumns) {
                sql.append(additionalColumn.columnName()).append(" ").append(additionalColumn.type().getName());
                if(additionalColumn.notNull()) {
                    sql.append(" NOT NULL ");
                }
                if(additionalColumn.reference() != null) {
                    sql.append("REFERENCES ").append(additionalColumn.reference().referenceTable()).append("(");
                    for(int i = 0; i < additionalColumn.reference().referenceColumns().length; ++i) {
                        sql.append(additionalColumn.reference().referenceColumns()[i]);
                        if(i != additionalColumn.reference().referenceColumns().length - 1) {
                            sql.append(", ");
                        }
                    }
                    sql.append(")");
                }
                sql.append(",");
                sql.append("key VARCHAR(50) NOT NULL,").append("value TEXT NOT NULL,").append("PRIMARY KEY(id)");
            }
            statement.executeUpdate(sql.toString());


        }
    }

    @Override
    public @Nullable String getValueById(int id) throws SQLException {
        connection.checkConnection();
        try(PreparedStatement preparedStatement = connection.getConnection().prepareStatement("SELECT value FROM " + tableName + " WHERE id=?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getString(1);
            }
            return null;
        }
    }

    @Override
    public @Nullable Result getValueByAdditionalColumnAndKey(String key, List<Pair<KeyValueBuilder.AdditionalColumn, Object>> additionalColumns) throws SQLException {
        connection.checkConnection();
        StringBuilder sql = new StringBuilder("SELECT id, value FROM ").append(tableName).append(" WHERE key=? AND ");
        for(int i = 0; i < additionalColumns.size(); ++i) {
            sql.append(additionalColumns.get(i).getLeft().columnName()).append("=?");
            if(i != additionalColumns.size() - 1) {
                sql.append(" AND ");
            }
        }
        sql.append(";");
        try(PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql.toString())) {
            preparedStatement.setString(1, key);
            for(int i = 0; i < additionalColumns.size(); ++i) {
                preparedStatement.setObject(i + 1, additionalColumns.get(i).getRight());
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
                return new Result(resultSet.getString(1), resultSet.getString(2));
            return null;
        }
    }

    @Override
    public long insertValue(String key, String value, List<Pair<KeyValueBuilder.AdditionalColumn, Object>> additionalColumns) throws SQLException {
        connection.checkConnection();
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + "(key, value");
        for(int i = 0; i < additionalColumns.size(); ++i) {
            if(i != additionalColumns.size() - 1) {
                sql.append(", ");
            }
            sql.append(additionalColumns.get(i).getLeft().columnName());

        }
        sql.append(") VALUES (?, ?");
        for(int i = 0; i < additionalColumns.size(); ++i) {
            if(i != additionalColumns.size() - 1) {
                sql.append(", ");
            }
            sql.append("?");
        }
        sql.append(");");
        try(PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, key);
            preparedStatement.setString(2, value);
            for(int i = 0; i < additionalColumns.size(); ++i) {
                preparedStatement.setObject(i + 3, additionalColumns.get(i).getRight());
            }
            preparedStatement.executeUpdate();
            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if(resultSet.next()) {
                    return resultSet.getLong(1);
                }
                return -1L;
            }
        }
    }

    @Override
    public void updateValueById(String key, String value) throws SQLException {
        connection.checkConnection();
        try(PreparedStatement preparedStatement = connection.getConnection().prepareStatement("UPDATE " + tableName + " SET value=? WHERE key=?")) {
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, key);
            preparedStatement.executeUpdate();
        }

    }

    @Override
    public void updateValue(String key, String value, List<Pair<KeyValueBuilder.AdditionalColumn, Object>> additionalColumns) throws SQLException {
        connection.checkConnection();
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET value=? WHERE key=?");
        for(int i = 0; i < additionalColumns.size(); ++i) {
            if(i != additionalColumns.size() - 1) {
                sql.append(" AND ");
            }
            sql.append(additionalColumns.get(i).getLeft().columnName()).append("=?");
        }
        sql.append(";");
        try(PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql.toString())) {
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, key);
            for(int i = 0; i < additionalColumns.size(); ++i) {
                preparedStatement.setObject(3 + i, additionalColumns.get(i).getRight());
            }
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<KeyValueBuilder.AdditionalColumn> getAdditionColumns() {
        return additionColumns;
    }
}
