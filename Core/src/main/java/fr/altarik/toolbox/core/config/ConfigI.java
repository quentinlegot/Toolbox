package fr.altarik.toolbox.core.config;

import com.google.gson.*;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Code "inspired" from <a href="https://github.com/CaffeineMC/sodium-fabric/blob/dev/src/main/java/me/jellysquid/mods/sodium/client/gui/SodiumGameOptions.java">
 *     https://github.com/CaffeineMC/sodium-fabric/blob/dev/src/main/java/me/jellysquid/mods/sodium/client/gui/SodiumGameOptions.java
 *     </a>
 */
public class ConfigI {

    protected static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().excludeFieldsWithModifiers(Modifier.PROTECTED, Modifier.PRIVATE).create();

    protected Path configPath;

    protected static Path getConfigPath(Path configPath, String name) {
        return configPath.resolve(name);
    }

    public static <T extends ConfigI> T load(Path configPath, String name, Class<T> clazz) throws IOException, JsonSyntaxException, JsonIOException {
        Path path = getConfigPath(configPath, name);

        T file;

        if(Files.exists(path)) {
            FileReader reader = new FileReader(path.toFile());
            file = GSON.fromJson(reader, clazz);
        } else {
            try {
                file = clazz.getConstructor().newInstance();
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                throw new IOException(e);
            }
        }

        file.configPath = path;

        file.writeChanges();
        return file;
    }

    public void writeChanges() throws IOException {
        Path dir = this.configPath.getParent();
        if(!Files.exists(dir)) {
            Files.createDirectories(dir);
        } else if (!Files.isDirectory(dir)) {
            throw new IOException("Not a directory: " + dir);
        }
        // Use a temporary location next to the config's final destination to replace it atomically
//        Path tempPath = this.configPath.resolveSibling(this.configPath.getFileName() + ".tmp");
        Files.writeString(this.configPath, GSON.toJson(this));
//        Files.copy(tempPath, this.configPath, StandardCopyOption.REPLACE_EXISTING);
//        Files.delete(tempPath);
        // commented because throws an error on windows each time if the file already exist
    }

}
