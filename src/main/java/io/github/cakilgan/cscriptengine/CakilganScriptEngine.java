package io.github.cakilgan.cscriptengine;

import io.github.cakilgan.clogger.CLogger;
import io.github.cakilgan.core.CakilganCore;
import io.github.cakilgan.core.io.FileHelper;
import io.github.cakilgan.cscriptengine.comp.Keyword;
import io.github.cakilgan.engine.logger.EngineContexts;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class CakilganScriptEngine {
    protected CLogger LOGGER = new CLogger(CakilganScriptEngine.class);
    public CLogger getLOGGER() {
        return LOGGER;
    }
    HashMap<String, Keyword<?>> KEYWORDS;
    public CakilganScriptEngine(){
        KEYWORDS = new HashMap<>();
        LOGGER.setDebugMode(true);
    }
    String start="{",end="}";

    public void setStart(String start) {
        this.start = start;
    }
    public void setEnd(String end) {
        this.end = end;
    }

    public void addKeyword(Keyword<?> keyword){
        addKeyword(keyword.getKeywordID(),keyword);
    }
    public void addKeyword(String key,Keyword<?> keyword){
        this.KEYWORDS.put(key,keyword);
    }
    public Keyword<?> getKeyword(String key){
        return KEYWORDS.get(key);
    }
    public boolean parse(File file){
        FileHelper helper = CakilganCore.createHelper(file);
        try {
            String value;
            helper.analyzeAndSetupTheFile();
            for (int i = 0; i < helper.map().values().size(); i++) {
                List<String> words = helper.map().get(i);
                for (int j = 0; j<words.size(); j++){
                    for (Keyword<?> keyword : KEYWORDS.values()){
                        if (words.get(j).equals(keyword.format())){
                            if (keyword.isReserveLine()){
                                words.remove(j);
                                StringBuilder builder = new StringBuilder();
                                words.forEach(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) {
                                        builder.append(s+" ");
                                    }
                                });
                                value = builder.toString();
                            }else{
                                int count = j+1;
                                value = words.get(count);
                                if (value.equals(start)){
                                    value  ="";
                                    StringBuilder builder = new StringBuilder();
                                    do {
                                       try {
                                           count++;
                                           value = words.get(count);
                                           LOGGER.debug("val on try: "+value);
                                       }catch (IndexOutOfBoundsException e){
                                           i++;
                                           words = helper.map().get(i);
                                           count = 0;
                                           value = words.get(count);
                                           LOGGER.debug("val on catch: "+value);
                                       }
                                       if (!value.equals(end)){
                                       builder.append(value+" ");
                                       }
                                    }while (!value.endsWith(end));
                                    LOGGER.debug(builder.toString());
                                    value = builder.toString();
                                }
                            }
                            if (keyword.getValue()==String.class){
                                ((Keyword<String>) keyword).action(value);
                            }
                            if (keyword.getValue()==Integer.class){
                                ((Keyword<Integer>) keyword).action(Integer.valueOf(value));
                            }
                            if (keyword.getValue()==Boolean.class){
                                ((Keyword<Boolean>) keyword).action(Boolean.valueOf(value));
                            }
                        }
                    }
                }
            }
            return true;
        }catch (IOException e){
            LOGGER.setCurrentContext(EngineContexts.EXCEPTION);
            LOGGER.excFalse(e);
            LOGGER.setCurrentContext(null);
            return false;
        }
    }
}
