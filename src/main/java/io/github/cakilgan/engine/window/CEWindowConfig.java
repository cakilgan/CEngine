package io.github.cakilgan.engine.window;

import java.util.ArrayList;

public class CEWindowConfig {
    public static class CEWindowHint{
        public int hint,value;

        public CEWindowHint(int hint, int value) {
            this.hint = hint;
            this.value = value;
        }
    }
    public int w,h;
    public String title;
    public ArrayList<CEWindowHint>hints;
    public CEWindowConfig(int w, int h, String title) {
        this.w = w;
        this.h = h;
        this.title = title;
       this.hints = new ArrayList<>();
    }
    public CEWindowConfig addHint(CEWindowHint hint){
        this.hints.add(hint);
        return this;
    }
}
