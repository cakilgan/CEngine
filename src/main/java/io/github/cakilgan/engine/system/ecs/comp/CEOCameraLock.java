package io.github.cakilgan.engine.system.ecs.comp;

import io.github.cakilgan.engine.system.ecs.comp.core.CEOComponent;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class CEOCameraLock extends CEOComponent {
    Vector2f add;
    public void setAdd(Vector2f add) {
        this.add = add;
    }
    public Vector2f getAdd() {
        return add;
    }
    public CEOCameraLock(Vector2f add){
        this.add = add;
    }
}
