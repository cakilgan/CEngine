package io.github.cakilgan.cscriptengine.comp;

public class Keyword<T> {
    String keywordID;
    boolean reserveLine = false;
    Class<T> value;

    public boolean isReserveLine() {
        return reserveLine;
    }
    public void setReserveLine(boolean reserveLine) {
        this.reserveLine = reserveLine;
    }

    public Class<T> getValue() {
        return value;
    }

    public Keyword(String keywordID, Class<T> value){
        this.keywordID = keywordID;
        this.value = value;
    }
    public void action(T value){

    }
    public String getKeywordID() {
        return keywordID;
    }

    public String format(){
        return keywordID;
    }
}
