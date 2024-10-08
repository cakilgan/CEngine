package io.github.cakilgan.engine.system;

import io.github.cakilgan.clogger.system.CLoggerSystem;
import io.github.cakilgan.core.serialization.CakilganComponent;
import io.github.cakilgan.clogger.CLogger;
import io.github.cakilgan.engine.window.scene.CScene;

public class CESceneSystem extends CakilganComponent implements CEComponent,Updatable{
    static CLogger LOGGER;
    static{
        LOGGER = CLoggerSystem.logger(CESceneSystem.class);
    }
    CScene scene;
    boolean sceneIsChanged = false;
    public CESceneSystem(CScene scene){
        this.scene = scene;
    }
    public CESceneSystem(){
        this.scene = new CScene() {
            @Override
            public void update(double dt) {
                super.update(dt);
            }
        };
    }
    public void setScene(CScene scene) {
        sceneIsChanged = true;
        this.scene = scene;
    }

    public CScene getCurrentScene() {
        return scene;
    }

    @Override
    public void init() {

    }
    @Override
    public void loop() {
        if (sceneIsChanged){
            LOGGER.warn("scene is changed to -> "+this.scene.getName());
            scene.init();
            sceneIsChanged = false;
        }
    }
    @Override
    public void dispose() {

    }
    @Override
    public void update(double dt) {
        if(dt>=0){
            scene.update(dt);
        }
    }
}
