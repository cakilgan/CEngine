package io.github.cakilgan.cresourcemanager.resources.types.file;

import io.github.cakilgan.cgraphics.c2d.render.C2DTexture;
import io.github.cakilgan.cresourcemanager.comp.ResourceType;

public class TextureResourceType extends ResourceType<String, C2DTexture,C2DTexture> {
    @Override
    public boolean create() {
        if (getContext()!=null){
            if (!getContext().hasCreatedAlready()){
            getContext().create();
            }
            return true;
        }
        return false;
    }
}
