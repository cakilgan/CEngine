package io.github.cakilgan.engine.system.ecs.comp;

import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.system.CEComponent;
import io.github.cakilgan.engine.system.ecs.comp.core.CEOComponent;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CEOComponentSystem {
    HashMap<String,CEOComponent> components;
    public CEOComponentSystem(){
        components =new HashMap<>();
    }

    public HashMap<String, CEOComponent> getComponents() {
        return components;
    }

    public void removeComponent(String code){
        components.remove(code);
    }
    public void addComponent(String code, CEOComponent component){
        components.put(code,component);
    }
    public CEOComponent getComponent(String code){
        return components.get(code);
    }
    public void init(){
        for (CEOComponent component : components.values()) {
            component.init();
        }
    }
    public void update(double dt){
        components.values().forEach(new Consumer<CEOComponent>() {
            @Override
            public void accept(CEOComponent component) {

                if (component instanceof C2DSprite){
                    if (components.containsKey("transform")){
                        CEOTransform transform = (CEOTransform) components.get("transform");
                        Vector2f newPos = new Vector2f(transform.pos.x,transform.pos.y);
                        newPos = newPos.add(((C2DSprite) component).getCompPos().x,((C2DSprite) component).getCompPos().y);
                        ((C2DSprite) component).setPosition(newPos);
                    }
                }
                component.update(dt);
            }
        });
    }
    public void dispose(){
        for (CEOComponent component : components.values()) {
            component.dispose();
        }
    }
}
