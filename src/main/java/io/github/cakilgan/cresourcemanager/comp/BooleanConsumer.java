package io.github.cakilgan.cresourcemanager.comp;

import java.util.function.Consumer;
@FunctionalInterface
public interface BooleanConsumer<T> {
    boolean accept(T obj);
}
