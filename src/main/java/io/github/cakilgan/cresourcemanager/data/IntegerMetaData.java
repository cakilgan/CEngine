package io.github.cakilgan.cresourcemanager.data;

public class IntegerMetaData extends ResourceMetaData<Integer>{
    public IntegerMetaData(Integer data,String name) {
        super(data,name);
    }

    @Override
    public String format(Integer data) {
        return data.toString();
    }
}
