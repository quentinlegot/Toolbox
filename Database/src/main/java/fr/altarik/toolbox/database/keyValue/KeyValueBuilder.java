package fr.altarik.toolbox.database.keyValue;

import fr.altarik.toolbox.core.builder.IBuilder;
import fr.altarik.toolbox.core.builder.RequiredParamBuilder;
import fr.altarik.toolbox.database.SqlConnection;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;


public class KeyValueBuilder implements IBuilder<KeyValuePostgresql> {

    private final RequiredParamBuilder<String> tableName;
    private final RequiredParamBuilder<SqlConnection> connection;

    private KeyValueBuilder() {
        this.tableName = new RequiredParamBuilder<>();
        this.connection = new RequiredParamBuilder<>();
    }

    public static KeyValueBuilder builder() {
        return new KeyValueBuilder();
    }

    public KeyValueBuilder setConnection(@NotNull SqlConnection connection) {
        this.connection.set(connection);
        return this;
    }

    public KeyValueBuilder setTableName(@NotNull String tableName) {
        this.tableName.set(tableName);
        return this;
    }

    public KeyValuePostgresql build() throws SQLException {
        return new KeyValuePostgresql(connection.get(), tableName.get());
    }

}
