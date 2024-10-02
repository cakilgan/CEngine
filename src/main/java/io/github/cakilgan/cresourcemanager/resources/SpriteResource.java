package io.github.cakilgan.cresourcemanager.resources;

import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.cresourcemanager.Resource;
import io.github.cakilgan.cresourcemanager.comp.ResourceID;
import io.github.cakilgan.cresourcemanager.comp.ResourceType;

public class SpriteResource extends Resource<String, C2DSprite,C2DSprite> {
    public SpriteResource(String id,C2DSprite context) {
        super(new ResourceID<>(id), new ResourceType<String,C2DSprite,C2DSprite>());
        this.type.setContext(context);
    }

    @Override
    public void saveMetaData() {

    }

    @Override
    public void loadMetaData() {

    }
}
