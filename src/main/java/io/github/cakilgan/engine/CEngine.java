package io.github.cakilgan.engine;
import io.github.cakilgan.cgraphics.CGShader;
import io.github.cakilgan.clogger.CLogger;
import io.github.cakilgan.cresourcemanager.resources.*;
import io.github.cakilgan.cresourcemanager.resources.file.ShaderResource;
import io.github.cakilgan.engine.console.CEConsole;
import io.github.cakilgan.engine.input.CEKeyCallback;
import io.github.cakilgan.engine.input.CEMouseCallback;
import io.github.cakilgan.engine.logger.CEngineLogger;
import io.github.cakilgan.engine.logger.EngineContexts;
import io.github.cakilgan.engine.managment.CEResourceManager;
import io.github.cakilgan.engine.math.time.CETime;
import io.github.cakilgan.engine.system.CEComponent;
import io.github.cakilgan.engine.system.CEComponentSystem;
import io.github.cakilgan.engine.system.CESceneSystem;
import io.github.cakilgan.engine.system.HasLogger;
import io.github.cakilgan.engine.window.CEWindow;
import io.github.cakilgan.engine.window.CEWindowConfig;

import static org.lwjgl.glfw.GLFW.*;

public class CEngine implements CEComponent, HasLogger {
    private static final CEComponentSystem<CEngine> MAIN_SYSTEM = new CEComponentSystem<>(new CEngine());
    public static final CEngine ENGINE = MAIN_SYSTEM.getPARENT();
    public static final CEngineLogger LOGGER = MAIN_SYSTEM.addComponent(new CEngineLogger(null));
    static {
        LOGGER.setCurrentContext(EngineContexts.START);
        LOGGER.info(" ;");
        LOGGER.setCurrentContext(null);
    }
    public static final CEWindow WINDOW = MAIN_SYSTEM.addComponent(
            new CEWindow(new CEWindowConfig(1920,1080,"CEngine")
            .addHint(new CEWindowConfig.CEWindowHint(GLFW_VISIBLE,GLFW_FALSE))
            .addHint(new CEWindowConfig.CEWindowHint(GLFW_RESIZABLE,GLFW_TRUE))));
    public static final CEMouseCallback MOUSE = MAIN_SYSTEM.addComponent(new CEMouseCallback());
    public static final CEKeyCallback KEYBOARD = MAIN_SYSTEM.addComponent(new CEKeyCallback());
    public static final CEResourceManager RESOURCE_MANAGER = MAIN_SYSTEM.addComponent(new CEResourceManager());
    public static final CETime TIME = MAIN_SYSTEM.addComponent(new CETime());
    public static final CESceneSystem SCENE = MAIN_SYSTEM.addComponent(new CESceneSystem());
    public static final CEConsole CONSOLE = MAIN_SYSTEM.addComponent(new CEConsole());
    public static final CGShader DEFAULT_SHADER,BATCH_SHADER,DEBUG_SHADER;
    static {
        DirectoryResource RESOURCES = (DirectoryResource) CEngine.RESOURCE_MANAGER.getResource("main");
        ShaderResource shaderVertex = new ShaderResource(RESOURCES.getFile(RESOURCES.getDir("engine").getDir("data").getDir("shaders"), "vertex.vert"));
        ShaderResource shaderFragment = new ShaderResource(RESOURCES.getFile(RESOURCES.getDir("engine").getDir("data").getDir("shaders"), "fragment.frag"));
        DEFAULT_SHADER = MAIN_SYSTEM.addComponent(new CGShader(new ShaderProgramResource(shaderVertex,shaderFragment)));
        ShaderResource bshaderVertex = new ShaderResource(RESOURCES.getFile(RESOURCES.getDir("engine").getDir("data").getDir("shaders"), "bvertex.vert"));
        ShaderResource bshaderFragment = new ShaderResource(RESOURCES.getFile(RESOURCES.getDir("engine").getDir("data").getDir("shaders"), "bfragment.frag"));
        BATCH_SHADER =MAIN_SYSTEM.addComponent(new CGShader(new ShaderProgramResource(bshaderVertex,bshaderFragment)));
        ShaderResource dshaderVertex = new ShaderResource(RESOURCES.getFile(RESOURCES.getDir("engine").getDir("data").getDir("shaders"), "dvertex.vert"));
        ShaderResource dshaderFragment = new ShaderResource(RESOURCES.getFile(RESOURCES.getDir("engine").getDir("data").getDir("shaders"), "dfragment.frag"));
        DEBUG_SHADER =MAIN_SYSTEM.addComponent(new CGShader(new ShaderProgramResource(dshaderVertex,dshaderFragment)));
    }

    @Override
    public void init() {
        PUBLIC_SYSTEM.runFirstInit();
        MAIN_SYSTEM.runFirstInit();
        MAIN_SYSTEM.init();
        PUBLIC_SYSTEM.init();
        MAIN_SYSTEM.runLastInit();
    }
    @Override
    public void loop() {
        MAIN_SYSTEM.runFirstLoop();
        do {
            MAIN_SYSTEM.loop();
            PUBLIC_SYSTEM.loop();
            SCENE.update(CEngine.TIME.getDt());
            CEngine.MOUSE.endframe();
            MAIN_SYSTEM.runLastLineOfWhileLoop();
        }while (!isShouldExit());
        MAIN_SYSTEM.runLastLoop();
    }
    @Override
    public void dispose() {
        MAIN_SYSTEM.runFirstDispose();
        MAIN_SYSTEM.dispose();
        PUBLIC_SYSTEM.dispose();
        MAIN_SYSTEM.runLastDispose();
    }
    public void run(){
        try {
            init();
            loop();
        }catch (Exception e){
            LOGGER.exc(e,"An error occurred during execution ", EngineContexts.EXCEPTION);
        }finally {
            dispose();
        }
        LOGGER.runExit();
        System.exit(0);
    }
    boolean shouldExit;
    public void setShouldExit(boolean shouldExit) {
        this.shouldExit = shouldExit;
    }
    public boolean isShouldExit() {
        return shouldExit;
    }
    private static final CEComponentSystem<CEComponent> PUBLIC_SYSTEM = new CEComponentSystem<>(null);
    public static void setParentPublicComponent(CEComponent parentPublicComponent){
        PUBLIC_SYSTEM.setPARENT(parentPublicComponent);
        addPublicComponent(parentPublicComponent);
    }
    public static void addPublicComponent(CEComponent component){
        PUBLIC_SYSTEM.addComponent(component);
    }
    @Override
    public CLogger getLogger() {
        return CEngine.LOGGER;
    }
}
