package io.github.cakilgan.game.scene.snakeGame;

import io.github.cakilgan.engine.system.ecs.comp.CEOTransform;
import io.github.cakilgan.engine.system.ecs.core.CEObject;
import io.github.cakilgan.engine.system.ecs.util.CEObjectID;
import org.joml.Vector2f;

public class SnakeBody {
    CEObjectID id;
    CEObject object;
    public SnakeBody(String id,Vector2f scale){
        this.id = new CEObjectID(id);
        object = new CEObject(this.id);
        object.addTransform(new CEOTransform(new Vector2f(),scale,0f));
    }

    public SnakeBody(String id,Vector2f scale,Vector2f pos){
        this.id = new CEObjectID(id);
        object = new CEObject(this.id);
        object.addTransform(new CEOTransform(pos,scale,0f));
    }
    public Vector2f scale(){
        return object.getTransform().getScale();
    }
    public Vector2f pos(){
        return object.getTransform().getPos();
    }
}
