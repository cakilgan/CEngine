package io.github.cakilgan.engine.input;

import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.system.CEComponent;
import io.github.cakilgan.engine.window.CEWindow;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class CEKeyCallback implements CEComponent {
    static CEWindow windowInstance = CEngine.WINDOW;
    static boolean[] isJustReleased = new boolean[500];
    static boolean[] isJustPressed = new boolean[500];
    static HashMap<Integer,State> keyStates= new HashMap<>();
    static boolean[] keys =new boolean[500];
    enum State{
        NONE,
        RELEASE,
        PRESS
    }
    @Override
    public void init() {
       glfwSetKeyCallback(windowInstance.getWindow(),CEKeyCallback::callback);
    }
    @Override
    public void loop() {

    }
    @Override
    public void dispose() {

    }
    private static void callback(long window, int key, int scancode, int action, int mods){
        if (action==GLFW_PRESS){
            if (key<0||key>500){
                return;
            }
            isJustReleased[key]=false;
            isJustPressed[key] = true;
            keyStates.put(key,State.PRESS);
            keys[key] = true;
        } else if (action==GLFW_RELEASE) {
            if (key<0||key>500){
                return;
            }
            if (isJustPressed[key]){
                isJustPressed[key] = false;
            }
            if (!isJustReleased[key]){
                isJustReleased[key] = true;
            }
            keyStates.put(key,State.RELEASE);
            keys[key] = false;
        } else {
            if (key<0||key>500){
                return;
            }
            isJustReleased[key] = false;
            keyStates.put(key,State.NONE);
            //keys[key] = false;
        }

    }
    public boolean isKeyHolding(int key){
        return keys[key];
    }
    public boolean isKeyPressed(int key){
        return keyStates.get(key)!= null ? keyStates.get(key).equals(State.PRESS) : false;
    }
    public boolean isKeyJustPressed(int key){
        if (isJustPressed[key]){
            isJustPressed[key] = false;
            return true;
        }else{
            return false;
        }

    }
    public boolean isKeyJustReleased(int key){
        if (isJustReleased[key]){
            isJustReleased[key] = false;
            return true;
        }else{
            return false;
        }
    }
    public void escapeExit(){
        if (isKeyHolding(GLFW_KEY_ESCAPE)){
            CEngine.ENGINE.setShouldExit(true);
        }
    }
}
