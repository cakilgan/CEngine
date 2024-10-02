package io.github.cakilgan.cresourcemanager.data;

public class StringMetaData extends ResourceMetaData<String>{
    public StringMetaData(String data,String name) {
        super(data,name);
    }
    @Override
    public String format(String data) {
        return data;
    }
}
