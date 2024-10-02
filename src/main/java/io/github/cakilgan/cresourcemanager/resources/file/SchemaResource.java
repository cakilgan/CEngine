package io.github.cakilgan.cresourcemanager.resources.file;

import io.github.cakilgan.core.io.FileHelper;
import io.github.cakilgan.cresourcemanager.comp.BooleanConsumer;
import io.github.cakilgan.cresourcemanager.resources.DirectoryResource;
import io.github.cakilgan.cscriptengine.engines.SchemaFileScriptEngine;
import io.github.cakilgan.cresourcemanager.resources.FileResource;

public class SchemaResource extends FileResource {
    DirectoryResource main;
    public SchemaResource(String path) {
        super(path);

    }
    public boolean read(DirectoryResource main){
        SchemaFileScriptEngine engine = new SchemaFileScriptEngine();
        SchemaResource resource = this;
        this.main = main;
        this.type.setRead(new BooleanConsumer<FileHelper>() {
            @Override
            public boolean accept(FileHelper obj) {
                return engine.parse(resource,main);
            }
        });
         return this.type.read();
    }
}
