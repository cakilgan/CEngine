package io.github.cakilgan.cconsole.comp.command;

import io.github.cakilgan.cconsole.comp.Console;

import java.util.List;

public abstract class Command <T>{
    String style;
    public String valueStyle;
    T value;
    String key;
    public Command(String key){
        this.key = key;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setStyle(String style) {
        this.style = style;
    }
    public void setValueStyle(String valueStyle) {
        this.valueStyle = valueStyle;
    }

    @Override
    public String toString() {
        return key;
    }

    public static Command<String> defaultCommand(Console console, Command<String> rtrn){
        rtrn.setStyle("-fx-fill: #4682b4;");
        rtrn.setValueStyle("-fx-fill: #ff8c00;");
        console.parser.commands.add(rtrn);
        return rtrn;
    }
    public abstract void action(String key, List<String> values);
}
