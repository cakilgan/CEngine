package io.github.cakilgan.engine.input;

import io.github.cakilgan.clogger.system.CLoggerSystem;
import io.github.cakilgan.core.serialization.CakilganComponent;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.system.CEComponent;
import io.github.cakilgan.engine.system.RunOnFirstLineOfWhileLoop;
import io.github.cakilgan.engine.system.RunOnLastLineOfLoop;
import io.github.cakilgan.engine.system.RunOnLastLineOfWhileLoop;
import io.github.cakilgan.engine.window.CEWindow;
import io.github.cakilgan.clogger.CLogger;
import org.joml.Vector3f;

import java.util.HashMap;

import static org.joml.Math.*;
import static org.joml.Math.toRadians;
import static org.lwjgl.glfw.GLFW.*;

public class CEMouseCallback extends CakilganComponent implements CEComponent, RunOnLastLineOfLoop, RunOnLastLineOfWhileLoop, RunOnFirstLineOfWhileLoop {
    private final static CLogger LOGGER;
    private final static CEMouseCallback INSTANCE = new CEMouseCallback();
    static {
        LOGGER = CLoggerSystem.logger(CEMouseCallback.class);
    }
    CEWindow windowInstance = CEngine.WINDOW;
    static double xOffset,yOffset,xpos,ypos,lastx,lasty,xScroll,yScroll;
    static boolean isDragging;
    static boolean[] isJustPressed = new boolean[10],keys = new boolean[10];
    static HashMap<Integer, CEKeyCallback.State> keyStates = new HashMap<>();
    private static void callbackMouseScroll(long l, double v, double v1) {

        LOGGER.debug("v: "+v);
        LOGGER.debug("v1: "+v1);
        xScroll = v;
        yScroll = v1;
    }


    static boolean fmouse =true;
    //glfw mouse callback
    private static void callbackMousePos(long l, double v, double v1) {
        xpos= (float) v;
        ypos= (float) v1;
        if (xpos!=lastx || ypos!=lasty){
            isDragging = keys[0];
        }
        if (fmouse){
            lastx = xpos;
            lasty = ypos;
            fmouse = false;
        }

        xOffset=xpos-lastx;
        yOffset=lasty-ypos;

        lastx = xpos;
        lasty = ypos;
    }
    private static void callbackMouseButton(long l, int i, int i1, int i2) {
        LOGGER.debug("l: "+l);
        LOGGER.debug("i: "+i);
        LOGGER.debug("i1: "+i1);
        LOGGER.debug("i2: "+i2);
        if (i1==GLFW_PRESS){
            if (i<0||i>10){
                return;
            }
            isJustPressed[i]=false;
            keyStates.put(i, CEKeyCallback.State.PRESS);
            keys[i] = true;
        } else if (i1==GLFW_RELEASE) {
            if (i<0||i>10){
                return;
            }
            if (!isJustPressed[i]){
                isJustPressed[i] = true;
            }
            keyStates.put(i, CEKeyCallback.State.RELEASE);
            keys[i] = false;
        } else {
            if (i<0||i>10){
                return;
            }
            isJustPressed[i] = false;
            keyStates.put(i, CEKeyCallback.State.NONE);
            keys[i] = false;
        }
        isDragging = keys[0];
    }
    @Override
    public void init() {
        glfwSetMouseButtonCallback(windowInstance.getWindow(),CEMouseCallback::callbackMouseButton);
        glfwSetCursorPosCallback(windowInstance.getWindow(),CEMouseCallback::callbackMousePos);
        glfwSetScrollCallback(windowInstance.getWindow(),CEMouseCallback::callbackMouseScroll);
    }
    @Override
    public void loop() {

    }
    @Override
    public void dispose() {


    }
    public boolean isPress(int key){
        return keys[key];
    }
    public boolean isLeftPress(){
        return isPress(0);
    }
    public boolean isRightPress(){
        return isPress(1);
    }
    public boolean isMiddlePress(){
        return isPress(2);
    }
    public boolean isClicked(int key){
        if (isJustPressed[key]){
            isJustPressed[key] = false;
            return true;
        }else{
            return false;
        }
    }
    public boolean isLeftClicked(){
        return isClicked(0);
    }
    public boolean isRightClicked(){
        return isClicked(1);
    }
    public boolean isMiddleClicked(){
        return isClicked(2);
    }
    public double getX(){
        return xpos;
    }
    public double getY(){
        return ypos;
    }
    public  double getDx() {
        return getX()-lastx;
    }
    public  double getDy(){
        return lasty-getY();
    }
    public boolean isDragging(){
        return isDragging;
    }

    public double getxOffset() {
        return xOffset;
    }

    public  double getyOffset() {
        return yOffset;
    }

    public double getxScroll() {
        return xScroll;
    }
    public double getyScroll() {
        return yScroll;
    }

    public void endframe(){
        xScroll = 0;
        yScroll = 0;
        xOffset = 0;
        yOffset = 0;
        lastx = xpos;
        lasty = ypos;
    }
    @Override
    public void runLastLoop() {

    }

    @Override
    public void runLastLineOfWhileLoop() {
    }

    @Override
    public void runFirstWhileLoop() {
    }
}
