package io.github.cakilgan.engine.managment;


import io.github.cakilgan.cgraphics.c2d.render.C2DTexture;
import io.github.cakilgan.cgraphics.c2d.render.font.C2DFont;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSpriteSheet;
import io.github.cakilgan.clogger.CLogger;
import io.github.cakilgan.core.io.FileHelper;
import io.github.cakilgan.cresourcemanager.Resource;
import io.github.cakilgan.cresourcemanager.comp.BooleanConsumer;
import io.github.cakilgan.cresourcemanager.comp.ResourceID;
import io.github.cakilgan.cresourcemanager.resources.*;
import io.github.cakilgan.cresourcemanager.resources.file.LoggerResource;
import io.github.cakilgan.cresourcemanager.resources.file.SchemaResource;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.system.CEComponent;
import io.github.cakilgan.cresourcemanager.CResourceManager;
import io.github.cakilgan.engine.system.RunOnExit;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CEResourceManager  implements CEComponent, RunOnExit {
    public CEResourceManager(){
        MAIN = new DirectoryResource("src/main/resources");
        MAIN.setup();
        manager = new CResourceManager();
        manager.addResource("main",MAIN);
    }
    public CResourceManager manager;
    private DirectoryResource MAIN;
    public DirectoryResource textures(){
        return MAIN.getDir("engine").getDir("assets").getDir("textures");
    }
    public DirectoryResource data(){
        return MAIN.getDir("engine").getDir("data");
    }
    boolean writeLogs = false;
    public void setWriteLogs(boolean writeLogs) {
        this.writeLogs = writeLogs;
    }

    @Override
    public void init() {
    }
    @Override
    public void loop() {

    }
    int maxLogFiles = 10;
    public void setMaxLogFiles(int maxLogFiles) {
        this.maxLogFiles = maxLogFiles;
    }
    @Override
    public void dispose() {
        if (MAIN.getDir("logs").getSubFiles().size()>maxLogFiles){
            for (FileResource logs : MAIN.getDir("logs").getSubFiles()) {
                logs.type.delete();
            }
        }
        if (writeLogs){
            FileResource latest = MAIN.getDir("logs").getNewFile("latest.clog");
            if (latest.type.getContext().exists()){
             latest.loadMetaData();
             String creationDate = latest.getStringData("creationDate").getData();
                final List<String>[] oldLogs = new List[]{new ArrayList<>()};
             latest.type.setRead(new BooleanConsumer<FileHelper>() {
                 @Override
                 public boolean accept(FileHelper obj) {
                     try {
                         obj.analyzeAndSetupTheFile();
                         oldLogs[0] = obj.readLines();
                     } catch (IOException e) {
                         throw new RuntimeException(e);
                     }
                     return false;
                 }
             });
             latest.type.read();
             FileResource oldFile = MAIN.getDir("logs").getNewFile(creationDate+".clog");
             oldFile.type.create();
             oldFile.type.setWrite(new BooleanConsumer<FileHelper>() {
                 @Override
                 public boolean accept(FileHelper obj) {
                     try {
                         obj.resetNotAppend();
                         for (String oldLog : oldLogs[0]) {
                             obj.writeln(oldLog);
                         }
                         obj.exitAndSave();
                     } catch (IOException e) {
                         throw new RuntimeException(e);
                     }
                     return false;
                 }
             });
             oldFile.type.write();
             latest.addStringData("creationDate",new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()));
             latest.saveMetaData();
            }else{
                latest.type.create();
                latest.addStringData("creationDate",new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()));
                latest.saveMetaData();
            }
            List<String> logs =  CLogger.getGlobalLogHistory();
            latest.type.setWrite(new BooleanConsumer<FileHelper>() {
                @Override
                public boolean accept(FileHelper obj) {
                    try {
                        obj.resetNotAppend();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    for (String log : logs) {
                        try {
                            obj.writeln(log);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        obj.exitAndSave();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return false;
                }
            });
            latest.type.write();
        }
    }

    @Override
    public void runExit() {

    }

    public void addResource(String code, Resource<?,?,?> resource){
        manager.addResource(code,resource);
    }
    public void addResource(Resource<?,?,?> resource){manager.addResource(resource);};
    public Resource<?, ?, ?> getResource(String code) {
        return manager.getResource(code);
    }
    public FileResource getFileResource(String code){return manager.getFileResource(code);}
    public DirectoryResource getDirectoryResource(String code) {return manager.getDirectoryResource(code);}
    public TextureResource getTextureResource(String code){return manager.getTextureResource(code);}
    public C2DTexture getTexture(String code){return getTextureResource(code).type.getContext();}
    public ShaderProgramResource getShaderProgramResource(String code){return manager.getShaderProgramResource(code);}
    public SchemaResource getSchemaResource(String code){return manager.getSchemaResource(code);}
    public SpriteResource getSpriteResource(String code){return manager.getSpriteResource(code);};
    public C2DSprite getSprite(String code){return getSpriteResource(code).type.getContext();};
    public SpriteSheetResource getSpriteSheetResource(String code){return manager.getSpriteSheetResource(code);};
    public C2DSpriteSheet getSpriteSheet(String code){return manager.getSpriteSheet(code);};
    public FontResource getFontResource(String code) {
        return (FontResource) getResource(code);
    }
    public C2DFont getFont(String code){
        return getFontResource(code).type.getContext();
    }
}
