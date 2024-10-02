package io.github.cakilgan.cresourcemanager.data;

public class BooleanMetaData extends ResourceMetaData<Boolean>{
    public BooleanMetaData(Boolean data,String name) {
        super(data,name);
    }

    @Override
    public String format(Boolean data) {
        return data.toString();
    }
}
