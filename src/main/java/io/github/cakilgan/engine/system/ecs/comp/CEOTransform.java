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
    public CEOTransform(){
        this.pos  = new Vector2f();
        this.scale = new Vector2f();
        this.rotation = 0f;
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
    public boolean isOn(Vector2f point){
        Vector2f bottomLeft = new Vector2f(pos.x - scale.x / 2, pos.y - scale.y / 2);

        Vector2f topRight = new Vector2f(pos.x + scale.x / 2, pos.y + scale.y / 2);

        if (point.x >= bottomLeft.x && point.x <= topRight.x &&
                point.y >= bottomLeft.y && point.y <= topRight.y) {
            return true;
        } else {
            return  false;
        }
    }
}
