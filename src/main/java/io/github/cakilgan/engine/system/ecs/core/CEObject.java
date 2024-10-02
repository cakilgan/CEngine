package io.github.cakilgan.engine.system.ecs.core;

import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.engine.system.ecs.CEObjectSystem;
import io.github.cakilgan.engine.system.ecs.comp.CEOComponentSystem;
import io.github.cakilgan.engine.system.ecs.comp.CEOTransform;
import io.github.cakilgan.engine.system.ecs.comp.core.CEOComponent;
import io.github.cakilgan.engine.system.ecs.util.CEObjectID;
import io.github.cakilgan.physics.CPBody;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class CEObject implements IObject{
    CEObjectSystem parent;
    CEOComponentSystem ceoComponentSystem;

    public HashMap<String, CEOComponent> getComponents() {
        return ceoComponentSystem.getComponents();
    }

    CEObjectID ID;
    public CEObjectID getID() {
        return ID;

    }

    public void removeComponent(String code){
        ceoComponentSystem.removeComponent(code);
    }
    HashMap<String,CEOComponent> forAdd = new HashMap<>();
    public void addListedComp(String code,CEOComponent component){
        forAdd.put(code,component);
    }
    public void addComponent(String code, CEOComponent component){
        ceoComponentSystem.addComponent(code,component);
        component.setParent(this);
    }
    public CEOComponent getComponent(String code){
        return ceoComponentSystem.getComponent(code);
    }
    public void setParent(CEObjectSystem parent) {
        this.parent = parent;
    }
    public CEObjectSystem getParent() {
        return parent;
    }

    public CEObject(CEObjectID ID){
        this.ID = ID;
        this.ceoComponentSystem = new CEOComponentSystem();
    }
    @Override
    public void init() {
        ceoComponentSystem.init();
    }

    @Override
    public void update(double dt) {
        forAdd.forEach(new BiConsumer<String, CEOComponent>() {
            @Override
            public void accept(String s, CEOComponent component) {
                addComponent(s,component);
            }
        });
        ceoComponentSystem.update(dt);
    }

    @Override
    public void dispose() {
        ceoComponentSystem.dispose();
    }
    public void addTransform(CEOTransform transform){
        addComponent("transform",transform);
    }
    public CEOTransform getTransform(String code){
        return (CEOTransform) getComponent(code);
    }
    public CEOTransform getTransform(){
        return getTransform("transform");
    }
    public void setTransform(String code,CEOTransform transform){
        getTransform(code).set(transform);
    }
    public CPBody getBody(String body){
        return (CPBody) ceoComponentSystem.getComponent(body);
    }
    public C2DSprite getSprite(String code){
        return (C2DSprite) getComponent(code);
    }
    public <T>  T getComponent(String code,Class<T> tClass){
        return (T) getComponent(code);
    }
}
