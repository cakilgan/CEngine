package io.github.cakilgan.cresourcemanager.resources;

import io.github.cakilgan.cresourcemanager.Resource;
import io.github.cakilgan.cresourcemanager.comp.BooleanConsumer;
import io.github.cakilgan.cresourcemanager.comp.ResourceID;
import io.github.cakilgan.cresourcemanager.comp.ResourceType;
import io.github.cakilgan.cresourcemanager.resources.file.ShaderResource;
import io.github.cakilgan.cresourcemanager.resources.types.file.ShaderProgramResourceType;

public class ShaderProgramResource extends Resource<Integer, ShaderResource, ShaderResource> {

    public ShaderProgramResource(ResourceID<Integer> id, ResourceType<Integer, ShaderResource, ShaderResource> type) {
        super(id, type);
    }

    @Override
    public void saveMetaData() {

    }

    @Override
    public void loadMetaData() {

    }

    public ShaderResource fragment,vertex;
    public ShaderProgramResource(ShaderResource vertex,ShaderResource fragment){
        super(new ResourceID<>(0),new ShaderProgramResourceType());
        this.vertex = vertex;
        this.fragment = fragment;
    }
    public String getShaderText(){
        final String[] finalShaderText = new String[1];
       this.type.setRead(new BooleanConsumer<ShaderResource>() {
           @Override
           public boolean accept(ShaderResource obj) {
               finalShaderText[0] = obj.getShaderText();
               return false;
           }
       });
       this.type.read();
        return finalShaderText[0];
    }

}
