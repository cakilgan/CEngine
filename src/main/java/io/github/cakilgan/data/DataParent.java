package io.github.cakilgan.data;

import java.util.HashMap;

public abstract class DataParent<Serializer> {
    protected HashMap<String,Data<?,?>> datas = new HashMap<>();
    public void addData(String dataCode,Data<?,?> data){
        datas.put(dataCode,data);
    }
    public Data<?,?> getData(String dataCode){
        return datas.get(dataCode);
    }
    public abstract void serialize(Serializer serializer);
    public abstract void deserialize(Serializer serializer);
}
