package analyzer;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

import static analyzer.Algorithm.*;
import static analyzer.PatternParser.*;
import static java.nio.charset.StandardCharsets.*;
import static java.nio.file.Files.*;
import static java.util.Collections.*;
import static java.util.concurrent.Executors.*;
import static java.util.concurrent.TimeUnit.*;

public class Main {
    public static void main(String[] args) {
        String folderPath = args[0];
        String patternsFilePath = args[1];


        List<String> lines = readLinesFromPath(patternsFilePath);

        final List<Pattern> patterns = parsePatternsFromPatternStrings(lines);

        final File folder = new File(folderPath);
        if (folder.isDirectory()) {
            final File[] listOfFiles = folder.listFiles();
            if (listOfFiles != null) {
                ExecutorService executor = newFixedThreadPool(listOfFiles.length);
                List<Future<String>> results = new ArrayList<>();
                for (final File file : listOfFiles) {
                    results.add(executor.submit(() -> getResultForFile(file, patterns)));
                }
                executor.shutdownNow();
                try {
                    executor.awaitTermination(5, SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<String> finalResults = new ArrayList<>();
                for (Future<String> result : results) {
                    try {
                        finalResults.add(result.get());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                sort(finalResults);
                for (final String result : finalResults) {
                    System.out.println(result);
                }

            }
        }
    }

    @NotNull
    private static List<String> readLinesFromPath(String patternsFilePath) {
        try {
            return readAllLines(Paths.get(patternsFilePath), UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Nullable
    private static String getResultForFile(File file, List<Pattern> patterns) {
        try (final FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath())) {
            byte[] array = fileInputStream.readAllBytes();
            String textString = getComparableString(array);
            for (Pattern pattern : patterns) {
                byte[] toFind = pattern.getPattern().getBytes(UTF_8);
                String patternText = getComparableString(toFind);
                boolean found = patternIsContainedKmp(textString, patternText);
                if (found) {
                    return file.getName() + ": " + pattern.getOutcome();
                }
            }
            return file.getName() + ": " + "Unknown file type";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @NotNull
    private static String getComparableString(final byte[] allBytes) {
        return Arrays.toString(allBytes).replace("[", "").replace("]", "");
    }
}