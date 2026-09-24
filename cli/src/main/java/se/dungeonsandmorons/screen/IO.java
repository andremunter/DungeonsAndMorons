package se.dungeonsandmorons.screen;

import java.util.Scanner;

public final class IO {
    private static final Scanner SCANNER = new Scanner(System.in);

    private IO() {}

    public static void println(Object obj) {
        System.out.println(obj);
    }

    public static void println() {
        System.out.println();
    }

    public static void print(Object obj) {
        System.out.print(obj);
    }

    public static void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

    public static String readln() {
        return SCANNER.hasNextLine() ? SCANNER.nextLine() : "";
    }

    // Handles IO.readln("> ")
    public static String readln(String prompt) {
        System.out.print(prompt);
        return readln();
    }
}