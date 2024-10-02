package io.github.cakilgan.engine.system.ecs;

import io.github.cakilgan.engine.system.CEComponent;
import io.github.cakilgan.engine.system.CESceneComponent;
import io.github.cakilgan.engine.system.ecs.core.CEObject;
import io.github.cakilgan.engine.system.ecs.util.CEObjectID;
import io.github.cakilgan.engine.window.scene.CEScene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CEObjectSystem implements CESceneComponent {
    HashMap<CEObjectID,CEObject> objects;

    public HashMap<CEObjectID, CEObject> getObjects() {
        return objects;
    }

    CEScene scene;
    public CEObjectSystem(CEScene scene){
        objects = new HashMap<>();
        this.scene = scene;
    }
    public CEScene getScene() {
        return scene;
    }
    public void addObject(CEObject object){
        object.setParent(this);
        this.objects.put(object.getID(),object);
    }
    public CEObject getObject(CEObjectID ID){
       return objects.get(ID);
    }
    @Override
    public void init() {
        for (CEObject object : objects.values()) {
            object.init();
        }
    }
    @Override
    public void update(double dt){
        for (CEObject object : objects.values()) {
            object.update(dt);
        }
    }
    @Override
    public void dispose() {
        for (CEObject object : objects.values()) {
            object.dispose();
        }
    }
}
