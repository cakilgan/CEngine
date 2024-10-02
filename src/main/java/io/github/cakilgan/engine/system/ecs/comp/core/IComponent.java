package io.github.cakilgan.engine.system.ecs.comp.core;

public interface IComponent {
    void init();
    void update(double dt);
    void dispose();
}
