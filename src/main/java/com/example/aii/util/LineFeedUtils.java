package com.example.aii.util;

public class LineFeedUtils {

    public static String toCRLF(String in) {
        return in.replaceAll("(?<!\r)\n", "\r\n")
                .replaceAll("\r(?!\n)", "\r\n");
    }

    public static String toLF(String in) {
        return in.replaceAll("\r\n", "\n")
                .replaceAll("\r", "\n");
    }

    public static String toCR(String in) {
        return in.replaceAll("\r\n", "\r")
                .replaceAll("\n", "\r");
    }
}
