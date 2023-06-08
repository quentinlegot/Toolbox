package fr.altarik.toolbox.database;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


class ConnectionTest {

    @Test
    void databaseTest() {
        assertDoesNotThrow(() -> {
            InputStream configInput = getResource("config.yml");
            String configStr = new BufferedReader(new InputStreamReader(Objects.requireNonNull(configInput)))
                    .lines().collect(Collectors.joining("\n"));
            Gson gson = new Gson();
            ConnectionConfig config = gson.fromJson(configStr, ConnectionConfig.class);
            SqlConnection connection = Connections.newPostgresConnection(config);
            try(PreparedStatement statement = connection.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS toolbox(id SERIAL, PRIMARY KEY (id));")) {
                statement.executeUpdate();
            }
            connection.close();
        });
    }

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
