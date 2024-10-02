package io.github.cakilgan.cresourcemanager.comp;

public interface IResourceType {
    boolean create();
    boolean read();
    boolean write();
    boolean update();
    boolean delete();
}
