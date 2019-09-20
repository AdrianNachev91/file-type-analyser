package analyzer;

import org.jetbrains.annotations.NotNull;

import static java.lang.Integer.*;

public class Pattern implements Comparable<Pattern> {

    private int priority;
    private String pattern;
    private String outcome;

    private int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    @Override
    public int compareTo(@NotNull Pattern o) {
        return compare(this.priority, o.getPriority());
    }
}
