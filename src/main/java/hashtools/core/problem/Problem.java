package hashtools.core.problem;

import java.util.Objects;

public class Problem {

    private String description;
    private String cause;
    private String fix;



    public Problem() {
        this.description = "";
        this.cause = "";
        this.fix = "";
    }



    public String getDescription() {
        return description;
    }

    public Problem withDescription(String description) {
        this.description = Objects.requireNonNullElse(description, "");
        return this;
    }

    public String getCause() {
        return cause;
    }

    public Problem withCause(String cause) {
        this.cause = Objects.requireNonNullElse(cause, "");
        return this;
    }

    public String getFix() {
        return fix;
    }

    public Problem withFix(String fix) {
        this.fix = Objects.requireNonNullElse(fix, "");
        return this;
    }

    @Override
    public String toString() {
        return "Problem{" +
            "description='" + description + '\'' +
            ", cause='" + cause + '\'' +
            ", fix='" + fix + '\'' +
            '}';
    }
}
