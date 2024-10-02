package io.github.cakilgan.core;

import io.github.cakilgan.core.io.FileHelper;
import io.github.cakilgan.core.managment.resource.ResourceManager;
import io.github.cakilgan.core.serialization.CakilganComponent;
import io.github.cakilgan.core.serialization.Category;
import io.github.cakilgan.clogger.CLogger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class CakilganCore extends CakilganComponent {
   public static final CakilganCore INSTANCE = new CakilganCore();
   static {
       INSTANCE.init();
   }
    protected final HashMap<Integer,CakilganComponent> MAIN_COMPONENTS = new HashMap<>();
    public HashMap<Integer, CakilganComponent> getMAIN_COMPONENTS() {
        return MAIN_COMPONENTS;
    }

    public static FileHelper createHelper(File file){
        try {
            FileHelper.LOGGER.debug("creating helper for -> "+file.getPath());
            return new FileHelper(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static ResourceManager createManagerFromSchemaFile(File file){
        return ResourceManager.readAndCreateSchemaFile(file);
    }
    public CakilganCore(){
    }
    public void init(){
        MAIN_COMPONENTS.put(this.id(),this);
        MAIN_COMPONENTS.put(ResourceManager.INSTANCE.id(), ResourceManager.INSTANCE);
        MAIN_COMPONENTS.put(FileHelper.INSTANCE.id(),FileHelper.INSTANCE);

        try {
            FileHelper.INSTANCE.resetNotAppend();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MAIN_COMPONENTS.forEach(new BiConsumer<Integer, CakilganComponent>() {
            @Override
            public void accept(Integer integer, CakilganComponent cakilganComponent) {
                try {
                    FileHelper.INSTANCE.writeln(cakilganComponent.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        try {
            FileHelper.INSTANCE.exitAndSave();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String name() {
        return "Çakılgan Core";
    }

    @Override
    public int id() {
        return 0;
    }

    @Override
    public String desc() {
        return "core class of all Çakılgan projects.";
    }

    @Override
    public Category category() {
        return Category.CORE;
    }

    @Override
    public CakilganComponent superComp() {
        return this;
    }
}
