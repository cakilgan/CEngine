package io.github.cakilgan.cconsole;

import io.github.cakilgan.cconsole.comp.Console;
import io.github.cakilgan.cconsole.comp.command.Command;
import io.github.cakilgan.clogger.CLogger;
import io.github.cakilgan.clogger.system.CLoggerSystem;
import io.github.cakilgan.core.CakilganCore;
import io.github.cakilgan.core.io.FileHelper;
import io.github.cakilgan.cresourcemanager.resources.DirectoryResource;
import io.github.cakilgan.cresourcemanager.resources.FileResource;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.vc.VoiceSender;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.fxmisc.richtext.InlineCssTextArea;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CakilganConsole extends Application {
    public static final CLogger LOGGER = CLoggerSystem.logger(CakilganConsole.class);
    public static CakilganConsole INSTANCE;
    public static final String TITLE= "BoltConsole";
    public static final String WELCOME_TEXT = "Welcome To "+TITLE+"! - Made By Cakilgan";
    public static final String STYLE_CONSOLE = "/"+"console_style.css";
    public static final String DEFAULT_TEXT_COLOR = "white";
    Console console;
    List<String> words = new ArrayList<>();
    public InlineCssTextArea textArea;
    StackPane root;
    public Scene scene;

    int width=800,height=500;
    static String printString = "";
    public static void setPrintString(String printString) {
        CakilganConsole.printString = printString;
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(CakilganConsole.TITLE);

        textArea = new InlineCssTextArea();
        console = new Console(this,textArea);
        textArea.setFocusTraversable(true);
        textArea.textProperty().addListener(this::handleText);
        textArea.setOnKeyPressed(this.console::keyEvent);
        this.console.area.appendText(WELCOME_TEXT+" \n");
        this.console.newLine();
        this.console.lastEditableIndex = this.console.area.getText().length()+1;

        this.console.area.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (console.area.getCaretPosition() < console.lastEditableIndex&&!event.getCode().equals(KeyCode.ENTER)) {
                event.consume(); // Düzenleme girişimlerini durdur
            }
        });

        Command<String> clearCommand = new Command<String>("clear") {

            @Override
            public void action(String key, List<String> values) {
                boolean log= false;
                boolean clearHistory= false;
                boolean clearTextArea = true;
                if (!values.isEmpty()){
                    if (values.contains("-log")){
                        log = true;
                    }
                    if (values.contains("-all")){
                        clearHistory = true;
                    }else if (values.contains("-consoleHistory")){
                        clearHistory = true;
                        clearTextArea = false;
                    }
                    runClear(log,clearHistory,clearTextArea);
                }else{
                    runClear(log,clearHistory,clearTextArea);
                }
            }
            public void runClear(boolean log,boolean clearHistory,boolean clearTextArea){
                if (log){
                    System.out.println("cleared text: \n"+console.oldText);
                }
                if (clearHistory){
                    console.history = new ArrayList<>();
                }
                if (clearTextArea){
                    console.line = 0;
                    console.oldLine = 0;
                    console.console.styledParagraphs = new ArrayList<>();
                    console.area.clear();
                    console.setLastEditableIndex(console.NAMESPACE.length()+1);
                }
            }

            @Override
            public String toString() {
                return "clear -<log,all,consoleHistory>";
            }
        };
        Command<String> printCommand = new Command<String>("print") {
            @Override
            public void action(String key, List<String> values) {
                StringBuilder builder = new StringBuilder();
                values.forEach(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        builder.append(s).append(" ");
                    }
                });
                String s = builder.substring(0,builder.length()-1);
                console.println(s);
            }
            @Override
            public String toString() {
                return
                        "print <String ...>";
            }
        };
        Command<String> helpCommand = new Command<String>("help") {
            @Override
            public void action(String key, List<String> values) {
                console.parser.commands.forEach(new Consumer<Command<?>>() {
                    @Override
                    public void accept(Command<?> command) {
                        console.println(command.toString());
                    }
                });
            }
        };
        Command<String> exitCommand = new Command<String>("exit") {
            @Override
            public void action(String key, List<String> values) {
                int exitCode = 0;
                if (!values.isEmpty()){
                    try {
                        exitCode = Integer.parseInt(values.get(0));
                    }catch (Exception e){
                        console.errorWithException(e);
                        return;
                    }
                }
                //console.println("exiting BoltConsole with code: "+exitCode);
                System.exit(exitCode);
            }
        };
        Command<String> zoomCommand = new Command<String>("zoom") {
            @Override
            public void action(String key, List<String> values) {
                try {
                    if (!values.isEmpty()){
                        if (values.get(0).equals("-in")){
                            try {
                                fsize+=Integer.parseInt(values.get(1));
                                textArea.setStyle("-fx-font-size: " + fsize + "px;");
                            }catch (IndexOutOfBoundsException e){
                                fsize+=2;
                                textArea.setStyle("-fx-font-size: " + fsize + "px;");
                            }
                        }
                        else if (values.get(0).equals("-out")){
                            try {
                                fsize-=Integer.parseInt(values.get(1));
                                textArea.setStyle("-fx-font-size: " + fsize + "px;");
                            }catch (IndexOutOfBoundsException e){
                                fsize-=2;
                                textArea.setStyle("-fx-font-size: " + fsize + "px;");
                            }
                        }else{
                            fsize-=Integer.parseInt(values.get(0));
                            textArea.setStyle("-fx-font-size: " + fsize + "px;");
                        }
                    }else{
                        fsize+=2;
                        textArea.setStyle("-fx-font-size: " + fsize + "px;");
                    }
                }catch (Exception e){
                    console.errorWithException(e);
                }
            }

            @Override
            public String toString() {
                return "zoom -<in(Integer),out(Integer)>,(null)";
            }
        };
        Command<String> historyCommand = new Command<String>("history") {
            @Override
            public void action(String key, List<String> values) {
                try {
                    int val = Integer.parseInt(values.get(0));
                    console.println(console.history.get(val).toString());
                }catch (Exception e){
                    console.errorWithException(e);
                }
            }
        };
        Command<String> sumCommand = new Command<String>("sum") {
            boolean first = false;
            @Override
            public void action(String key, List<String> values) {
                float rtrn = 0;
                first = false;
                for (String val : values){
                    if (!first){
                        rtrn = Float.parseFloat(val);
                        first = true;
                    }else{
                        rtrn+=Float.parseFloat(val);
                    }
                }
                console.println("value: "+rtrn);
            }

            @Override
            public String toString() {
                return
                        "sum <Float ...>";
            }
        };
        Command<String> subCommand = new Command<String>("sub") {
            boolean first = false;
            @Override
            public void action(String key, List<String> values) {
                float rtrn = 0;
                first = false;
                for (String val : values){
                    if (!first){
                        rtrn = Float.parseFloat(val);
                        first = true;
                    }else{
                        rtrn-=Float.parseFloat(val);
                    }
                }
                console.println("value: "+rtrn);
            }

            @Override
            public String toString() {
                return "sub <Float ...>";
            }
        };
        Command<String> mulCommand = new Command<String>("mul") {
            boolean first = false;
            @Override
            public void action(String key, List<String> values) {
                float rtrn = 0;
                first = false;
                for (String val : values){
                    if (!first){
                        rtrn = Float.parseFloat(val);
                        first = true;
                    }else{
                        rtrn*=Float.parseFloat(val);
                    }
                }
                console.println("value: "+rtrn);
            }

            @Override
            public String toString() {
                return "mul <Float ...>";
            }
        };
        Command<String> divCommand = new Command<String>("div") {
            boolean first = false;
            @Override
            public void action(String key, List<String> values) {
                float rtrn = 0;
                first = false;
                for (String val : values){
                    if (!first){
                        rtrn = Float.parseFloat(val);
                        first = true;
                    }else{
                        rtrn/=Float.parseFloat(val);
                    }
                }
                console.println("value: "+rtrn);
            }

            @Override
            public String toString() {
                return "div <Float ...>";
            }
        };
        Command<String> colorCommand = new Command<String>("color") {
            @Override
            public void action(String key, List<String> values) {
                console.console.consoleColorCode = values.get(0);
            }

            @Override
            public String toString() {
                return "color <String>";
            }
        };
        Command<String> randomCommand = new Command<String>("random") {
            @Override
            public void action(String key, List<String> values) {
                int min=0,max=1000;
                if (!values.isEmpty()){
                    try {
                        min = Integer.parseInt(values.get(0));
                        max = Integer.parseInt(values.get(1));
                    }catch (Exception e){
                        console.errorWithException(e);
                        return;
                    }
                }
                console.println(""+new Random().nextInt(min,max));
            }

            @Override
            public String toString() {
                return "random <Integer min,Integer max>,<null(default min = 0, max = 1000)>";
            }
        };
        Command<String> reverseCommand = new Command<String>("reverse") {
            @Override
            public void action(String key, List<String> values) {
                if (!values.isEmpty()){
                    console.println(new StringBuilder(CakilganConsole.makeString(values)).reverse().toString());
                }
            }

            @Override
            public String toString() {
                return "reverse <String ...>";
            }
        };
        Command<String> timeCommand = new Command<String>("time") {
            @Override
            public void action(String key, List<String> values) {
                Date date = new Date();
                String format = "HH:mm:ss";
                if (!values.isEmpty()){
                    format = values.get(0);
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                console.println("Current time: " + dateFormat.format(date));
            }

            @Override
            public String toString() {
                return "time <String>";
            }
        };
        Command<String> uppercaseCommand = new Command<String>("uppercase") {
            @Override
            public void action(String key, List<String> values) {
                if (!values.isEmpty()) {
                    String text = String.join(" ", values);
                    console.println(text.toUpperCase());
                }
            }

            @Override
            public String toString() {
                return "uppercase <String ...>";
            }
        };
        Command<String> lowercaseCommand = new Command<String>("lowercase") {
            @Override
            public void action(String key, List<String> values) {
                if (!values.isEmpty()) {

                    String text = String.join(" ", values);
                    console.println(text.toLowerCase());
                }
            }

            @Override
            public String toString() {
                return "lowercase <String ...>";
            }
        };
        Command<String> lenCommand = new Command<String>("len") {
            @Override
            public void action(String key, List<String> values) {
                if (!values.isEmpty()) {
                    String text = CakilganConsole.makeString(values);
                    console.println("length of string is: "+text.length());
                }
            }

            @Override
            public String toString() {
                return "len <String ...>";
            }
        };
        Command<String> countCommand = new Command<String>("count") {
            @Override
            public void action(String key, List<String> values) {
                if (!(values.size() <2)){
                    String text = String.join(" ", values.subList(1, values.size()));
                    String target = values.get(0);
                    int count = text.split(target, -1).length - 1;

                    console.println(count+"");
                }
            }

            @Override
            public String toString() {
                return "count <String target,String text ...>";
            }
        };
        Command<String> mkdirCommand = new Command<String>("mkdir") {
            @Override
            public void action(String key, List<String> values) {
                if (!values.isEmpty()){
                    for (String value : values) {
                        DirectoryResource resource = new DirectoryResource(value);
                        resource.type.create();
                    }
                }
            }
            @Override
            public String toString() {
                return "mkdir <String ...>";
            }
        };
        Command<String> mkfileCommand = new Command<String>("mkfile") {
            @Override
            public void action(String key, List<String> values) {
                if (!values.isEmpty()){
                    for (String value : values) {
                        FileResource resource = new FileResource(value);
                        resource.type.create();
                    }
                }
            }
            @Override
            public String toString() {
                return "mkfile <String ...>";
            }
        };
        Command<String> fileCommand = new Command<String>("file") {
            @Override
            public String toString() {
                return "file String path,-<write String ...,delete,create,tree,read Integer line,read>";
            }
            @Override
            public void action(String key, List<String> values) {
                String path = values.get(0);
                File file = new File(path);
               FileHelper helper = null;
                if (values.get(1).equals("-write")){
                    helper = CakilganCore.createHelper(file);
                    try {
                        helper.resetNotAppend();
                        List<String> rtrn = new ArrayList<>();
                        for (int i = 0; i < values.size(); i++) {
                            if (i==0||i==1){
                                continue;
                            }
                            rtrn.add(values.get(i));
                        }
                        helper.write(CakilganConsole.makeString(rtrn));
                        console.println("writing text to file -> "+path);
                        helper.exitAndSave();
                    } catch (IOException e) {
                        console.println("something went wrong -> "+e.getMessage());
                    }
                }
                if (values.get(1).equals("-delete")){
                    console.println("deleting -> "+path+" "+file.delete());
                }
                if (values.get(1).equals("-tree")){
                    DirectoryResource resource = new DirectoryResource(path);
                    for (String s : resource.directoryTreeInSingleLog().lines().toList()){
                        console.println(s);
                    }
                }
                if (values.get(1).equals("-create")){
                    try {
                        console.println("creating -> "+path+" "+file.createNewFile());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (values.get(1).equals("-read")){
                    try {
                        try {
                            int linecode = Integer.parseInt(values.get(2));
                            helper = new FileHelper(new File(path));
                            helper.analyzeAndSetupTheFile();
                            console.println(helper.readLines().get(linecode));
                        }catch (IndexOutOfBoundsException e){
                            helper = new FileHelper(new File(path));
                            helper.analyzeAndSetupTheFile();
                            for (String string : helper.readLines()) {
                                console.println(string);
                        }
                        }
                    } catch (IOException e) {
                        console.errorWithException(e);
                    }
                }
            }
        };
        Command<String> treeCommand = new Command<String>("tree") {
            @Override
            public String toString() {
                return "tree <String>";
            }

            @Override
            public void action(String key, List<String> values) {
                String dirPath = values.get(0);
                DirectoryResource resource = new DirectoryResource(dirPath);
                for (String s : resource.directoryTreeInSingleLog().lines().toList()){
                    console.println(s);
                }
            }
        };
        Command<String> searchCommand = new Command<String>("search") {
            @Override
            public void action(String key, List<String> values) {
                if (values.size() >= 2) {
                    String keyword = values.get(0);
                    String text = CakilganConsole.makeString(values.subList(1, values.size()));
                    int index = text.indexOf(keyword);
                    if (index != -1) {
                        console.println("Found at index: " + index);
                    } else {
                        console.println("Not found");
                    }
                }
            }

            @Override
            public String toString() {
                return "search <String keyword, String text ...>";
            }
        };
        Command<String> replaceCommand = new Command<String>("replace") {
            @Override
            public void action(String key, List<String> values) {
                if (values.size() >= 2) {
                    String target = values.get(0);
                    String replacement = values.get(1);
                    String text = CakilganConsole.makeString(values.subList(2, values.size()));
                    console.println(text.replace(target, replacement));
                }
            }

            @Override
            public String toString() {
                return "replace <String target, String replacement, String text ...>";
            }
        };
        Command<String> calcCommand = new Command<String>("calc") {
            @Override
            public void action(String key, List<String> values) {
                try {
                    if (values.size() == 3) {
                        float num1 = Float.parseFloat(values.get(0));
                        float num2 = Float.parseFloat(values.get(2));
                        String operator = values.get(1);
                        float result = switch (operator) {
                            case "+" -> num1 + num2;
                            case "-" -> num1 - num2;
                            case "*" -> num1 * num2;
                            case "/" -> num1 / num2;
                            default -> throw new IllegalArgumentException("Invalid operator");
                        };
                        console.println("Result: " + result);
                    }
                } catch (Exception e) {
                    console.errorWithException(e);
                }
            }

            @Override
            public String toString() {
                return "calc <Float num1, String operator, Float num2>";
            }
        };
        Command<String> moveCommand = new Command<String>("move") {
            @Override
            public void action(String key, List<String> values) {
                String file = values.get(0);
                String dir = values.get(1);
                FileResource fileResource = new FileResource(file);
                DirectoryResource directoryResource = new DirectoryResource(dir);
                String path ;
                if (directoryResource.id.getID().contains("/")){
                    path = fileResource.id.getID().substring(fileResource.id.getID().lastIndexOf("/"));
                }else{
                    path = fileResource.id.getID();
                }
                directoryResource.createFile(path);
                fileResource.type.delete();
            }
            @Override
            public String toString() {
                return "move <sourcePath> <destPath>";
            }
        };
        Command<String> connect = new Command<String>("connect") {
            @Override
            public void action(String key, List<String> values) {
                try {
                    int port = Integer.parseInt(values.get(0));
                    String ip = values.get(1);

                    new Thread(new VoiceSender(ip, port)).start();


                    LOGGER.info("trying for port: "+port+" ip: "+ip);
                }catch (Exception e){
                    console.simulateCommand("print error in connect method please try again");
                    LOGGER.excFalse(e,"error in console server thread: ");
                }
            }
        };
        Command<String> changeScene = new Command<String>("setScene") {
            @Override
            public void action(String key, List<String> values) {
            }
        };




        Command.defaultCommand(console,clearCommand);
        Command.defaultCommand(console,printCommand);
        Command.defaultCommand(console,helpCommand);
        Command.defaultCommand(console,exitCommand);
        Command.defaultCommand(console,zoomCommand);
        Command.defaultCommand(console,historyCommand);
        Command.defaultCommand(console,sumCommand);
        Command.defaultCommand(console,subCommand);
        Command.defaultCommand(console,mulCommand);
        Command.defaultCommand(console,divCommand);
        Command.defaultCommand(console,randomCommand);
        Command.defaultCommand(console,reverseCommand);
        Command.defaultCommand(console,timeCommand);
        Command.defaultCommand(console,lowercaseCommand);
        Command.defaultCommand(console,uppercaseCommand);
        Command.defaultCommand(console,lenCommand);
        Command.defaultCommand(console,countCommand);
        Command.defaultCommand(console,colorCommand);
        Command.defaultCommand(console,mkdirCommand);
        Command.defaultCommand(console,mkfileCommand);
        Command.defaultCommand(console,fileCommand);
        Command.defaultCommand(console,treeCommand);
        Command.defaultCommand(console,searchCommand);
        Command.defaultCommand(console,replaceCommand);
        Command.defaultCommand(console,calcCommand);
        Command.defaultCommand(console,moveCommand);

        Command.defaultCommand(console,connect);

        Command.defaultCommand(console,changeScene);


        this.console.parser.init();
        root = new StackPane(textArea);

        console.refresh();

        scene = new Scene(root,width,height);
        scene.addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (!printString.isEmpty()){
                    console.simulateCommand("print "+printString);
                    printString = "";
                }
            }
        });
        scene.getStylesheets().add(getClass().getResource(STYLE_CONSOLE).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
        makeListAndAddToParser("String Float Integer Boolean");
    }
    int fsize = 22;
    String oldText;
    String newText;
    private void handleText(ObservableValue<? extends String> observableValue, String oldText, String newText){
        this.oldText = oldText;
        this.newText = newText;
        _handleText();
        console.refresh();
    }
    private void _handleText(){

        words = new ArrayList<>();
        if (textArea.getText().isEmpty()){
            return;
        }
        Scanner scanner = new Scanner(textArea.getText());
        do {
            String rt = scanner.next();
            if (rt==null){
                continue;
            }else{
                words.add(rt);
            }
        }while (scanner.hasNext());
    }

    public static String makeString(List<String> strings){
        StringBuilder builder = new StringBuilder();
        for (String string : strings) {
            builder.append(string+" ");
        }
        return builder.substring(0,builder.length()-1);
    }
    public String consoleColorCode = "white";
    public List<Integer> styledParagraphs = new ArrayList<>();
    public Map<String,String> wordColors = new HashMap<>();
    public void colorizeWords() {
        String text = textArea.getText();

        textArea.setStyle(0, text.length(), "-fx-fill: "+consoleColorCode+";");

        for (Map.Entry<String, String> entry : wordColors.entrySet()) {
            String word = entry.getKey();
            String style = entry.getValue();

            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(word) + "\\b");
            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                textArea.setStyle(start, end, style);
            }
        }
        for (int i = 0; i < styledParagraphs.size(); i++) {
            textArea.setStyle(styledParagraphs.get(i),"-fx-fill: red;");
        }
        for (String value :console.values){
            String word = value;
            Command<?> command = console.parser.getCommand(console.key);
            if (command != null){
                String style = command.valueStyle;
                Pattern pattern = Pattern.compile("[-,()]*" + Pattern.quote(word) + "[-,()]*");
                Matcher matcher = pattern.matcher(text);

                while (matcher.find()) {
                    int start = matcher.start();
                    int end = matcher.end();
                    textArea.setStyle(start, end, style);
                }
            }
        }
    }

    public void makeListAndAddToParser(String keys){
        String[] array =  keys.split(" ");
        for (int i = 0; i < array.length; i++) {
            this.wordColors.put(array[i],"-fx-fill: red;");
        }
    }

    public void start(){
        launch();
    }

}
