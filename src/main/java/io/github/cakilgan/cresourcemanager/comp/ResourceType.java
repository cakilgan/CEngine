package io.github.cakilgan.cresourcemanager.comp;

public class ResourceType<T,K,R> implements IResourceType {
    BooleanConsumer<R> create;
    BooleanConsumer<R> write;
    BooleanConsumer<R> update;
    BooleanConsumer<R> delete;
    BooleanConsumer<R> exit;
    BooleanConsumer<R> read;

    public BooleanConsumer<R> getCreate() {
        return create;
    }
    public BooleanConsumer<R> getWrite() {
        return write;
    }
    public BooleanConsumer<R> getUpdate() {
        return update;
    }
    public BooleanConsumer<R> getDelete() {
        return delete;
    }
    public BooleanConsumer<R> getExit() {
        return exit;
    }
    public BooleanConsumer<R> getRead() {
        return read;
    }

    public void setCreate(BooleanConsumer<R> create) {
        this.create = create;
    }
    public void setWrite(BooleanConsumer<R> write) {
        this.write = write;
    }
    public void setUpdate(BooleanConsumer<R> update) {
        this.update = update;
    }
    public void setDelete(BooleanConsumer<R> delete) {
        this.delete = delete;
    }
    public void setExit(BooleanConsumer<R> exit) {
        this.exit = exit;
    }
    public void setRead(BooleanConsumer<R> read) {
        this.read = read;
    }

    String typeName;
    ResourceID<T> id;
    public K context;

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public void setId(ResourceID<T> id) {
        this.id = id;
    }
    public void setContext(K context) {
        this.context = context;
    }

    public String getType() {
        return typeName;
    }
    public ResourceID<T> getId() {
        return id;
    }
    public K getContext() {
        return context;
    }

    @Override
    public boolean create() {
        return false;
    }
    @Override
    public boolean read() {
        return false;
    }
    @Override
    public boolean write() {
        return false;
    }
    @Override
    public boolean update() {
        return false;
    }
    @Override
    public boolean delete() {
        return false;
    }
}
