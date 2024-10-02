package io.github.cakilgan.clogger.format;

public class ElementWithType<T> {
    String elementPattern;
    T type;
    int index = -1;
    public void setIndex(int index) {
        this.index = index;
    }

    public ElementWithType(String elementPattern, T t){
        setType(t);
        setElementPattern(elementPattern);
    }

    public String getElementPattern() {
        return elementPattern;
    }

    public T getType() {
        return type;
    }

    public void setElementPattern(String elementPattern) {
        this.elementPattern = elementPattern;
    }

    public void setType(T type) {
        this.type = type;
    }

    public String format(){
        return "";
    }

    public int getIndex() {
        return index;
    }
}
