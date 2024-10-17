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
        glfwSetInputMode(CEngine.WINDOW.getWindow(), GLFW_LOCK_KEY_MODS, GLFW_TRUE);
       glfwSetKeyCallback(windowInstance.getWindow(),CEKeyCallback::callback);
    }
    @Override
    public void loop() {

    }
    @Override
    public void dispose() {

    }
    static boolean capsLockOn;
    public boolean isCapsLockOn() {
        return capsLockOn;
    }
    private static void setCapsLockOn(boolean capsLockOn) {
        CEKeyCallback.capsLockOn = capsLockOn;
    }

    private static void callback(long window, int key, int scancode, int action, int mods){
        if ((mods & GLFW_MOD_CAPS_LOCK) != 0) {
            setCapsLockOn(true);
        }else{
            setCapsLockOn(false);
        }
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
    public int activeLastKey(){
        int rtrn = 0;
        for (int i = 0; i < keys.length; i++) {
            if (keys[i]){
                rtrn = i;
            }
        }
        return rtrn;
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
