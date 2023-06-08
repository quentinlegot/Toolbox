package fr.altarik.toolbox.database.keyvalue;

import fr.altarik.toolbox.core.builder.IBuilder;
import fr.altarik.toolbox.core.builder.RequiredCollectionParameterBuilder;
import fr.altarik.toolbox.core.builder.RequiredParamBuilder;
import fr.altarik.toolbox.database.SqlConnection;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KeyValueBuilder implements IBuilder<KeyValueConnection> {

    private final RequiredParamBuilder<String> tableName;
    private final RequiredCollectionParameterBuilder<AdditionalColumn, List<AdditionalColumn>> additionalColumns;
    private final RequiredParamBuilder<SqlConnection> connection;

    private KeyValueBuilder() {
        this.tableName = new RequiredParamBuilder<>();
        this.connection = new RequiredParamBuilder<>();
        this.additionalColumns = new RequiredCollectionParameterBuilder<>(new ArrayList<>(), true);
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

    public KeyValueBuilder addColumn(AdditionalColumn additionalColumn) {
        this.additionalColumns.add(additionalColumn);
        return this;
    }

    public KeyValueConnection build() throws SQLException {
        return new KeyValueConnection(connection.get(), tableName.get(), additionalColumns.get());
    }

    public record AdditionalColumn(@NotNull String columnName, @NotNull JDBCType type, boolean notNull, @Nullable AdditionalColumnReference reference) {
        public AdditionalColumn {
            Objects.requireNonNull(columnName);
            Objects.requireNonNull(type);
        }

        public Pair<AdditionalColumn, Object> toPair(Object value) {
            return new Pair<>(this, value);
        }
    }

    public record AdditionalColumnReference(@NotNull String referenceTable, @NotNull String... referenceColumns) {
        public AdditionalColumnReference {
            Objects.requireNonNull(referenceTable);
            if(Objects.requireNonNull(referenceColumns).length == 0)
                throw new IllegalArgumentException("Reference Columns should not be empty");
        }

    }

}
