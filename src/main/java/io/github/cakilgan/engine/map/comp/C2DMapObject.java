package io.github.cakilgan.engine.map.comp;

import io.github.cakilgan.engine.system.ecs.comp.CEOTransform;
import io.github.cakilgan.engine.system.ecs.core.CEObject;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class C2DMapObject {
    CEObject object;
    Vector2i pos;
    Vector2i scale;

    public Vector2i getPos() {
        return pos;
    }
    public CEObject getObject() {
        return object;
    }

    public C2DMapObject(CEObject object, Vector2i pos,Vector2i scale){
        this.object = object;
        this.object.addTransform(new CEOTransform(new Vector2f(pos.x,pos.y),new Vector2f(scale.x,scale.y),0f));
        this.pos = pos;
    }
}
