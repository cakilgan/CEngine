package io.github.cakilgan.core.io;

import io.github.cakilgan.clogger.system.CLoggerSystem;
import io.github.cakilgan.core.CakilganCore;
import io.github.cakilgan.core.io.automation.BasicStringPattern;
import io.github.cakilgan.core.io.automation.Pattern;
import io.github.cakilgan.core.serialization.CakilganComponent;
import io.github.cakilgan.core.serialization.Category;
import io.github.cakilgan.clogger.CLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;

public class FileHelper extends CakilganComponent {
    public static final CLogger LOGGER;;
    public static final FileHelper INSTANCE;
    static {
        LOGGER = CLoggerSystem.logger(FileHelper.class);
        try {
            INSTANCE = new FileHelper(new File("serial.comp"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class Reader{
        File file;
        BufferedReader reader;
        ArrayList<String> Lines;
        HashMap<Integer,ArrayList<String>> map = new HashMap<>();
        ArrayList<String> finalList;
        boolean setupChecker;
        public Reader(File file) throws IOException {
            this.file = file;
            this.reader = new BufferedReader(new FileReader(file));
        }
        private void cs() throws IOException {
            if (!setupChecker){
                LOGGER.debug("setup is false! doing a new setup.");
                setupForExclusive();
            }
        }
        private int readSetup(int j) throws IOException {
            reader.mark(j);
            int rtrn = 0;
            for (int i = 0; i < j; i++) {
                char s = read();
                if (s!='\0'){
                    rtrn++;
                }
            }
            reader.reset();
            return rtrn;
        }
        private void reset() throws FileNotFoundException {
            LOGGER.debug("resetting fileReader");
            reader = new BufferedReader(new FileReader(this.file));
        }
        private void setupForExclusive() throws IOException {
            LOGGER.debug("exclusive setup");
            Lines = readLines();
            reset();
            int i = 0;
            finalList = new ArrayList<>();
            for(String s: Lines){
                if (s.isEmpty()){
                    continue;
                }
                ArrayList<String> list = convertStringToList(s);
                finalList.addAll(list);
                map.put(i,list);
                i++;
            }
            setupChecker = true;
        }
        private ArrayList<String> convertStringToList(String str){
            //LOGGER.debug("converting "+str+" to List.");
            ArrayList<String> rtrn = new ArrayList<>();
            Scanner scanner = new Scanner(str);

                do {
                    String s = null;
                    try {
                        s = scanner.next();
                        rtrn.add(s);
                    }
                    catch (Exception e) {
                        try {
                            LOGGER.excFalse(e);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }while (scanner.hasNext());
            return rtrn;
        }
        private boolean canRead(){
            try {
                return reader.ready();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        private void mark(int i) throws IOException {
            LOGGER.debug("reader marked to "+i);
            reader.mark(i);
        }

        public char read() throws IOException {
            char rtrn;
            if (reader.ready()){
               rtrn = (char) reader.read();
            }else{
                rtrn = '\0';
            }
            return rtrn;
        }
        private ArrayList<String> readLines() throws IOException {
            ArrayList<String> rtrn = new ArrayList<>();
            do {
                rtrn.add(readLine());
            }while (canRead());
            return rtrn;
        }
        public String readLine() throws IOException {
            return canRead()?reader.readLine():"";
        }
        public char[] read(int i) throws IOException {
            int realLength = readSetup(i);
            char[] rtrn = new char[realLength];
            for (int j = 0; j < realLength; j++) {
                try {
                    char s = read();
                    if (s!='\0'){
                        rtrn[j] = s;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return rtrn;
        }
        public String getLine(int i) throws IOException {
            cs();
            LOGGER.debug("returning line "+i);
            return Lines.get(i);
        }
        public String getLastLine() throws IOException {
            cs();
            return Lines.get(Lines.size()-1);
        }
        public String getFirstLine() throws IOException {
            cs();
            return Lines.get(0);
        }
        public String readWord() throws IOException {
            StringBuilder rtrn = new StringBuilder();
            do {
                char read = read();
                if (read=='\040'||read=='\0'){
                    break;
                }
                else{
                    rtrn.append(read);
                }
            }while (canRead());
            return rtrn.toString();
        }
        public String getWord(int line, int wordNum) throws IOException {
            cs();
            return map.get(line).get(wordNum);
        }
        public String getWord(int wordNum) throws IOException {
            cs();
            return finalList.get(wordNum);
        }
        public <R ,T extends Pattern<R>>ArrayList<R> readPattern(T pattern) throws IOException {
            ArrayList<R> rtrn = new ArrayList<>();
            for (int i = 0; i < map.size(); i++) {
                ArrayList<String> map2 =  map.get(i);
                for (int j = 0; j < map2.size(); j++) {
                    if (map2.get(j).equals(pattern.getPatternFirstText())){
                        rtrn.add((R) map2.get(j+1));
                    }
                }
            }
           return rtrn;
        }
    }
    private static class Writer{
        File file;
        FileWriter fwriter;
        BufferedWriter writer;
        public Writer(File file) throws IOException {
            this.file = file;
            this.fwriter = new FileWriter(this.file,true);
            this.writer = new BufferedWriter(fwriter);
        }
        private void ln() throws IOException {
            writer.newLine();
        }
        public void write(int chr) throws IOException {
            writer.write(chr);
        }
        public void write(String str) throws IOException {

            writer.write(str);

        }
        public void writeln(String  str) throws IOException {
            ln();
            write(str);
        }
        public void app(char chr) throws IOException {
            writer.append(chr);
        }
        public void app(String str) throws IOException {
            writer.append(str);
        }
        public void appln(String str) throws IOException {
            app(str);
            ln();
        }
        public void exitAndSave() throws IOException {
            writer.flush();
            writer.close();
        }
        public <R ,T extends Pattern<R>>void writePattern(T pattern,R val) throws IOException {
            write(pattern.makePatternText(val));
        }
        public <R ,T extends Pattern<R>>void writePatternList(T pattern,ArrayList<R> val) throws IOException {
            val.forEach(new Consumer<R>() {
                @Override
                public void accept(R r) {
                    try {
                        ln();
                        write(pattern.makePatternText(r));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        public <R ,T extends Pattern<R>>void writePatternSurrounded(T pattern,R val) throws IOException {
            write(pattern.makePatternSurrounded());
        }
    }

    private File file;
    private Writer writer;
    private Reader reader;

    public FileHelper(File file) throws IOException {
        if (file.exists()){
            this.file = file;
        }else{
            LOGGER.warn(file.getPath()+" file is not exists creating a new one.");
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.file = file;
        }
        this.writer = new Writer(this.file);
        this.reader = new Reader(this.file);
    }

    public char read() throws IOException {
        return reader.read();
    }
    public char[] read(int i) throws IOException {
        return reader.read(i);
    }
    public String readWord() throws IOException {
        return reader.readWord();
    }
    public HashMap<Integer, ArrayList<String>> map(){
        return reader.map;
    }
    public String readLine() throws IOException {
        return reader.readLine();
    }
    public ArrayList<String> readLines() throws IOException {
        return reader.Lines;
    }
    public String getLine(int i) throws IOException {
        return reader.getLine(i);
    }
    public String getFirstLine() throws IOException {
        return reader.getFirstLine();
    }
    public String getLastLine() throws IOException {
        return reader.getLastLine();
    }
    public String getWord(int wordNumber) throws IOException {
        return reader.getWord(wordNumber);
    }
    public String getWord(int line,int wordNumber) throws IOException {
        return reader.getWord(line,wordNumber);
    }


    public void append(String str) throws IOException {
        writer.app(str);
    }
    public void append(char chr) throws IOException {
        writer.app(chr);
    }
    public void appendNewLine(String str) throws IOException {
        writer.appln(str);
    }
    public void write(String str) throws IOException {
        writer.write(str);
    }
    public void write(char chr) throws IOException {
        writer.write(chr);
    }
    public void writeln(String str) throws IOException {
        writer.writeln(str);
    }
    public void newLine(){
        try {
            writer.ln();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public void analyzeAndSetupTheFile() throws IOException {
        reader.cs();
    }
    public void exitAndSave() throws IOException {
        writer.exitAndSave();
    }


    public void resetw() throws IOException {
        writer.writer = new BufferedWriter(new FileWriter(file,true));
    }
    public void resetr() throws FileNotFoundException {
        reader.reset();
    }

    public void makeBlank() throws IOException {
        resetNotAppend();
    }
    public void resetNotAppend() throws IOException {
       writer.writer = new BufferedWriter(new FileWriter(file,false));
    }



    public void writeStringPattern(BasicStringPattern pattern,String  val) throws IOException {
        writer.writePattern(pattern,val);
    }
    public void writeStringPattern(BasicStringPattern pattern,ArrayList<String> val) throws IOException {
        writer.writePatternList(pattern,val);
    }

    public <R ,T extends Pattern<R>>ArrayList<R> readPattern(T pattern) throws IOException {
        return reader.readPattern(pattern);
    }



    public void ln() throws IOException {
        writer.ln();
    }

    public boolean canRead(){
        return reader.canRead();
    }

    @Override
    public String name() {
        return "File Helper";
    }

    @Override
    public CakilganComponent superComp() {
        return CakilganCore.INSTANCE;
    }

    @Override
    public Category category() {
        return Category.MANAGMENT;
    }

    @Override
    public int id() {
        return 2;
    }

    @Override
    public String desc() {
        return "IO Handler ,can read and write patterned texts.";
    }
}
