package analyzer;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.*;
import static java.util.Collections.*;

public class PatternParser {
    public static List<Pattern> parsePatternsFromPatternStrings(List<String> lines) {
        List<Pattern> patterns = new ArrayList<>();

        for (String line : lines) {
            String[] lineParts = line.split(";");

            Pattern pattern = new Pattern();
            pattern.setPriority(parseInt(lineParts[0]));
            pattern.setPattern(lineParts[1].replace("\"", ""));
            pattern.setOutcome(lineParts[2].replace("\"", ""));

            patterns.add(pattern);
        }

        sort(patterns);
        reverse(patterns);

        return patterns;
    }
}
