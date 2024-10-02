package io.github.cakilgan.cresourcemanager;


import io.github.cakilgan.clogger.CLogger;
import io.github.cakilgan.clogger.system.CLoggerSystem;
import io.github.cakilgan.cresourcemanager.comp.ResourceID;
import io.github.cakilgan.cresourcemanager.comp.ResourceType;
import io.github.cakilgan.cresourcemanager.data.BooleanMetaData;
import io.github.cakilgan.cresourcemanager.data.IntegerMetaData;
import io.github.cakilgan.cresourcemanager.data.ResourceMetaData;
import io.github.cakilgan.cresourcemanager.data.StringMetaData;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class Resource<T,K,R> {
    public static CLogger LOGGER  = CLoggerSystem.logger(Resource.class);
    public Map<String, ResourceMetaData<?>> data;
    public ResourceID<T> id;
    public ResourceType<T,K,R> type;
    public Resource(ResourceID<T> id,ResourceType<T,K,R> type){
        this.data = new HashMap<>();
        this.id=id;
        this.type=type;
        this.type.setId(this.id);
    }
    public String formatAllMetaData(){
        StringBuilder builder = new StringBuilder();
        data.forEach(new BiConsumer<String, ResourceMetaData<?>>() {
            @Override
            public void accept(String s, ResourceMetaData<?> data) {
                builder.append(data.toString()+"\n");
            }
        });
        return builder.toString();
    }
    public abstract void saveMetaData();
    public abstract void loadMetaData();
    public void addBooleanData(String name,boolean data){
        addData(name,new BooleanMetaData(data,name));
    }
    public void addStringData(String name,String data){
        addData(name,new StringMetaData(data,name));
    }
    public void addIntData(String name,int data){
        addData(name,new IntegerMetaData(data,name));
    }
    public void addData(ResourceMetaData<?> data){
        addData(data.dataName,data);
    }
    public void addData(String name,ResourceMetaData<?> data){
        this.data.put(name,data);
    }
    public StringMetaData getStringData(String name){
        return (StringMetaData) getData(name,String.class);
    }
    public IntegerMetaData getIntData(String name){
        return (IntegerMetaData) getData(name,Integer.class);
    }
    public BooleanMetaData getBooleanData(String name){
        return (BooleanMetaData) getData(name,Boolean.class);
    }
    public ResourceMetaData<?> getData(String name){
        return data.get(name);
    }
    public <Data> ResourceMetaData<Data> getData(String name,Class<Data> dataClass){
        ResourceMetaData<?> metaData = this.data.get(name);
        if (metaData != null && dataClass.isInstance(metaData.getData())) {
            return (ResourceMetaData<Data>) metaData;
        } else {
            return null;
        }
    }
}
