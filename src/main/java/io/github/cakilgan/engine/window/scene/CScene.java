package io.github.cakilgan.engine.window.scene;

import io.github.cakilgan.clogger.CLogger;
import io.github.cakilgan.clogger.system.CLoggerSystem;
import io.github.cakilgan.engine.system.CESceneComponent;
import io.github.cakilgan.engine.system.Updatable;

import java.util.ArrayList;
import java.util.List;

public class CScene implements Updatable {
    public CLogger LOGGER;
    public List<CESceneComponent> components;
    protected String sceneName;
    public CScene(String sceneName){
        this.sceneName = sceneName;
        this.components = new ArrayList<>();
        this.LOGGER = CLoggerSystem.logger(getClass());
    }
    public CScene(){
        this.components = new ArrayList<>();
        this.LOGGER = CLoggerSystem.logger(getClass());
        this.sceneName = "nullScene";
    }
    @Override
    public void update(double dt) {
        for (CESceneComponent component : components) {
            component.update(dt);
        }
    }
    public void init(){
        for (CESceneComponent component : components) {
            component.init();
        }
    }
    public void dispose(){
        for (CESceneComponent component : components) {
            component.dispose();
        }
    }
    public String getName() {
        return sceneName;
    }
}
