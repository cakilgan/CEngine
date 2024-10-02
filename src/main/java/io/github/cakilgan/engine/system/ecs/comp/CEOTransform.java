package io.github.cakilgan.engine.system.ecs.comp;

import io.github.cakilgan.engine.system.ecs.comp.core.CEOComponent;
import io.github.cakilgan.engine.system.ecs.core.CEObject;
import org.joml.Vector2f;

public class CEOTransform extends CEOComponent {
    float zPos;

    public void setZPos(float zPos) {
        this.zPos = zPos;
    }

    public float getZPos() {
        return zPos;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    Vector2f pos;
    Vector2f scale;
    float rotation;
    public CEOTransform(Vector2f pos,Vector2f scale,float rotation){
        this.pos  = pos;
        this.scale = scale;
        this.rotation = rotation;
    }
    public Vector2f getScale() {
        return scale;
    }
    public float getRotation() {
        return rotation;
    }
    public Vector2f getPos() {
        return pos;
    }
    public void set(CEOTransform transform){
        this.pos = transform.pos;
        this.scale = transform.scale;
        this.rotation = transform.rotation;
        this.zPos = transform.zPos;
    }
}
