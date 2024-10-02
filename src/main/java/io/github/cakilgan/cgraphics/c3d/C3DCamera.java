package io.github.cakilgan.cgraphics.c3d;

import io.github.cakilgan.cgraphics.CGShader;
import io.github.cakilgan.cgraphics.ICamera;
import io.github.cakilgan.engine.CEngine;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import static org.joml.Math.*;
import static org.joml.Math.toRadians;

public class C3DCamera extends ICamera {
    public C3DCamera(Vector3f pos){
        this.position = pos;
        setProjectionCode("projection");
        setViewCode("view");
    }
    @Override
    public void init() {
        setYaw(90.0f);
        setPitch(0.0f);

        setZoomMin(1.0f);
        setZoomMax(45.0f);

        setPitchMax(89.0f);
        setPitchMin(-89.0f);

        setzNear(0.1f);
        setzFar(100f);

        setSensivity(0.5f);
    }

    @Override
    public void refresh() {
        addZoom((float) -CEngine.MOUSE.getyScroll());
        if (zoom < zoomMin)
            setZoom(zoomMin);
        if (zoom > zoomMax)
            setZoom(zoomMax);


        addYaw((float) (CEngine.MOUSE.getxOffset()*sensivity));
        addPitch((float) (CEngine.MOUSE.getyOffset()*sensivity));

        if (pitch > pitchMax)
            setPitch(pitchMax);
        if (pitch < pitchMin)
            setPitch(pitchMin);

        Vector3f front = new Vector3f();
        front.x = cos(toRadians(yaw))*cos(toRadians(pitch));
        front.y = sin(toRadians(pitch));
        front.z = sin(toRadians(yaw))*cos(toRadians(pitch));
        this.front = front.normalize();

        up = new Vector3f(0.0f,1.0f,0.0f);
        float camSpeed = (float) (2.5f*CEngine.TIME.getDt());
    }

    String viewCode,projectionCode;

    public void setViewCode(String viewCode) {
        this.viewCode = viewCode;
    }
    public void setProjectionCode(String projectionCode) {
        this.projectionCode = projectionCode;
    }

    @Override
    public void render(CGShader shader) {
        view = new Matrix4f().identity();
        Vector3f add = new Vector3f();
        position.add(front,add);

        view = view.lookAt(position,add,up);

        projection = new Matrix4f().identity();
        projection.perspective(Math.toRadians(zoom), (float) CEngine.WINDOW.getConfig().w/CEngine.WINDOW.getConfig().h,zNear,zFar);

        shader._mat4f(viewCode,view);
        shader._mat4f(projectionCode,projection);
    }
}
