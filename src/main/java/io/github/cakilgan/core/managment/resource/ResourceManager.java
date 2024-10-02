package io.github.cakilgan.core.managment.resource;

import io.github.cakilgan.clogger.system.CLoggerSystem;
import io.github.cakilgan.core.CakilganCore;
import io.github.cakilgan.core.io.FileHelper;
import io.github.cakilgan.core.serialization.CakilganComponent;
import io.github.cakilgan.core.serialization.Category;
import io.github.cakilgan.core.util.Text;
import io.github.cakilgan.clogger.CLogger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ResourceManager extends CakilganComponent {
    public static final CLogger LOGGER;
    public static final ResourceManager INSTANCE = new ResourceManager("src\\main\\resources");
    public static final String MAIN_CLASS_SCHEMA_CODE = "main";
    public static final String SUFFIX = File.separator;

    //Methods
    public static final String SUB_DIR_LIST = ".sub[]Dir";
    public static final String SUB_DIR = ".subDir";
    public static final String CREATE_FILE = ".createFile";
    public static final String DELETE_FILE = ".deleteFile";
    public static final String WRITE_FILE = ".writeFile";

    //Components
    public static final String NEW_LINE = "/nl";
    public static final String FILE_NAME= "/filename";

    static{
        LOGGER = CLoggerSystem.logger(ResourceManager.class);
    }

    public  String RESOURCES_MAIN_CLASS_PATH;
    public  File RESOURCES_MAIN_CLASS_FILE;
    public final String suffix ;
    public final HashMap<String,String> schema = new HashMap<>();
    public final HashMap<String,List<String>> subdir_schema = new HashMap<>();
    public final HashMap<String,List<String>> supdir_schema = new HashMap<>();
    public ResourceManager(String resourcesMainClassPath) {
        RESOURCES_MAIN_CLASS_PATH = resourcesMainClassPath;
        this.RESOURCES_MAIN_CLASS_FILE = new File(this.RESOURCES_MAIN_CLASS_PATH);
        suffix = RESOURCES_MAIN_CLASS_PATH+SUFFIX;
        if (!this.RESOURCES_MAIN_CLASS_FILE.exists()){
            LOGGER.error("main class path is not exists!");
            try {
                LOGGER.info("created a new file named: "+this.RESOURCES_MAIN_CLASS_FILE.createNewFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        schema.put(MAIN_CLASS_SCHEMA_CODE,suffix);
    }
    private File getPrivateFile(String s){
        return new File(s);
    }
    public File getFile(String  s){
       return getFile(MAIN_CLASS_SCHEMA_CODE,s);
    }
    public File getFile(String  schemacode,String dirname){
       return getPrivateFile(schema.get(schemacode)+dirname);
    }
    public void createFile(String schemacode,String dirname){
        try {
            LOGGER.warn("creating a new file -> "+getFile(schemacode,dirname).createNewFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void mkdir(String schemacode,String dirname){
        String path = schema.get(schemacode)+dirname+SUFFIX;
        schema.put(dirname,path);
        LOGGER.debug("making dir "+getPrivateFile(path).mkdir());
    }
    public void mkdir(String schemacode,String dirname,String ownschemacode){
        String path = schema.get(schemacode)+dirname+SUFFIX;
        schema.put(ownschemacode,path);
        LOGGER.debug("making dir "+getPrivateFile(path).mkdir());
    }
    public static ResourceManager readAndCreateSchemaFile(File file){
        if (!file.getPath().endsWith(".sch")){
           LOGGER.fatal("file is not a schema file returning null!");
            return null;
        }
        FileHelper helper = CakilganCore.createHelper(file);
        ResourceManager manager = null;
        String maindir;
        String pattern_name;
        String pattern_val;
        String pattern_schema_code;
        try {
            helper.analyzeAndSetupTheFile();
            for (ArrayList<String> line : helper.map().values()){
                if (line.isEmpty()){
                    continue;
                }
                if (line.get(0).equals(MAIN_CLASS_SCHEMA_CODE +":")){
                    LOGGER.debug("setting main class schemacode!");
                    maindir = line.get(1);
                    manager = new ResourceManager(maindir);
                }else {
                  String pattern = line.get(0);
                  if (pattern.contains(SUB_DIR)){
                      LOGGER.debug("executing subDir method!");
                      int i = pattern.indexOf(".");
                     pattern_name = pattern.substring(0,i);
                     pattern_val = line.get(1);
                     pattern_schema_code = pattern_val;
                     if (line.size()==3){
                         pattern_schema_code = line.get(2);
                     }
                      if (manager != null) {
                          manager.mkdir(pattern_name,pattern_val,pattern_schema_code);
                      }
                  }
                  if (pattern.contains(SUB_DIR_LIST)){
                      LOGGER.debug("executing sub[]Dir method!");
                      int i = pattern.indexOf(".");
                      pattern_name = pattern.substring(0,i);
                      pattern_val = line.get(1);
                      ArrayList<String> list = Text.convertDottedStringToList(pattern_val);
                      String finalPattern_val1 = pattern_val;
                      String finalPattern_name = pattern_name;
                      ResourceManager finalManager = manager;
                      list.forEach(s -> finalManager.mkdir(finalPattern_name, s));
                  }
                  if (pattern.contains(CREATE_FILE)){
                      LOGGER.debug("executing createFile method!");
                      int i = pattern.indexOf(".");
                      pattern_name = pattern.substring(0,i);
                      pattern_val = line.get(1);
                      if (line.size()>2){
                          String finalPattern_val = pattern_val;
                          line.removeIf(s -> s.contains(CREATE_FILE)||s.equals(finalPattern_val));
                          ArrayList<String> text = line;
                          StringBuilder builder = new StringBuilder();
                          for (String comp: text){
                              LOGGER.debug("component: "+comp);
                              if (comp.equals(FILE_NAME)){
                                  builder.append(file.getPath());
                                  continue;
                              }
                              if (comp.equals(NEW_LINE)){
                                  builder.append(System.lineSeparator());
                              }else{
                                  builder.append(comp);
                                  builder.append(" ");
                              }
                          }

                          LOGGER.debug("writing and exiting!");
                          Text.writeFileAndExit(manager.getFile(pattern_name,pattern_val),builder.toString());
                      }
                      manager.createFile(pattern_name,pattern_val);
                  }
                  if (pattern.contains(DELETE_FILE)){
                      int i = pattern.indexOf(".");
                      pattern_name = pattern.substring(0,i);
                      pattern_val = line.get(1);
                      boolean is = manager.getFile(pattern_name,pattern_val).delete();
                      if (!is){
                          LOGGER.error("cannot delete file-> "+pattern_val);
                      }
                  }
                  if (pattern.contains(WRITE_FILE)){
                      int i = pattern.indexOf(".");
                      pattern_name = pattern.substring(0,i);
                      pattern_val = line.get(1);
                      if (line.size()>2){
                          String finalPattern_val = pattern_val;
                          line.removeIf(new Predicate<String>() {
                              @Override
                              public boolean test(String s) {
                                  return s.contains(CREATE_FILE)||s.equals(finalPattern_val);
                              }
                          });
                          StringBuilder builder = new StringBuilder();
                          for (String comp: line){
                              LOGGER.debug("component: "+comp);
                              if (comp.equals(FILE_NAME)){
                                  builder.append(file.getPath());
                                  continue;
                              }
                              if (comp.equals(NEW_LINE)){
                                  builder.append(System.lineSeparator());
                              }else{
                                  builder.append(comp);
                                  builder.append(" ");
                              }
                          }

                          LOGGER.debug("writing and exiting!");
                          Text.writeFileAndExit(manager.getFile(pattern_name,pattern_val),builder.toString());
                      }
                  }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return manager;
    }

    public String visualizeSchema(){
        String builder = "main" +
                "\n";
        final int[] i = {1};
        schema.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String s, String s2) {
                List<String> supDirs = new ArrayList<>();
                List<String> subDirs = new ArrayList<>();
                schema.forEach(new BiConsumer<String, String>() {
                    @Override
                    public void accept(String s3, String s4) {
                        if (!s3.equals(s)){
                            if (isSupDirOf(s2,s4)){
                                subDirs.add(s3);
                            }
                            if (isSubDirOf(s2,s4)){
                                supDirs.add(s3);
                            }
                        }
                    }
                });
                if (!subDirs.isEmpty())
                 subdir_schema.put(s,subDirs);
                if (!supDirs.isEmpty())
                 supdir_schema.put(s,supDirs);
            }
        });
        return builder;
    }
    private String makeListedSub(String name){
        List<String> subdir = subdir_schema.get(name);
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append("\n");
        subdir.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                builder.append(" |");
                builder.append("\n");
                builder.append(" --> ").append(s);
                builder.append("\n");
            }
        });
        return builder.toString();
    }
    public String makeFinalListSub(String name){
        List<String> subdir = subdir_schema.get(name);
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append("\n");
        subdir.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                if (subdir_schema.get(s)!=null&& !subdir_schema.isEmpty()){
                    System.out.println("has subdir "+s);
                    builder.append("|  ").append(supdir_schema.get(s).toString());
                    TreeMap<String,String> treeMap = new TreeMap<>();

                    builder.append("\n");
                    builder.append(makeListedSub(s));
                    return;
                }else if (supdir_schema.get(s)!=null&&supdir_schema.get(s).size()==2&&supdir_schema.get(s).get(0).equals(name)){
                    builder.append("|  ");
                    builder.append("\n");
                    builder.append(s);
                    builder.append("\n");
                    return;
                }
            }
        });



        return builder.toString();
    }
    public boolean isSubDirOf(String path1,String path2){
        return path1.contains(path2);
    }
    public boolean isSupDirOf(String path1,String path2){
        return path2.contains(path1);
    }
    @Override
    public Category category() {
        return Category.MANAGMENT;
    }

    @Override
    public int id() {
        return 1;
    }

    @Override
    public String name() {
        return "Resource Manager";
    }

    @Override
    public CakilganComponent superComp() {
        return CakilganCore.INSTANCE;
    }

    @Override
    public String desc() {
        return "creates files dirs and manages them.";
    }
}
