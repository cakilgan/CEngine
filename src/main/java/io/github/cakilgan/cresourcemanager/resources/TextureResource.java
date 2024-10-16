package io.github.cakilgan.cresourcemanager.resources;

import io.github.cakilgan.cgraphics.c2d.render.C2DTexture;
import io.github.cakilgan.cresourcemanager.Resource;
import io.github.cakilgan.cresourcemanager.comp.ResourceID;
import io.github.cakilgan.cresourcemanager.resources.types.file.TextureResourceType;
import org.joml.Vector2i;

public class TextureResource extends Resource<String, C2DTexture,C2DTexture> {
    public TextureResource(FileResource path, Vector2i scale) {
        super(new ResourceID<>(path.id.getID()),new TextureResourceType());
        type.setContext(new C2DTexture(path,scale));
        if (!type.getContext().hasCreatedAlready()){
        type.getContext().create();
        }
    }
    public TextureResource(FileResource path, Vector2i scale,String id) {
        super(new ResourceID<>(id),new TextureResourceType());
        type.setContext(new C2DTexture(path,scale));
        if (!type.getContext().hasCreatedAlready()){
            type.getContext().create();
        }
    }
    public TextureResource(C2DTexture texture,String mapcode) {
        super(new ResourceID<>(mapcode),new TextureResourceType());
        type.setContext(texture);
        if (!type.getContext().hasCreatedAlready()){
            type.getContext().create();
        }
    }

    @Override
    public void saveMetaData() {

    }

    @Override
    public void loadMetaData() {

    }
}
