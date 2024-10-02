package io.github.cakilgan.cresourcemanager.resources.types.file;

import io.github.cakilgan.core.CakilganCore;
import io.github.cakilgan.core.io.FileHelper;
import io.github.cakilgan.cresourcemanager.comp.ResourceType;

import java.io.File;
import java.io.IOException;

public class FileResourceType<T> extends ResourceType<T, File,FileHelper> {
    @Override
    public boolean create() {
        try {
            return getContext().createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean delete() {
        return getContext().delete();
    }
    @Override
    public boolean read() {
        return getRead().accept(CakilganCore.createHelper(getContext()));
    }
    @Override
    public boolean write() {
        return getWrite().accept(CakilganCore.createHelper(getContext()));
    }
    @Override
    public boolean update() {
        return getUpdate().accept(CakilganCore.createHelper(getContext()));
    }
}
