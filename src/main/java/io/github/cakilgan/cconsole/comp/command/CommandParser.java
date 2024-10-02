package io.github.cakilgan.cconsole.comp.command;

import io.github.cakilgan.cconsole.comp.Console;

import java.util.ArrayList;
import java.util.List;

public class CommandParser {
    public Console console;
    public List<Command<?>> commands;
    public Command<?> getCommand(String key){
        Command<?> rtrn = null;
        for (Command<?> command : commands){
            if (command.key.equals(key)){
                rtrn = command;
            }
        }
        return rtrn;
    }
    public CommandParser(Console console){
        commands = new ArrayList<>();
        this.console = console;
    }
    boolean hasInit = false;
    public void init(){
        if (!hasInit){
            commands.forEach(command -> console.console.wordColors.put(command.key,command.style));
            hasInit = true;
        }
    }
    public void parse(String finalKey,List<String> values){
        boolean found = false;
        for (Command<?>command : commands){
            if (command.key.equals(finalKey)){
                found = true;
                command.action(finalKey,values);
            }
        }
        if (!found) {
            console.println("cannot understand the keyword _> "+finalKey+" with values "+values.toString());
            console.console.styledParagraphs.add(console.oldLine);
            sp++;
        }
    }
    int sp = 0;
}
