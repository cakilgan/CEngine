package io.github.cakilgan.cresourcemanager.comp;

@FunctionalInterface
public interface BooleanConsumer<T> {
    boolean accept(T obj);
}
