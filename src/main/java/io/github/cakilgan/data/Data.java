package io.github.cakilgan.data;

public abstract class Data<M,T> {
    DataParent parent = null;
    public void setParent(DataParent parent) {
        this.parent = parent;
    }
    public DataParent getParent() {
        return parent;
    }
    public Data(M ID,T normal){
        this.ID = ID;
        data = normal;
    }
    protected M ID;
    protected T data;
    public abstract T get();
    public abstract void set(T data);
    public M getID() {
        return ID;
    }
}
