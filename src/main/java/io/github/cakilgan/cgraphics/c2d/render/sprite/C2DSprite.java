package io.github.cakilgan.cgraphics.c2d.render.sprite;

import io.github.cakilgan.cgraphics.CGShader;
import io.github.cakilgan.cgraphics.IBindUnbind;
import io.github.cakilgan.cgraphics.c2d.C2DCamera;
import io.github.cakilgan.cgraphics.c2d.render.C2DTexture;
import io.github.cakilgan.cgraphics.c2d.render.mesh.C2DGeo;
import io.github.cakilgan.cgraphics.c2d.render.mesh.C2DMesh;
import io.github.cakilgan.clogger.CLogger;
import io.github.cakilgan.clogger.system.CLoggerSystem;
import io.github.cakilgan.cresourcemanager.comp.ResourceID;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.system.ecs.comp.core.CEOComponent;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class C2DSprite extends CEOComponent implements IBindUnbind {
    String pointer = "";
    public void setPointer(String pointer) {
        this.pointer = pointer;
    }

    static CLogger LOGGER = CLoggerSystem.logger(C2DSprite.class);
    boolean flag;

    String direction = "right";
    public void setDirection(String direction) {
        this.direction = direction;
    }
    public String getDirection() {
        return direction;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public boolean isFlag() {
        return flag;
    }
    Vector2f compPos = new Vector2f();

    public void setCompPos(Vector2f compPos) {
        this.compPos = compPos;
    }
    public Vector2f getCompPos() {
        return compPos;
    }

    Vector2f position = new Vector2f();
    Vector2f scale = new Vector2f();
    float rotation = 0f;

    public void setRotation(float rotation) {
        this.rotation = rotation;
        setFlag(true);
    }
    public float getRotation() {
        return rotation;
    }

    public C2DSprite setZPos(float zpos){
        if (mesh instanceof C2DGeo){
            ((C2DGeo) mesh).setZpos(zpos);
        }
        setFlag(true);
        return this;
    }
    public void setPosition(Vector2f position) {
        this.position = position;
        setFlag(true);
    }
    public Vector2f getPosition() {
        return position;
    }

    public int getZPos(){
        return ((C2DGeo)mesh).getZPos();
    }
    public void setScale(Vector2f scale) {
        this.scale = scale;
        setFlag(true);
    }
    public Vector2f getScale() {
        return scale;
    }

    C2DMesh mesh;

    public C2DMesh mesh() {
        return mesh;
    }

    C2DTexture texture;
    public C2DSprite(C2DTexture texture,C2DMesh mesh){
        this.mesh = mesh;
        this.texture = texture;
        color =  new Vector4f(1,1,1,1);
    }
    Vector4f color;

    public Vector4f getColor() {
        return color;
    }
    public C2DSprite(C2DTexture texture,C2DMesh mesh,Vector2f pos,Vector2f scale,float rotation){
        this(texture,mesh);
        setPosition(pos);
        setScale(scale);
        setRotation(rotation);
        color =  new Vector4f(1,1,1,1);
    }
    boolean dontSyncZpos;
    public void setDontSyncZpos(boolean dontSyncZpos) {
        this.dontSyncZpos = dontSyncZpos;
    }

    public void init(){
        if (!dontSyncZpos){
        setZPos(getParent().getTransform().getZPos());
        }
        if (getTexture()!=null){
            getTexture().create();
        }
        getParent().getParent().getScene().getRenderer().add(this);
        super.init();
    }

    @Override
    public void bind() {
        texture.bind();
        mesh.bind();
    }
    public void defaultRender(){
        bind();
        render(CEngine.DEFAULT_SHADER);
        unbind();
    }
    public void render(CGShader shader){
        if (mesh instanceof C2DGeo){
            ((C2DGeo) mesh).uploadModel(position,scale,rotation,shader);
        }
        mesh.render();
    }

    public void setTexture(C2DTexture texture) {
        if (!ResourceID.getValue(this.texture.getPath().id.compareTo(texture.getPath().id))){
        this.texture = texture;
        this.texture.create();
        }else{
            LOGGER.warn("texture already added to sprite!");
        }
        setFlag(true);
    }

    @Override
    public void unbind() {
      mesh.unbind();
      texture.unbind();
    }
    public void dispose(){
        if (mesh instanceof C2DGeo){
            ((C2DGeo) mesh).dispose();
        }
        if (texture!=null){
        texture.unbind();
        }
        this.scale = null;
        this.position = null;
    }

    public C2DTexture getTexture() {
        return texture;
    }
    public Vector2f getScreenPosition(C2DCamera cam) {
        Vector2f spritePos = this.getPosition();
        Vector2f cameraPos = new Vector2f(cam.getPosition());

        float screenHeight = cam.getHeight();
        float screenX = -(spritePos.x - cameraPos.x); // X koordinatını ters çevir
        float screenY = screenHeight - (spritePos.y - cameraPos.y); // Y koordinatını ters çevir

        return new Vector2f(screenX, screenY);
    }

    boolean syncRotationWithObject = true;
    public void setSyncRotationWithObject(boolean syncRotationWithObject) {
        this.syncRotationWithObject = syncRotationWithObject;
    }

    @Override
    public void update(double dt) {
        if (syncRotationWithObject){
            rotation = getParent().getTransform().getRotation();
        }
        super.update(dt);
    }
    public C2DSprite copyWithoutTransform(){
        C2DSprite sprite = new C2DSprite(getTexture(),mesh);
        sprite.setDirection(direction);
        sprite.setPointer(pointer);
        return sprite;
    }

    public void set(C2DSprite sprite) {
        this.texture = sprite.texture;
        this.mesh = sprite.mesh;
        this.getColor().set(sprite.getColor());
        this.pointer = sprite.pointer;
        setDirection(sprite.getDirection());
    }

    public String getPointer() {
        return pointer;
    }
}
