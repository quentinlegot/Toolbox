package fr.altarik.toolbox.database.keyvalue;

import fr.altarik.toolbox.database.SqlConnection;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KeyValueBuilder {

    private final String tableName;
    private final List<AdditionalColumn> additionalColumns;
    private final SqlConnection connection;

    private KeyValueBuilder(SqlConnection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
        this.additionalColumns = new ArrayList<>();
    }

    public static KeyValueBuilder builder(@NotNull SqlConnection connection, @NotNull String tableName) {
        return new KeyValueBuilder(connection, tableName);
    }

    public KeyValueBuilder addColumn(AdditionalColumn additionalColumn) {
        this.additionalColumns.add(additionalColumn);
        return this;
    }

    public KeyValueConnection build() throws SQLException {
        return new KeyValueConnection(connection, tableName, additionalColumns);
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
