package io.github.cakilgan.cscriptengine.engines;

import io.github.cakilgan.core.CakilganCore;
import io.github.cakilgan.core.io.FileHelper;
import io.github.cakilgan.core.util.Text;
import io.github.cakilgan.cscriptengine.CakilganScriptEngine;
import io.github.cakilgan.cscriptengine.comp.Keyword;
import io.github.cakilgan.cresourcemanager.resources.DirectoryResource;
import io.github.cakilgan.cresourcemanager.resources.FileResource;
import io.github.cakilgan.cresourcemanager.resources.file.SchemaResource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class SchemaFileScriptEngine extends CakilganScriptEngine {
    @Override
    public boolean parse(File file) {
        return super.parse(file);
    }
    public boolean parse(SchemaResource file, DirectoryResource main){
        Keyword<String> mkdir = new Keyword<>("mkdir",String.class){
            @Override
            public void action(String value) {
                List<String> list = Text.makeList(value);
                if (list.size()>1){
                    list.forEach(new Consumer<String>() {
                        @Override
                        public void accept(String s) {
                            main.createSubdirectory(s);
                        }
                    });
                }else{
                    main.createSubdirectory(value);
                }
            }
        };
        Keyword<String> deldir = new Keyword<>("deldir",String.class){
            @Override
            public void action(String value) {
                List<String> list = Text.makeList(value);
                if (list.size()>1){
                    list.forEach(new Consumer<String>() {
                        @Override
                        public void accept(String s) {
                            main.deleteSubdirectory(s);
                        }
                    });
                }else{
                    main.deleteSubdirectory(value);
                }
            }
        };
        Keyword<String> mkfile = new Keyword<>("mkfile",String.class){
            @Override
            public void action(String value) {
                List<String> list = Text.makeList(value);
                if (list.size()==2){
                    main.createFile(list.get(0)+File.separator+list.get(1));
                    return;
                }
                if (list.size()>1){
                    for (int i = 0; i < list.size(); i++) {
                            main.createFile(list.get(i));
                    }
                }else{
                    main.createFile(value);
                }
            }
        };
        Keyword<String> delfile = new Keyword<>("delfile",String.class){
            @Override
            public void action(String value) {
                List<String> list = Text.makeList(value);
                if (list.size()>1){
                    list.forEach(new Consumer<String>() {
                        @Override
                        public void accept(String s) {
                            main.deleteFile(s);
                        }
                    });
                }else{
                    main.deleteFile(value);
                }
            }
        };
        Keyword<String> writefile = new Keyword<>("wfile",String.class){
            @Override
            public void action(String value) {
                Scanner scanner = new Scanner(value);
                String dirName = scanner.next();
                if (dirName.equals("/m")){
                    dirName = "";
                }
                String filename = scanner.next();
                String isWriteOn = scanner.next();
                List<String> list = new ArrayList<>();
                do {
                    list.add(scanner.next());
                }while (scanner.hasNext());
                FileHelper helper = null;
                if (dirName.isEmpty()){
                 helper= CakilganCore.createHelper(new File(main.type.getContext().getPath(),filename));
                }else{
                    helper= CakilganCore.createHelper(new File(main.type.getContext().getPath()+File.separator+dirName,filename));
                }
                if (Boolean.valueOf(isWriteOn)){
                    try {
                        helper.resetNotAppend();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).equals(list.get(0))||list.get(i).equals(list.get(list.size()-1))){
                            helper.write(list.get(i));
                        }else{
                            if (list.get(i).trim().equals("/nl")){
                                helper.write("\n");
                            }
                            else{
                                if (list.get(i).startsWith("/s")){
                                    helper.write(list.get(i).substring(2));
                                }else{
                                helper.write(" "+list.get(i));
                                }
                            }
                        }
                    }
                    helper.exitAndSave();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Keyword<String> addMetaData = new Keyword<>("addMetaData",String.class){
            @Override
            public void action(String value) {
                LOGGER.info(value);
                Scanner scanner = new Scanner(value);
                String dirName = scanner.next();
                String filename = scanner.next();
                LOGGER.debug("dir: "+dirName+" file: "+filename+" path: "+main.type.getContext().getPath()+"/"+dirName);
                List<String> words = new ArrayList<>();
                do {
                    words.add(scanner.next());
                }while (scanner.hasNext());
                FileResource file  = main.getFile(main.getDir(dirName),filename);
                for (int i = 0; i < words.size(); i++) {
                    if (words.get(i).equals("int")){
                        String dataName = words.get(i+1);
                        int data = Integer.valueOf(words.get(i+2));
                        file.addIntData(dataName,data);
                        i+=3;
                    }
                    if (words.get(i).equals("bool")){
                        String dataName = words.get(i+1);
                        boolean data = Boolean.valueOf(words.get(i+2));
                        file.addBooleanData(dataName,data);
                        i+=3;
                    }
                    if (words.get(i).equals("string")){
                        String dataName = words.get(i+1);
                        String data = words.get(i+2);
                        if (data.startsWith("\"")){
                            data = "";
                            for (int j = i+2; j < words.size(); j++) {
                                data = data+" "+words.get(j);
                                if (words.get(j).endsWith("\"")){
                                    break;
                                }
                            }
                        }
                        file.addStringData(dataName, Text.unSurround(data,'\"','\"'));
                        i+=3;
                    }
                }
            }
        };
        this.addKeyword(mkdir);
        this.addKeyword(deldir);
        this.addKeyword(mkfile);
        this.addKeyword(delfile);
        this.addKeyword(writefile);
        this.addKeyword(addMetaData);
        return this.parse(file.type.getContext());
    }
}
