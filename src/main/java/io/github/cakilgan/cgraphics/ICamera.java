package io.github.cakilgan.cgraphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public abstract class ICamera {
    protected Matrix4f view,projection;
    protected Vector3f position,front,up;
    protected float zoom=45.0f,yaw=90.0f,pitch=0.0f;
    protected float zoomMin,zoomMax;
    protected float pitchMin,pitchMax;
    public float zNear;
    public float zFar;
    protected float sensivity;
    public abstract void init();
    public abstract void refresh();
    public abstract void render(CGShader shader);
    public void addYaw(float var){
        yaw+=var;
    }
    public void addPitch(float var){
        pitch+=var;
    }
    public void addZoom(float var){
        zoom+=var;
    }
    public float zoom() {
        return zoom;
    }
    public float pitch() {
        return pitch;
    }
    public float yaw() {
        return yaw;
    }
    public Vector3f front() {
        return front;
    }
    public Vector3f up() {
        return up;
    }
    public void setSensivity(float sensivity) {
        this.sensivity = sensivity;
    }
    public void setZoom(float zoom) {
        this.zoom = zoom;
    }
    public void setZoomMin(float zoomMin) {
        this.zoomMin = zoomMin;
    }
    public void setZoomMax(float zoomMax) {
        this.zoomMax = zoomMax;
    }
    public void setzNear(float zNear) {
        this.zNear = zNear;
    }
    public void setzFar(float zFar) {
        this.zFar = zFar;
    }
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
    public void setPitchMin(float pitchMin) {
        this.pitchMin = pitchMin;
    }
    public void setPitchMax(float pitchMax) {
        this.pitchMax = pitchMax;
    }
}
