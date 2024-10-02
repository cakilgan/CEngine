package io.github.cakilgan.engine.math.time;

import io.github.cakilgan.engine.system.CEComponent;
import io.github.cakilgan.engine.system.RunOnFirstLineOfLoop;
import io.github.cakilgan.engine.system.RunOnLastLineOfWhileLoop;


import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class CETime implements CEComponent, RunOnFirstLineOfLoop, RunOnLastLineOfWhileLoop {
    double begin,end,dt,fps;
    public double getFps() {
        return fps;
    }
    public double getDt() {
        return dt;
    }
    private void setFps(double fps) {
        this.fps = fps;
    }
    @Override
    public void runFirstLoop() {
        begin = glfwGetTime();
    }
    @Override
    public void init() {

    }
    @Override
    public void loop() {

    }
    @Override
    public void dispose() {

    }

    @Override
    public void runLastLineOfWhileLoop() {
        end = glfwGetTime();
        dt = end-begin;
        setFps(1/dt);
        begin = end;
    }
}
