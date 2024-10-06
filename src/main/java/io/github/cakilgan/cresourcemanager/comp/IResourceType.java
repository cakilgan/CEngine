package io.github.cakilgan.cresourcemanager.comp;

public interface IResourceType {
    boolean create();
    boolean write();
    boolean update();
    boolean delete();
    boolean read();
}
