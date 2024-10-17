package io.github.cakilgan.data.comp;

import io.github.cakilgan.data.Data;

public class StringData extends Data<String,String> {
    public StringData(String ID, String normal) {
        super(ID, normal);
    }

    @Override
    public String get() {
        return data;
    }

    @Override
    public void set(String data) {
        this.data = data;
    }
}
