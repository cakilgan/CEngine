package io.github.cakilgan.data.comp;

import io.github.cakilgan.data.Data;

public class IntegerData extends Data<String,Integer> {
    public IntegerData(String ID, Integer normal) {
        super(ID, normal);
    }

    @Override
    public Integer get() {
        return data;
    }

    @Override
    public void set(Integer data) {
        this.data = data;
    }
}
