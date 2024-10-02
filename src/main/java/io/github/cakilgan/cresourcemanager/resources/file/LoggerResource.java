package io.github.cakilgan.cresourcemanager.resources.file;

import io.github.cakilgan.core.io.FileHelper;
import io.github.cakilgan.cresourcemanager.comp.BooleanConsumer;
import io.github.cakilgan.cresourcemanager.resources.FileResource;

import java.io.IOException;
import java.util.List;

public class LoggerResource extends FileResource {
    public LoggerResource(String path) {
        super(path);
    }
    public LoggerResource(FileResource file){
        super(file.id.getID());
        this.data = file.data;
    }
    public boolean write(List<String> logs){
        this.type.setWrite(new BooleanConsumer<FileHelper>() {
            @Override
            public boolean accept(FileHelper obj) {
                try {
                    obj.resetNotAppend();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                for (String s:logs){
                    try {
                        obj.write(s);
                        obj.ln();
                    } catch (IOException e) {
                        LOGGER.excFalse(e);
                        return false;
                    }
                }
                try {
                    obj.exitAndSave();
                    return true;
                } catch (IOException e) {
                    LOGGER.excFalse(e);
                    return false;
                }
            }
        });
        return this.type.write();
    }
}
