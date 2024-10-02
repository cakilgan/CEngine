package io.github.cakilgan.cresourcemanager;

import io.github.cakilgan.cgraphics.c2d.render.C2DTexture;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSpriteSheet;
import io.github.cakilgan.cresourcemanager.comp.ResourceID;
import io.github.cakilgan.cresourcemanager.resources.*;
import io.github.cakilgan.cresourcemanager.resources.file.SchemaResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CResourceManager {
    HashMap<String,Resource<?,?,?>> RESOURCES;
    public CResourceManager(){
        RESOURCES = new HashMap<>();
    }
    public void addResource(String code,Resource<?,?,?> resource){
        this.RESOURCES.put(new ResourceID<>(code).getID(),resource);
    }
    public void addResource(Resource<?,?,?> resource){this.RESOURCES.put((String) resource.id.getID(),resource);};
    public Resource<?, ?, ?> getResource(String code) {
        return RESOURCES.get(code);
    }
    public FileResource getFileResource(String code){return (FileResource) RESOURCES.get(code);}
    public DirectoryResource getDirectoryResource(String code) {return (DirectoryResource) RESOURCES.get(code);}
    public TextureResource getTextureResource(String code){return (TextureResource) RESOURCES.get(code);}
    public C2DTexture getTexture(String code){return getTextureResource(code).type.getContext();}
    public ShaderProgramResource getShaderProgramResource(String code){return (ShaderProgramResource) RESOURCES.get(code);}
    public SchemaResource getSchemaResource(String code){return (SchemaResource) RESOURCES.get(code);}
    public SpriteResource getSpriteResource(String code){return (SpriteResource) RESOURCES.get(code);};
    public SpriteSheetResource getSpriteSheetResource(String code){return (SpriteSheetResource) RESOURCES.get(code);};
    public C2DSpriteSheet getSpriteSheet(String code){return getSpriteSheetResource(code).type.getContext();};
    public C2DSprite getSprite(String code){return getSpriteResource(code).type.getContext();};

}
