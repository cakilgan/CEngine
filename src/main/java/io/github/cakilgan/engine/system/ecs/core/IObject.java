package io.github.cakilgan.engine.system.ecs.core;

public interface IObject {
    void init();
    void update(double dt);
    void dispose();
}
