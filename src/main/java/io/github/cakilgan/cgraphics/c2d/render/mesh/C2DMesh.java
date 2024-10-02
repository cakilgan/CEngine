package io.github.cakilgan.cgraphics.c2d.render.mesh;

import io.github.cakilgan.cgraphics.IBindUnbind;

public class C2DMesh implements IBindUnbind,AutoCloseable {
    public void init(){

    }

    public void render(){

    }
    @Override
    public void bind() {

    }

    @Override
    public void unbind() {

    }

    @Override
    public void close() throws Exception {
        unbind();
    }
}
