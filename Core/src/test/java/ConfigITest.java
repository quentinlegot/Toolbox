import fr.altarik.toolbox.core.config.ConfigI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

public class ConfigITest {

    public static class ConfigClazz extends ConfigI {

        public int par1 = 5;
        public String par2 = "bad";
        public double para3 = 3.14;

        public static ConfigClazz load() throws IOException {
            return load(Path.of("."), "test.json", ConfigClazz.class);
        }

    }

    @Test
    public void testConfig() throws IOException {
        ConfigClazz config = ConfigClazz.load();
        Assertions.assertEquals(5, config.par1);
        Assertions.assertEquals("bad", config.par2);
        Assertions.assertEquals(3.14, config.para3);
        config.par1 = 6;
        config.par2 = "good";
        config.para3 = 4.2;
        Assertions.assertEquals(6, config.par1);
        config.writeChanges();
        config = ConfigClazz.load();
        Assertions.assertEquals(6, config.par1);
        Assertions.assertEquals("good", config.par2);
        Assertions.assertEquals(4.2, config.para3);

        Path.of(".").resolve("test.json").toFile().delete();
    }
}
