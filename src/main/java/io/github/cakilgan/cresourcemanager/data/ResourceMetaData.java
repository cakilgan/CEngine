package io.github.cakilgan.cresourcemanager.data;

public class ResourceMetaData<T>{
    public String dataName;
    T data;
    public ResourceMetaData(T data,String dataName){
        this.data = data;
        this.dataName = dataName;
    }
    public ResourceMetaData(T data){
        this.data = data;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    public String format(T data){
        return null;
    }
    @Override
    public String toString() {
        return "[meta-data] "+dataName+" - "+format(getData());
    }
}
