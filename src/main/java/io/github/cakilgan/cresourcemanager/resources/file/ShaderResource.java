package io.github.cakilgan.cresourcemanager.resources.file;

import io.github.cakilgan.core.io.FileHelper;
import io.github.cakilgan.cresourcemanager.comp.BooleanConsumer;
import io.github.cakilgan.cresourcemanager.resources.FileResource;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class ShaderResource extends FileResource {
    public int shaderID;
    public ShaderResource(String path) {
        super(path);
    }
    public ShaderResource(FileResource resource){
        super(resource.id.getID());
        this.type.setContext(resource.type.getContext());
    }
    public String getShaderText(){
        final String[] returnStr = new String[1];
        returnStr[0] = "";
        this.type.setRead(new BooleanConsumer<FileHelper>() {
            @Override
            public boolean accept(FileHelper obj) {
                try {
                    obj.analyzeAndSetupTheFile();
                    List<String> allLines = obj.readLines();
                    allLines.forEach(new Consumer<String>() {
                        @Override
                        public void accept(String s) {
                            returnStr[0] = returnStr[0]+s+"\n";
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return false;
            }
        });
        this.type.read();
        return returnStr[0];
    }
}
