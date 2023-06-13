package fr.altarik.toolbox.database.keyValue;

import com.google.gson.Gson;
import fr.altarik.toolbox.database.ConnectionConfig;
import fr.altarik.toolbox.database.Connections;
import fr.altarik.toolbox.database.SqlConnection;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class KeyValueTest {

    @Test
    void tableTest() {
        System.out.println("Hello");
        assertDoesNotThrow(() -> {InputStream configInput = getResource("config.yml");
            String configStr = new BufferedReader(new InputStreamReader(Objects.requireNonNull(configInput)))
                    .lines().collect(Collectors.joining("\n"));
            Gson gson = new Gson();
            ConnectionConfig config = gson.fromJson(configStr, ConnectionConfig.class);
            try(SqlConnection connection = Connections.newPostgresConnection(config)) {
                KeyValueTable keyValueTable = Connections.newKeyValueTable(connection, "toolbox_keyvalue");
                keyValueTable.truncateTable();
                keyValueTable.insertValue("location", "here");
                keyValueTable.insertValue("experience", "5");
                assertEquals("here", keyValueTable.getValue("location"));
                assertEquals("5", keyValueTable.getValue("experience"));
                keyValueTable.updateValue("location", "Elsewhere");
                assertEquals("Elsewhere", keyValueTable.getValue("location"));
                assertEquals("5", keyValueTable.getValue("experience"));
                keyValueTable.updateValue("experience", "10");
                assertEquals("Elsewhere", keyValueTable.getValue("location"));
                assertEquals("10", keyValueTable.getValue("experience"));
                keyValueTable.deleteRow("experience");
                assertEquals("Elsewhere", keyValueTable.getValue("location"));
                assertNull(keyValueTable.getValue("experience"));
                keyValueTable.truncateTable();
                assertNull(keyValueTable.getValue("location"));
                assertNull(keyValueTable.getValue("experience"));
            }
        });
    }

    // TODO: 08/06/2023 Move to Core module in a toolkit class
    private InputStream getResource(String resourcePath) {
        try {
            URL url = this.getClass().getClassLoader().getResource(resourcePath);
            if(url == null)
                return null;

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException e){
            return null;
        }
    }

}
