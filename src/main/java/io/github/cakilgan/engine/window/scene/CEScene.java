package io.github.cakilgan.engine.window.scene;


import io.github.cakilgan.cgraphics.c2d.C2DCamera;
import io.github.cakilgan.cgraphics.c2d.render.C2DBatchRenderer;
import io.github.cakilgan.cgraphics.c2d.render.debug.C2DDebugRenderer;
import io.github.cakilgan.engine.events.Event;
import io.github.cakilgan.engine.system.ecs.CEObjectSystem;
import io.github.cakilgan.engine.system.ecs.core.CEObject;
import io.github.cakilgan.engine.system.ecs.util.CEObjectID;
import io.github.cakilgan.physics.CEWorld;
import library.math.Vectors2D;
import org.jbox2d.common.Vec2;
import org.joml.Vector2f;

import java.util.List;

public abstract class CEScene extends CScene {
    List<Event<?>> events;
    public CEScene(String sceneName){
        super(sceneName);
        this.components.add(new CEObjectSystem(this));
    }
    public CEScene(){
        super();
        this.components.add(C2DCamera.createForBatch(this));
        this.components.add(new CEObjectSystem(this));
        this.components.add(new C2DBatchRenderer());
        this.components.add(new C2DDebugRenderer(this));
        this.components.add(new CEWorld(new Vectors2D(0,-9.81D)));

    }

    public CEWorld getWorld(){
        return (CEWorld) components.get(4);
    }
    public CEObjectSystem objectSystem(){
        return (CEObjectSystem) this.components.get(1);
    }
    public C2DBatchRenderer getRenderer(){
        return (C2DBatchRenderer) components.get(2);
    }
    public C2DDebugRenderer getDebugRenderer(){
        return (C2DDebugRenderer) components.get(3);
    }
    public void addObject(CEObject object){
        objectSystem().addObject(object);
    }
    public CEObject getObject(CEObjectID ID){
        return objectSystem().getObject(ID);
    }

    public void setCamera(C2DCamera camera){
        this.components.set(0,camera);
    }
    public C2DCamera getCamera(){
        return (C2DCamera) this.components.get(0);
    }

    @Override
    public void dispose() {
        super.dispose();
        components = null;
    }
}
