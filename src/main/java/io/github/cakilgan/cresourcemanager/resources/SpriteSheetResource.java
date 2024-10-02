package io.github.cakilgan.cresourcemanager.resources;

import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSpriteSheet;
import io.github.cakilgan.cresourcemanager.Resource;
import io.github.cakilgan.cresourcemanager.comp.ResourceID;
import io.github.cakilgan.cresourcemanager.comp.ResourceType;

public class SpriteSheetResource extends Resource<String, C2DSpriteSheet,C2DSpriteSheet> {
    public SpriteSheetResource(String id, C2DSpriteSheet context) {
        super(new ResourceID<>(id), new ResourceType<>());
        type.setContext(context);
    }

    @Override
    public void saveMetaData() {

    }

    @Override
    public void loadMetaData() {

    }
}
