package io.github.cakilgan.cresourcemanager.resources.file;

import io.github.cakilgan.cgraphics.c2d.render.C2DTexture;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSpriteSheet;
import io.github.cakilgan.core.io.FileHelper;
import io.github.cakilgan.cresourcemanager.comp.BooleanConsumer;
import io.github.cakilgan.cresourcemanager.resources.FileResource;
import org.joml.Vector2i;

public class TextureFileResource extends FileResource {
    C2DTexture texture;
    public TextureFileResource(String path) {
        super(path);
    }
    public TextureFileResource(FileResource resource, Vector2i scale){
        super(resource.id.getID());
        this.type.setContext(resource.type.getContext());
        texture = new C2DTexture(resource,scale);
        this.type.setCreate(new BooleanConsumer<FileHelper>() {
            @Override
            public boolean accept(FileHelper obj) {
                if (!texture.hasCreatedAlready()){
                texture.create();
                }
                return false;
            }
        });
    }
    public C2DSpriteSheet loadSpriteSheetFromMetadataFile(){
        C2DSpriteSheet spriteSheet;
        loadMetaData();
        int spriteWidth,spriteHeight,xCount,yCount,spacing;
        spriteWidth = getIntData("spriteWidth").getData();
        spriteHeight = getIntData("spriteHeight").getData();
        xCount = getIntData("xCount").getData();
        yCount = getIntData("yCount").getData();
        spacing = getIntData("spacing").getData();
        spriteSheet = new C2DSpriteSheet(texture,spriteWidth,spriteHeight,xCount,yCount,spacing);
        return spriteSheet;
    }
    public C2DTexture getTexture() {
        return texture;
    }
}
