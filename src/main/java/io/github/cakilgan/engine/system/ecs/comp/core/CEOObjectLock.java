package io.github.cakilgan.engine.system.ecs.comp.core;

import io.github.cakilgan.engine.system.ecs.core.CEObject;
import org.joml.Vector2f;

public class CEOObjectLock extends CEOComponent{
    CEObject to;
    public CEOObjectLock(CEObject to){
        this.to =to;
    }

    Vector2f compPos= new Vector2f();
    public void setCompPos(Vector2f compPos) {
        this.compPos = compPos;
    }
    @Override
    public void update(double dt) {
        getParent().getTransform().getPos().set(new Vector2f(
                to.getTransform().getPos().x+compPos.x,
                to.getTransform().getPos().y+compPos.y
        ));
        super.update(dt);
    }
}
