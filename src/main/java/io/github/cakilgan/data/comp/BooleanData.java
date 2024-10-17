package io.github.cakilgan.data.comp;

import io.github.cakilgan.core.io.FileHelper;
import io.github.cakilgan.data.Data;

import java.io.IOException;

public class BooleanData extends Data<String,Boolean> {
    public BooleanData(String ID, Boolean normal) {
        super(ID, normal);
    }

    @Override
    public Boolean get() {
        return data;
    }
    @Override
    public void set(Boolean data) {
        this.data = data;
    }
}
