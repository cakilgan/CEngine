package io.github.cakilgan.cresourcemanager.comp;

public class ResourceID<T>  implements Comparable<ResourceID<T>>{
    public static int getValue(boolean val){
        if (val){
            return 1;
        }
        else{
            return 0;
        }
    }
    public static boolean getValue(int val){
        return val == 1;
    }
    T id;
    public ResourceID(T id){
        this.id = id;
    }
    public T getID() {
        return this.id;
    }
    public void setID(T id) {
        this.id = id;
    }

    @Override
    public int compareTo(ResourceID<T> o) {
        return getValue(o.getID().equals(getID())) ;
    }
}
