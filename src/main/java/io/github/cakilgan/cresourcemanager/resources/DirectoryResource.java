package io.github.cakilgan.cresourcemanager.resources;

import io.github.cakilgan.core.io.FileHelper;
import io.github.cakilgan.cresourcemanager.Resource;
import io.github.cakilgan.cresourcemanager.comp.BooleanConsumer;
import io.github.cakilgan.cresourcemanager.comp.ResourceID;
import io.github.cakilgan.cresourcemanager.resources.file.SchemaResource;
import io.github.cakilgan.cresourcemanager.resources.types.file.DirectoryResourceType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DirectoryResource extends Resource<String, File, FileHelper> {
    private List<DirectoryResource> subDirs;
    private List<FileResource> subFiles;
    private SchemaResource schemaFile;

    public DirectoryResource(String path) {
        super(new ResourceID<>(path), new DirectoryResourceType());
        this.subDirs = new ArrayList<>();
        this.subFiles = new ArrayList<>();
        this.type.setContext(new File(this.id.getID()));
        initializeContents();
        setUpdateConsumer();
    }

    private void initializeContents() {
        File[] files = type.getContext().listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    subDirs.add(new DirectoryResource(file.getPath()));
                } else if (file.isFile()) {
                    subFiles.add(new FileResource(file.getPath()));
                }
            }
        }
    }

    private void setUpdateConsumer() {
        type.setUpdate(new BooleanConsumer<FileHelper>() {
            @Override
            public boolean accept(FileHelper obj) {
                File[] files = type.getContext().listFiles();
                if (files != null) {
                    subDirs.clear();
                    subFiles.clear();
                    for (File file : files) {
                        if (file.isDirectory()) {
                            subDirs.add(new DirectoryResource(file.getPath()));
                        } else if (file.isFile()) {
                            subFiles.add(new FileResource(file.getPath()));
                        }
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public List<DirectoryResource> getSubDirs() {
        return new ArrayList<>(subDirs);
    }

    public List<FileResource> getSubFiles() {
        return new ArrayList<>(subFiles);
    }

    public boolean createSubdirectory(String name) {
        File subDir = new File(type.getContext(),name);
        if (subDir.mkdir()) {
            return subDirs.add(new DirectoryResource(subDir.getPath()));
        }
        return false;
    }

    public boolean deleteSubdirectory(String name) {
        File subDir = new File(type.getContext(), name);
        if (subDir.isDirectory() && deleteRecursive(subDir)) {
            return subDirs.removeIf(dir -> dir.id.getID().equals(name));
        }
        return false;
    }

    public boolean createFile(String name) {
        File file = new File(type.getContext(),name);
        try {
            if (file.createNewFile()) {
                return subFiles.add(new FileResource(file.getPath()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteFile(String name) {
        File file = new File(type.getContext(), name);
        if (file.isFile() && file.delete()) {
            return subFiles.removeIf(f -> f.id.getID().equals(name));
        }
        return false;
    }

    private boolean deleteRecursive(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteRecursive(f);
                }
            }
        }
        return file.delete();
    }

    public void setup() {
        getSubFiles().forEach(new Consumer<FileResource>() {
            @Override
            public void accept(FileResource fileResource) {
                String idSub = id.getID().substring(id.getID().lastIndexOf("/")+1)+".sch";
                String checkIDSub = fileResource.id.getID().substring(fileResource.id.getID().lastIndexOf("\\")+1);
                LOGGER.debug("--< "+idSub+" to "+checkIDSub);
                if (idSub.equals(checkIDSub)){
                    LOGGER.debug("found schema file for "+id.getID());
                    schemaFile = new SchemaResource(fileResource.id.getID());
                    schemaFile.type.setContext(new File(schemaFile.id.getID()));
                }
            }
        });
        if (schemaFile!=null){
            LOGGER.debug("reading schema file -> "+schemaFile.read(this));
        }
    }
    public FileResource getNewFile(DirectoryResource dir,String path){
        return new FileResource(dir.id.getID()+File.separator+path);
    }
    public FileResource getNewFile(String path){
        return getNewFile(this,path);
    }
    public FileResource getFile(String path){
        return getFile(this,path);
    }
    public FileResource getFile(DirectoryResource dir,String path){
        final FileResource[] file = new FileResource[1];
        dir.getSubFiles().forEach(new Consumer<FileResource>() {
            @Override
            public void accept(FileResource fileResource) {
                LOGGER.debug("file: "+fileResource.id.getID()+" testfile: "+path);
                if (fileResource.id.getID().equals(dir.id.getID()+File.separator+path)){
                    file[0] = fileResource;
                }
            }
        });
        if (file[0]==null){
            LOGGER.excFalse(new FileNotFoundException("cannot found file for "+path));
        }
        return file[0];
    }
    public DirectoryResource getDir(String path){
        path = type.getContext().getPath()+File.separator+path;
        final DirectoryResource[] dir = new DirectoryResource[1];
        String finalPath = path;
        subDirs.forEach(new Consumer<DirectoryResource>() {
            @Override
            public void accept(DirectoryResource directoryResource) {
                LOGGER.debug("dir: "+directoryResource.id.getID()+" path: "+ finalPath);
                if (finalPath.equals(directoryResource.id.getID())){
                    LOGGER.debug("found dir");
                    dir[0] = directoryResource;
                }
            }
        });
        return dir[0];
    }




    private String name(DirectoryResource resource){
        return resource.id.getID().substring(resource.id.getID().lastIndexOf("\\"));
    }
    private String n_name(DirectoryResource resource){
        return resource.id.getID().substring(resource.id.getID().lastIndexOf("/"));
    }



    public String directoryTreeInSingleLog() {
        StringBuilder treeOutput = new StringBuilder();
        buildDirectoryTree(treeOutput, this, "", true);
        return treeOutput.toString();
    }

    private void buildDirectoryTree(StringBuilder output, DirectoryResource dir, String indent, boolean last) {
        output.append(indent).append(last ? "\\-- " : "|-- ").append(dir.id.getID().substring(dir.id.getID().lastIndexOf(File.separator) + 1)).append("\n");
        indent += last ? "    " : "|   ";

        for (int i = 0; i < dir.getSubDirs().size(); i++) {
            buildDirectoryTree(output, dir.getSubDirs().get(i), indent, i == dir.getSubDirs().size() - 1);
        }

        for (int i = 0; i < dir.getSubFiles().size(); i++) {
            boolean isLastFile = (i == dir.getSubFiles().size() - 1);
            output.append(indent).append(isLastFile ? "\\-- " : "|-- ").append(dir.getSubFiles().get(i).id.getID().substring(dir.getSubFiles().get(i).id.getID().lastIndexOf(File.separator) + 1)).append("\n");
        }
    }

    @Override
    public void saveMetaData() {

    }

    @Override
    public void loadMetaData() {

    }
}
