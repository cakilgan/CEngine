package io.github.cakilgan.engine.window;

import io.github.cakilgan.clogger.CLogger;
import io.github.cakilgan.clogger.system.CLoggerSystem;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.system.CEComponent;
import io.github.cakilgan.engine.system.HasLogger;
import io.github.cakilgan.engine.system.RunOnLastLineOfWhileLoop;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.function.Consumer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class CEWindow implements CEComponent, RunOnLastLineOfWhileLoop, HasLogger {
    private long window;
    CEWindowConfig config;
    public CEWindowConfig getConfig() {
        return config;
    }

    public CEWindow(CEWindowConfig config) {
        this.config = config;
    }
    @Override
    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
            getLogger().exc(new IllegalStateException("Unable to Initialize GLFW"),"glfwInit: ");

        glfwDefaultWindowHints();
        for (CEWindowConfig.CEWindowHint hint : this.config.hints) {
            getLogger().debug("window hint: "+hint.hint+" set to "+hint.value);
            glfwWindowHint(hint.hint,hint.value);
        }
        window = glfwCreateWindow(config.w,
                config.h, config.title, NULL, NULL);
        getLogger().debug("window creating with "+config.w+" "+config.h+" "+config.title);
        if ( window == NULL )
            getLogger().exc(new RuntimeException("Failed to create the GLFW window"),"glfwCreateWindow: ");

        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }
        glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long l, int i, int i1) {
                config.w = i;
                config.h = i1;
            }
        });
        glfwSetFramebufferSizeCallback(window, new GLFWFramebufferSizeCallbackI() {
            @Override
            public void invoke(long l, int i, int i1) {
                glViewport(0,0,i,i1);
            }
        });
        glfwMakeContextCurrent(window);
        glfwSwapInterval(GLFW_TRUE);

        glfwShowWindow(window);
        GL.createCapabilities();
    }
    float r=0f,g=0f,b=0f,a=0f;
    public void setR(float r) {
        this.r = r;
    }
    public void setG(float g) {
        this.g = g;
    }
    public void setB(float b) {
        this.b = b;
    }
    public void setA(float a) {
        this.a = a;
    }
    public float getR() {
        return r;
    }
    public float getG() {
        return g;
    }
    public float getB() {
        return b;
    }
    public float getA() {
        return a;
    }
    public void set(float r,float g,float b,float a){
        setR(r);
        setG(g);
        setB(b);
        setA(a);
    }
    public void setBlack(){
        set(0f,0f,0f,0f);
    }
    public void setWhite(){
        set(1f,1f,1f,0f);
    }
    @Override
    public void loop() {
        glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
        glClearColor(r, g, b, a);
        CEngine.ENGINE.setShouldExit(glfwWindowShouldClose(window));
    }
    @Override
    public void dispose() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
    public long getWindow() {return window;}
    public void setConfig(CEWindowConfig config) {
        this.config = config;
    }

    @Override
    public void runLastLineOfWhileLoop() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    @Override
    public CLogger getLogger() {
        return CLoggerSystem.logger(CEWindow.class);
    }
}
