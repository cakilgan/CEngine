package io.github.cakilgan.cresourcemanager.resources.types.file;

import io.github.cakilgan.cresourcemanager.comp.ResourceType;
import io.github.cakilgan.cresourcemanager.resources.file.ShaderResource;

public class ShaderProgramResourceType extends ResourceType<Integer, ShaderResource,ShaderResource> {
    @Override
    public boolean read() {
        return getRead().accept(getContext());
    }
}
