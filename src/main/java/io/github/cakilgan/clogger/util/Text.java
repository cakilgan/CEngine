package io.github.cakilgan.clogger.util;


import io.github.cakilgan.clogger.format.CLColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Text {
    public static String surroundWith(char a ,char b,String s){
        return a+s+b;
    }
    public static String colorize(String s,String colorcode){
        return colorcode+s+ CLColor.RESET;
    }
    public static String surroundWithCurlyBracket(String s){
        return surroundWith('{','}',s);
    }
    public static String surroundWithRectalBracket(String s){
        return surroundWith('[',']',s);
    }
    public static String surroundWithBracket(String s){
        return surroundWith('(',')',s);
    }
    public static String unSurround(String s,char a, char b){
        return s.replace(a,'\040').replace(b,'\040').trim();
    }
    public static String unSurroundCurlyBracket(String s){
        return unSurround(s,'{','}');
    }
    public static String unSurroundRectalBracket(String s){
        return unSurround(s,'[',']');
    }
    public static String unSurroundBracket(String s){
        return unSurround(s,'(',')');
    }
    public static ArrayList<String> makeList(String makeList){
        Scanner scanner = new Scanner(makeList);
        ArrayList<String> rtrn = new ArrayList<>();
        do {
            rtrn.add(scanner.next());
        }while (scanner.hasNext());
        return rtrn;
    }
    public static ArrayList<String> convertDottedStringToList(String input){
        ArrayList<String> result = new ArrayList<>();

        if (input == null || input.isEmpty()) {
            return result;
        }

        // Regex pattern to match elements including content within curly braces
        Pattern pattern = Pattern.compile("([^,{}]*(?:\\{[^}]*\\})?[^,]*)+");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String matched = matcher.group().trim();
            if (!matched.isEmpty()) {
                result.add(matched);
            }
        }

        return result;
    }
    public static String removeOuterCurlyBraces(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Trim leading and trailing whitespace
        input = input.trim();

        // Check if the input starts with '{' and ends with '}'
        if (input.startsWith("{") && input.endsWith("}")) {
            // Remove the outer curly braces
            return input.substring(1, input.length() - 1).trim();
        }

        // Return the original input if it doesn't start and end with curly braces
        return input;
    }

    private static final Pattern ANSI_ESCAPE = Pattern.compile("\u001B\\[[;\\d]*m");
    public static String removeColorCodes(String log) {
        if (log == null) {
            return null;
        }
        return ANSI_ESCAPE.matcher(log).replaceAll("");
    }
}
