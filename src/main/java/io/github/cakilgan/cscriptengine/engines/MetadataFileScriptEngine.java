package io.github.cakilgan.cscriptengine.engines;

import io.github.cakilgan.clogger.util.Text;
import io.github.cakilgan.cresourcemanager.data.BooleanMetaData;
import io.github.cakilgan.cresourcemanager.data.IntegerMetaData;
import io.github.cakilgan.cresourcemanager.data.ResourceMetaData;
import io.github.cakilgan.cresourcemanager.data.StringMetaData;
import io.github.cakilgan.cresourcemanager.resources.FileResource;
import io.github.cakilgan.cscriptengine.CakilganScriptEngine;
import io.github.cakilgan.cscriptengine.comp.Keyword;
import io.github.cakilgan.engine.CEngine;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetadataFileScriptEngine extends CakilganScriptEngine {
    public Map<String, ResourceMetaData<?>> parse(FileResource resource){
        Map<String,ResourceMetaData<?>> rtrn = new HashMap<>();
        Keyword<String> String = new Keyword<>("String", java.lang.String.class){
            @Override
            public void action(java.lang.String value) {
                List<String> datas = Text.convertDottedStringToList(value);
                for (java.lang.String data : datas) {
                    java.lang.String name = data.substring(0,data.lastIndexOf(":"));
                    java.lang.String val = data.substring(data.lastIndexOf("[")+1,data.lastIndexOf("]")-1);
                    rtrn.put(name,new StringMetaData(val,name));
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
                    rtrn.put(name,new IntegerMetaData(java.lang.Integer.valueOf(val),name));
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
                    rtrn.put(name,new BooleanMetaData(java.lang.Boolean.valueOf(val),name));
                }
                super.action(value);
            }
        };
        this.addKeyword(String);
        this.addKeyword(Integer);
        this.addKeyword(Boolean);
        this.parse(resource.type.getContext());
        return rtrn;
    }
}
