package io.github.cakilgan.clogger.format;

import io.github.cakilgan.clogger.CLogger;
import io.github.cakilgan.clogger.util.Text;
import io.github.cakilgan.cscriptengine.CakilganScriptEngine;
import io.github.cakilgan.cscriptengine.comp.Keyword;

import java.util.*;

public class CLoggerFormatter {
    private static final CLoggerFormatter DEFAULT_FORMATTER = new CLoggerFormatter();
    private static CLoggerFormatter SYSTEM_OUT_FORMATTER = new CLoggerFormatter();
    private final Map<String, ElementWithType<?>> elements = new HashMap<>();

    public Map<String, ElementWithType<?>> getElements() {
        return elements;
    }

    public void addElement(String code, ElementWithType<?> element) {
        elements.put(code, element);
    }

    public String parse(String s) {
        List<String> list = Text.convertDottedStringToList(s);
        if (list.isEmpty()) return "";

        // Update elements based on the parsed string
        list.forEach(item -> elements.forEach((key, element) -> {
            if (item.startsWith(element.getElementPattern())) {
                String value = item.substring(element.getElementPattern().length());
                if (element.getType() instanceof String) {
                    ((ElementWithType<String>) element).setType(value);
                } else if (element.getType() instanceof Boolean) {
                    ((ElementWithType<Boolean>) element).setType(Boolean.valueOf(value));
                }
            }
        }));

        // Build the formatted output string
        StringBuilder builder = new StringBuilder();
        elements.values().stream()
                .filter(e -> e.getIndex() != -1)
                .sorted(Comparator.comparingInt(ElementWithType::getIndex))
                .map(ElementWithType::format)
                .filter(Objects::nonNull)
                .forEach(builder::append);

        return builder.toString();
    }

    public String unparse(Map<String, ElementWithType<?>> elements) {
        StringBuilder builder = new StringBuilder();
        elements.forEach((key, element) -> {
            builder.append(element.getElementPattern()).append(element.getType()).append(",");
        });
        return builder.toString();
    }

    static {
        DEFAULT_FORMATTER.addElement("context",new ElementWithType<>("ctx:","empty"){

            @Override
            public String format() {
                if (getType().isEmpty()){
                    return " ";
                }
                String str = "--"+getType()+"--> ";
                return Text.colorize(str,Level.valueOf(DEFAULT_FORMATTER.elements.get("level").type.toString().toUpperCase(Locale.ROOT)).message_color);
            }

            @Override
            public int getIndex() {
                return 3;
            }
        });
        DEFAULT_FORMATTER.addElement("dateEnabled", new ElementWithType<>("d:", true));
        DEFAULT_FORMATTER.addElement("dateFormat", new ElementWithType<>("df:", CLDate.FORMAT_STR) {
            @Override
            public String format() {
                return Text.colorize(Text.surroundWithCurlyBracket(CLDate.getDate(type)), CLColor.BLUE_BOLD) + "--";
            }

            @Override
            public int getIndex() {
                return 0;
            }
        });
        DEFAULT_FORMATTER.addElement("namespaceEnabled", new ElementWithType<>("ns:", true));
        DEFAULT_FORMATTER.addElement("namespace", new ElementWithType<>("nsn:", CLogger.COLORIZED_NAMESPACE) {
            @Override
            public String format() {
                return ((ElementWithType<Boolean>) DEFAULT_FORMATTER.getElements().get("namespaceEnabled")).getType()
                        ? type + "--" : "";
            }

            @Override
            public int getIndex() {
                return 1;
            }
        });
        DEFAULT_FORMATTER.addElement("classEnabled", new ElementWithType<>("c:", true));
        DEFAULT_FORMATTER.addElement("className", new ElementWithType<>("class:", "") {
            @Override
            public String format() {
                return ((ElementWithType<Boolean>) DEFAULT_FORMATTER.getElements().get("classEnabled")).getType()
                        ? Text.colorize(Text.surroundWithRectalBracket(type), CLColor.WHITE_BOLD_BRIGHT) + "--" : "";
            }

            @Override
            public int getIndex() {
                return 2;
            }
        });
        DEFAULT_FORMATTER.addElement("level", new ElementWithType<>("l:", "empty") {
            @Override
            public String format() {
                return Text.colorize(Text.surroundWithBracket(Level.valueOf(type.toUpperCase(Locale.ROOT)).toString()),
                        Level.valueOf(type.toUpperCase(Locale.ROOT)).level_color);
            }

            @Override
            public int getIndex() {
                return 3;
            }
        });
        DEFAULT_FORMATTER.addElement("msg", new ElementWithType<>("msg:", "empty") {
            @Override
            public String getType() {
                return Text.surroundWithCurlyBracket(type);
            }
            @Override
            public String format() {
                type = type.substring(1,type.length()-1);
                return Text.colorize(Text.removeOuterCurlyBraces(type), Level.valueOf(((ElementWithType<String>) DEFAULT_FORMATTER.getElements().get("level")).getType().toUpperCase(Locale.ROOT)).message_color);
            }

            @Override
            public int getIndex() {
                return 4;
            }
        });
        SYSTEM_OUT_FORMATTER.addElement("class",new ElementWithType<>("class:",""){
            @Override
            public String format() {
                return Text.surroundWithCurlyBracket(type)+"-";
            }

            @Override
            public int getIndex() {
                return 0;
            }
        });
        SYSTEM_OUT_FORMATTER.addElement("level",new ElementWithType<>("l:",""){
            @Override
            public String format() {
                if (type.isEmpty()){
                    return "";
                }
                return Text.surroundWithRectalBracket(type)+"-";
            }

            @Override
            public int getIndex() {
                return 1;
            }
        });
        SYSTEM_OUT_FORMATTER.addElement("msg",new ElementWithType<>("msg:","empty"){
            @Override
            public String format() {
                return Text.colorize(Text.unSurroundCurlyBracket(type),CLColor.WHITE_BOLD_BRIGHT);
            }

            @Override
            public int getIndex() {
                return 2;
            }
        });
    }
    public static void setSystemOutFormatter(CLoggerFormatter formatter){
        SYSTEM_OUT_FORMATTER = formatter;
    }
    public static CLoggerFormatter getSystemOutFormatter() {
        return SYSTEM_OUT_FORMATTER;
    }

    public static CLoggerFormatter getDefaultFormatter() {
        return DEFAULT_FORMATTER;
    }
}
