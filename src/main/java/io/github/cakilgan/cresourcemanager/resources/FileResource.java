package io.github.cakilgan.cresourcemanager.resources;

import io.github.cakilgan.core.io.FileHelper;
import io.github.cakilgan.cresourcemanager.Resource;
import io.github.cakilgan.cresourcemanager.comp.BooleanConsumer;
import io.github.cakilgan.cresourcemanager.comp.ResourceID;
import io.github.cakilgan.cresourcemanager.data.ResourceMetaData;
import io.github.cakilgan.cresourcemanager.resources.types.file.FileResourceType;
import io.github.cakilgan.cscriptengine.engines.MetadataFileScriptEngine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class FileResource extends Resource<String, File, FileHelper> {
    FileResource metadataFile;
    public FileResource(String path) {
        super(new ResourceID<>(path), new FileResourceType<>());
        this.type.setContext(new File(path));
    }

    @Override
    public void saveMetaData() {
        if (!this.type.getContext().exists()){
            this.type.create();
        }
        if (metadataFile==null)
         metadataFile = new FileResource(this.type.getContext().getPath()+".metadata");

        if (!metadataFile.type.getContext().exists()){
            metadataFile.type.create();
        }
        metadataFile.type.setWrite(new BooleanConsumer<FileHelper>() {
            @Override
            public boolean accept(FileHelper obj) {
                try {
                    obj.resetNotAppend();
                        List<Class<?>> classes = new ArrayList<>();
                    HashMap<Class<?>,List<String>> classStringHashMap = new HashMap<>();
                    for (ResourceMetaData<?> value : data.values()) {
                        if (!classes.contains(value.getData().getClass())){
                            classes.add(value.getData().getClass());
                        }
                    }
                    for (Class<?> aClass : classes) {
                        classStringHashMap.put(aClass,new ArrayList<>());
                    }
                    for (ResourceMetaData<?> value : data.values()) {
                        classStringHashMap.get(value.getData().getClass()).add(value.dataName+": [ "+value.getData()+" ]");
                    }
                    classStringHashMap.forEach(new BiConsumer<Class<?>, List<String>>() {
                        @Override
                        public void accept(Class<?> aClass, List<String> strings) {
                            try {
                                obj.writeln(aClass.getSimpleName()+ " {");
                                for (String string : strings) {
                                    obj.writeln(string+",");
                                }
                                obj.writeln("}");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                    obj.exitAndSave();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return false;
            }
        });
        LOGGER.debug("saving metadata: "+formatAllMetaData());
        metadataFile.type.write();
        metadataFile.type.setWrite(null);
    }

    @Override
    public void loadMetaData() {
        if (metadataFile==null)
            metadataFile = new FileResource(this.type.getContext().getPath()+".metadata");
        if (!metadataFile.type.getContext().exists()){
            LOGGER.info("metadata file is not exists creating one! "+metadataFile.id.getID());
            metadataFile.type.create();
            return;
        }
        MetadataFileScriptEngine scriptEngine = new MetadataFileScriptEngine();
        data = scriptEngine.parse(metadataFile);
    }
}
