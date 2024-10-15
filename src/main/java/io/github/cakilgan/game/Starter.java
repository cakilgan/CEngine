package io.github.cakilgan.game;

import io.github.cakilgan.clogger.CLogger;
import io.github.cakilgan.clogger.format.Level;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.window.CEWindowConfig;
import io.github.cakilgan.game.comp.FlappyBirdGame;
import io.github.cakilgan.game.comp.MainGame;
import io.github.cakilgan.game.comp.SnakeGame;
import io.github.cakilgan.game.comp.TestUI;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

public class Starter {
    public static void main(String[] args) {
        //startSnakeGame(new String[]{"-logDebugs"});
        //startTestUI(args);
        startFlappyBirdGame();
    }
    public static void startTestUI(String[] args){
        CEngine.WINDOW.getConfig().title = "UI Test";
        CEngine.RESOURCE_MANAGER.setWriteLogs(true);
        CEngine.setParentPublicComponent(new TestUI());
        CEngine.ENGINE.run();
    }
    public static void startFlappyBirdGame(){
        CEngine.WINDOW.getConfig().title = "Flappy Bird";
        CEngine.RESOURCE_MANAGER.setWriteLogs(true);
        CEngine.setParentPublicComponent(new FlappyBirdGame());
        CEngine.ENGINE.run();
    }
    public static void startSnakeGame(String[] args){
        if (Arrays.asList(args).contains("-showOnlyErrors")){
            CLogger.setGlobalLogLevel(Level.ERROR);
        }
        if (Arrays.asList(args).contains("-logDebugs")){
            CLogger.setGlobalDebugMode(true);
        }else{
            CLogger.setGlobalDebugMode(false);
        }
        CEngine.WINDOW.getConfig().title = "Snake Game";
        CEngine.RESOURCE_MANAGER.setWriteLogs(true);
        CEngine.setParentPublicComponent(new SnakeGame());
        CEngine.ENGINE.run();
    }
}
