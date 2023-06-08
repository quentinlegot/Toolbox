import fr.altarik.toolbox.core.builder.IBuilder;
import fr.altarik.toolbox.core.builder.RequiredCollectionParameterBuilder;
import fr.altarik.toolbox.core.builder.RequiredParamBuilder;

import java.util.ArrayList;
import java.util.List;

public class BuilderImpl implements IBuilder<BuilderResult> {

    private final RequiredCollectionParameterBuilder<String, List<String>> collection;
    private final RequiredParamBuilder<Integer> numberOfSentences;

    private BuilderImpl(boolean canBeEmpty) {
        this.collection = new RequiredCollectionParameterBuilder<>(new ArrayList<>(), canBeEmpty);
        this.numberOfSentences = new RequiredParamBuilder<>();
    }

    public BuilderImpl addSentence(String sentence) {
        collection.add(sentence);
        return this;
    }

    public BuilderImpl numberOfSentence(int i) {
        this.numberOfSentences.set(i);
        return this;
    }

    public static BuilderImpl builder(boolean canBeEmpty) {
        return new BuilderImpl(canBeEmpty);
    }



    @Override
    public BuilderResult build() throws Exception {
        return new BuilderResult(collection.get(), numberOfSentences.get());
    }
}
