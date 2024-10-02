package io.github.cakilgan.core.serialization;

import io.github.cakilgan.core.util.Text;


public class CakilganComponent {
    public String name(){
        return null;
    }
    public int id(){
        return 0;
    }
    public String desc(){
        return null;
    }
    public CakilganComponent superComp(){
        return null;
    }
    public Category category(){
        return null;
    }

    @Override
    public String toString() {
        return superComp().name()+";"+category().toString()+":"+name()+ Text.surroundWithRectalBracket(String.valueOf(id())) +"\n" +
                desc();
    }
}
