package io.github.cakilgan.data.defaults;

import io.github.cakilgan.clogger.util.Text;
import io.github.cakilgan.core.io.FileHelper;
import io.github.cakilgan.cresourcemanager.comp.BooleanConsumer;
import io.github.cakilgan.cresourcemanager.data.BooleanMetaData;
import io.github.cakilgan.cresourcemanager.data.IntegerMetaData;
import io.github.cakilgan.cresourcemanager.data.ResourceMetaData;
import io.github.cakilgan.cresourcemanager.data.StringMetaData;
import io.github.cakilgan.cresourcemanager.resources.FileResource;
import io.github.cakilgan.cscriptengine.comp.Keyword;
import io.github.cakilgan.cscriptengine.engines.DataFileScriptEngine;
import io.github.cakilgan.data.Data;
import io.github.cakilgan.data.DataParent;
import io.github.cakilgan.data.comp.BooleanData;
import io.github.cakilgan.data.comp.IntegerData;
import io.github.cakilgan.data.comp.StringData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class CEDataParent extends DataParent<FileHelper> {
    DataFileScriptEngine engine = new DataFileScriptEngine();
    @Override
    public void serialize(FileHelper obj) {
        try {
            obj.resetNotAppend();
            List<Class<?>> classes = new ArrayList<>();
            HashMap<Class<?>,List<String>> classStringHashMap = new HashMap<>();
            for (Data<?,?> value : datas.values()) {
                if (!classes.contains(value.get().getClass())){
                    classes.add(value.get().getClass());
                }
            }
            for (Class<?> aClass : classes) {
                classStringHashMap.put(aClass,new ArrayList<>());
            }
            for (Data<?,?> value : datas.values()) {
                classStringHashMap.get(value.get().getClass()).add(value.getID()+": [ "+value.get()+" ]");
            }
            classStringHashMap.forEach(new BiConsumer<Class<?>, List<String>>() {
                @Override
                public void accept(Class<?> aClass, List<String> strings) {
                    try {
                        obj.writeln(aClass.getSimpleName()+ " {");
                        for (String string : strings) {
                            obj.writeln(string+",");
                        }
                        obj.writeln("}");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            obj.exitAndSave();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    * Integer {
    * posX {500},
    * posY {500},
    * }
    *
    *
    *
    *
    *
    * */
    @Override
    public void deserialize(FileHelper obj) {
        Map<String,Data<?,?>> rtrn = new HashMap<>();
        Keyword<String> String = new Keyword<>("String", java.lang.String.class){
            @Override
            public void action(java.lang.String value) {
                List<String> datas = Text.convertDottedStringToList(value);
                for (java.lang.String data : datas) {
                    java.lang.String name = data.substring(0,data.lastIndexOf(":"));
                    java.lang.String val = data.substring(data.lastIndexOf("[")+1,data.lastIndexOf("]")-1);
                    rtrn.put(name,new StringData(name,val));
                }
                super.action(value);
            }
        };
        Keyword<String> Integer = new Keyword<>("Integer", java.lang.String.class){
            @Override
            public void action(java.lang.String value) {
                List<String> datas = Text.convertDottedStringToList(value);
                for (java.lang.String data : datas) {
                    java.lang.String name = data.substring(0,data.lastIndexOf(":"));
                    java.lang.String val = data.substring(data.lastIndexOf("[")+2,data.lastIndexOf("]")-1);
                    rtrn.put(name,new IntegerData(name,java.lang.Integer.valueOf(val)));
                }
                super.action(value);
            }
        };Keyword<String> Boolean = new Keyword<>("Boolean", java.lang.String.class){
            @Override
            public void action(java.lang.String value) {
                List<String> datas = Text.convertDottedStringToList(value);
                for (java.lang.String data : datas) {
                    java.lang.String name = data.substring(0,data.lastIndexOf(":"));
                    java.lang.String val = data.substring(data.lastIndexOf("[")+1,data.lastIndexOf("]")-1);
                    rtrn.put(name,new BooleanData(name,java.lang.Boolean.valueOf(val)));
                }
                super.action(value);
            }
        };
        engine.addKeyword(String);
        engine.addKeyword(Integer);
        engine.addKeyword(Boolean);
        engine.parse(obj);
        rtrn.forEach(new BiConsumer<java.lang.String, Data<?, ?>>() {
            @Override
            public void accept(java.lang.String s, Data<?, ?> data) {
                datas.put(s,data);
            }
        });
    }
}
