package se.dungeonsandmorons.helper;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CapitalizeWords {
    public static String capitalizeWords(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return Arrays.stream(input.split("\\s+"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }
}
