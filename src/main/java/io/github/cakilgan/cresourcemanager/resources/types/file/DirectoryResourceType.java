package io.github.cakilgan.cresourcemanager.resources.types.file;

import io.github.cakilgan.core.CakilganCore;
import io.github.cakilgan.core.io.FileHelper;
import io.github.cakilgan.cresourcemanager.comp.ResourceType;

import java.io.File;

public class DirectoryResourceType extends ResourceType<String, File, FileHelper> {
    @Override
    public boolean create() {
            return getContext().mkdir();
    }
    @Override
    public boolean delete() {
        return getContext().delete();
    }
    @Override
    @Deprecated
    public boolean read() {
        return false;
    }
    @Override
    @Deprecated
    public boolean write() {
        return false;
    }
    @Override
    public boolean update() {
        return getUpdate().accept(CakilganCore.createHelper(context));
    }
}
