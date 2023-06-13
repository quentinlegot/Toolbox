import fr.altarik.toolbox.core.builder.EmptyCollectionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class BuilderTest {


    @Test
    void builderTest() throws Exception {
        BuilderImpl builder = BuilderImpl.builder(true);
        builder.addSentence("First sentence");
        builder.addSentence("Second sentence");
        builder.numberOfSentence(2);
        BuilderResult res = builder.build();
        Assertions.assertEquals(Arrays.asList("First sentence", "Second sentence"), res.sentences());
        Assertions.assertEquals(res.numberOfSentences(), 2);

        BuilderImpl builder1 = BuilderImpl.builder(false);
        builder1.numberOfSentence(3);
        Assertions.assertThrowsExactly(EmptyCollectionException.class, builder1::build);

        BuilderImpl builder2 = BuilderImpl.builder(true);
        builder2.numberOfSentence(3);
        Assertions.assertDoesNotThrow(builder2::build);

        BuilderImpl builder3 = BuilderImpl.builder(true);
        Assertions.assertThrowsExactly(NullPointerException.class, builder3::build);
    }

}
