package io.github.cakilgan.core.io.automation;

public class Pattern<T extends Object> {
    String pattern_text,pattern_suffix;
    public Pattern(String pattern_text,String pattern_suffix){
        this.pattern_text = pattern_text;
        this.pattern_suffix = pattern_suffix;
    }
    public String makePatternText(T val){
        return null;
    }
    public String getPatternFirstText(){
        return null;
    }
    public String makePatternSurrounded(){
        return null;
    }
}
