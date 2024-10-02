package io.github.cakilgan.core.util;

import io.github.cakilgan.clogger.format.CLColor;
import io.github.cakilgan.core.CakilganCore;
import io.github.cakilgan.core.io.FileHelper;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
    public static ArrayList<String> convertDottedStringToList(String list){
        return makeList(list.replace(',','\040'));
    }
    public static void writeFileAndExit(File file,String s){
        try {
            FileHelper helper = CakilganCore.createHelper(file);
            helper.resetNotAppend();
            helper.writeln(s);
            helper.exitAndSave();
            helper = null;
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
