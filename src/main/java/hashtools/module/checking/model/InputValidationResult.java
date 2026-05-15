package hashtools.module.checking.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class InputValidationResult {

    private List<String> problems;



    public InputValidationResult() {
        this.problems = new ArrayList<>();
    }



    public void addProblem(String problem) {
        problems.add(problem);
    }

    public void consumeProblems(Consumer<List<String>> consumer) {
        Objects
            .requireNonNullElse(consumer, _ -> {})
            .accept(problems);
    }

    public String getBulletListFormattedProblems() {
        return problems
            .stream()
            .map("- "::concat)
            .collect(Collectors.joining(System.lineSeparator()));
    }

    public boolean hasProblems() {
        return !problems.isEmpty();
    }
}
