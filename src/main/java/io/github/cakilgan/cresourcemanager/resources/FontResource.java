package io.github.cakilgan.cresourcemanager.resources;

import io.github.cakilgan.cgraphics.c2d.render.font.C2DFont;
import io.github.cakilgan.cgraphics.c2d.render.font.C2DFontRenderer;
import io.github.cakilgan.cresourcemanager.Resource;
import io.github.cakilgan.cresourcemanager.comp.ResourceID;
import io.github.cakilgan.cresourcemanager.comp.ResourceType;

public class FontResource extends Resource<String, C2DFont, C2DFontRenderer> {
    public FontResource(String ID,C2DFont font) {
        super(new ResourceID<>(ID), new ResourceType<>());
        this.type.setContext(font);
    }

    @Override
    public void saveMetaData() {

    }

    @Override
    public void loadMetaData() {

    }
}
