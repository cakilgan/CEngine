package io.github.cakilgan.cconsole.comp;

import io.github.cakilgan.cconsole.CakilganConsole;
import io.github.cakilgan.cconsole.comp.command.CommandParser;
import io.github.cakilgan.clogger.CLogger;
import io.github.cakilgan.clogger.comp.CLoggerPrintStream;
import io.github.cakilgan.clogger.system.CLoggerSystem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.InlineCssTextArea;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Console {
    static CLogger LOGGER = CLoggerSystem.logger(Console.class);
    public CommandParser parser;
    public final String NAMESPACE = "BoltConsole[]-> ";
    String reserved;
    public CakilganConsole console;
    public InlineCssTextArea area;
    public Console(CakilganConsole console,InlineCssTextArea area){
        this.area = area;
        this.console = console;
        this.parser = new CommandParser(this);
    }
    public void setParser(CommandParser parser) {
        this.parser = parser;
    }
    public String getReserved() {
        return reserved;
    }
    public void println(String var){
        LOGGER.info("console print: "+var);
        console.textArea.appendText(var+"\n");
        oldLine = line;
        line++;
    }
    public void errorWithException(Exception e){
        println("an error occurred executing this command exception is -*> "+e.getMessage());
        console.styledParagraphs.add(oldLine);
    }
    public void print(String var){
        console.textArea.appendText(var);
    }
    public void newLine(){
        console.textArea.appendText(NAMESPACE);
    }
    public void simulateCommand(String command){
        print(command+"\n");
        ref();
        commandEvent();
    }
    public int line = 1;
    public int oldLine = 1;
    public List<List<String>> history = new ArrayList<>();
    private void oldcommand(){
        oldLine = line;
        line++;
        String commandLine = area.getText(oldLine);
        List<String> keywords = new ArrayList<>();
        history.add(keywords);
        Scanner scanner = new Scanner(commandLine);
        do {
            String scan = scanner.next();
            if (scan==null||scan.isEmpty()){
                continue;
            }else{
                keywords.add(scan);
            }
        }while (scanner.hasNext());
        List<String> values = new ArrayList<>();
        String key = "";
        if (keywords.size()>2){
            key = keywords.get(1);
            for (int i = 2; i < keywords.size(); i++) {
                values.add(keywords.get(i));
            }
        }else if (keywords.size()==1){
            key = "";
        }
        else{
            key = keywords.get(1);
        }
        parser.parse(key,values);
    }
    List<String> keywords= new ArrayList<>();
    public List<String> values=new ArrayList<>();
    public String key;
    public void ref(){
        String commandLine = area.getText(line);
        keywords = new ArrayList<>();
        Scanner scanner = new Scanner(commandLine);
        do {
            if (commandLine.isEmpty()){
                continue;
            }
            String scan = scanner.next();
            if (scan==null||scan.isEmpty()){
                continue;
            }else{
                keywords.add(scan);
            }
        }while (scanner.hasNext());
        values = new ArrayList<>();
        key = "";
        if (keywords.size()>2){
            key = keywords.get(1);
            for (int i = 2; i < keywords.size(); i++) {
                values.add(keywords.get(i));
            }
        }else if (keywords.size()==1){
            key = keywords.get(0);
        } else{
            key = keywords.get(1);
        }
    }
    public int lastEditableIndex = 0;
    public void setLastEditableIndex(int lastEditableIndex) {
        this.lastEditableIndex = lastEditableIndex;
    }

    public String oldText;
    private void newcommand(){
        oldText = this.area.getText();
        newLine();
        this.reserved = this.area.getText();
        lastEditableIndex = this.reserved.length()+1; // Komut girildiğinde belirle
        this.area.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (area.getCaretPosition() < lastEditableIndex&&!event.getCode().equals(KeyCode.ENTER)) {
                event.consume(); // Düzenleme girişimlerini durdur
            }
        });
    }
    public void commandEvent(){
        oldcommand();
        newcommand();
    }
    int historyLoc = 0;
    public void keyEvent(KeyEvent event){
        ref();
        if (event.getCode().equals(KeyCode.ENTER)){
            commandEvent();
        }
    }
    public void refresh(){
        console.colorizeWords();
    }


}
