package io.github.cakilgan.core.io.automation;

public class BasicStringPattern extends Pattern<String>{

    public BasicStringPattern(String pattern_text, String pattern_suffix) {
        super(pattern_text, pattern_suffix);
    }

    @Override
    public String makePatternText(String val) {
        return pattern_text+pattern_suffix+" "+val;
    }

    @Override
    public String getPatternFirstText() {
        return pattern_text+pattern_suffix;
    }

    @Override
    public String makePatternSurrounded() {
        return pattern_text+pattern_suffix+"{}";
    }
}
