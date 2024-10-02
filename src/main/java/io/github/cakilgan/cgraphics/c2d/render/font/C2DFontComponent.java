package io.github.cakilgan.cgraphics.c2d.render.font;

public class C2DFontComponent {
    String component = "";
    String oldComponent = "";
    public void set(String set){
        if (component.isEmpty()){
            this.oldComponent = set;
            this.component = set;
        }else{
        this.oldComponent = component;
        this.component = set;
        }
    }

    public String getComponent() {
        return component;
    }

    public String getOldComponent() {
        return oldComponent;
    }
}
