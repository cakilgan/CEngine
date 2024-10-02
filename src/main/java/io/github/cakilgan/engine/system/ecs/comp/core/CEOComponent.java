package io.github.cakilgan.engine.system.ecs.comp.core;

import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.system.ecs.core.CEObject;
import org.joml.Vector3f;

public class CEOComponent implements IComponent{
    CEObject parent;

    public void setParent(CEObject parent) {
        this.parent = parent;
    }
    public CEObject getParent() {
        return parent;
    }

    @Override
    public void init() {
    }

    @Override
    public void update(double dt) {

    }

    @Override
    public void dispose() {

    }

}
