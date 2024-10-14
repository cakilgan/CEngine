package io.github.cakilgan.editor.core;

import io.github.cakilgan.cgraphics.c2d.render.C2DDebugDraw;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSpriteSheet;
import io.github.cakilgan.cresourcemanager.resources.FileResource;
import io.github.cakilgan.cresourcemanager.resources.file.TextureFileResource;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.map.C2DMap;
import io.github.cakilgan.engine.map.comp.C2DMapObject;
import io.github.cakilgan.engine.system.ecs.comp.CEOCameraLock;
import io.github.cakilgan.engine.system.ecs.comp.CEOTransform;
import io.github.cakilgan.engine.system.ecs.comp.core.CEOButton;
import io.github.cakilgan.engine.system.ecs.comp.core.CEOObjectLock;
import io.github.cakilgan.engine.system.ecs.core.CEObject;
import io.github.cakilgan.engine.system.ecs.util.CEObjectID;
import io.github.cakilgan.engine.window.scene.CEScene;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class SpriteSelector {
    CEScene scene;
    List<CEObjectID> sprites;
    public SpriteSelector(CEScene scene){
        this.scene = scene;
        sprites = new ArrayList<>();
    }
    public void addSprite(int count,C2DSprite sprite){
        sprite.setScale(new Vector2f(spriteWidth,spriteHeight));
        map.getMapObjects()[count].getObject().addComponent("sprite",sprite);
    }
    char[] chars = "1234567890abcdefghijklmoprstuvyzwxABCDEFGHIJKLMNOPRSTUVYZXW".toCharArray();
    String spriteSheetPath;

    public String getSpriteSheetPath() {
        return spriteSheetPath;
    }

    String spriteSheetCode;
    Vector2i scale;
    public String getSpriteSheetCode() {
        return spriteSheetCode;
    }

    public Vector2i getScale() {
        return scale;
    }

    public void addSpriteSheet(String code, String path, Vector2i scale){
        this.spriteSheetCode = code;
        this.scale = scale;
        this.spriteSheetPath = path;
        C2DSpriteSheet spriteSheet=
        C2DSpriteSheet.readFromTextureMetadata(new TextureFileResource(new FileResource(path),scale));
        for (int i = sprites.size(); i < spriteSheet.getSprites().size(); i++) {
            if (i>chars.length-1){
                continue;
            }
            spriteSheet.getSprites().get(i).setPointer(chars[i]+"");
            addSprite(i,spriteSheet.getSprites().get(i));
        }
    }
    C2DMap map;

    public C2DMap getMap() {
        return map;
    }

    CEObjectID frame;
    int width,height,spriteWidth,spriteHeight;
    public void init(int width,int height,int spriteWidth,int spriteHeight){
        this.spriteHeight = spriteHeight;
        this.spriteWidth = spriteWidth;
        map = new C2DMap(new Vector2i(),"spriteSelector");
        map.init(height,width,spriteHeight,spriteWidth);
        map.compile();
        map.setDebugDrawAll();
        map.useForObjectSetup(scene);

        CEObject object = new CEObject(frame);
        object.addTransform(new CEOTransform(new Vector2f(),new Vector2f(width,height),0));
        object.addComponent("debug",new C2DDebugDraw());
        object.addComponent("camLock",new CEOCameraLock(new Vector2f(-width/2f,1080-height/2f)));
        scene.addObject(object);
        for (C2DMapObject mapObject : map.getMapObjects()) {
            CEOObjectLock lock = new CEOObjectLock(object);
            lock.setCompPos(new Vector2f(mapObject.getPos().x,mapObject.getPos().y));
            mapObject.getObject().addComponent("objLock",lock);
            mapObject.getObject().addComponent("button",new CEOButton());
        }

        this.width = width;
        this.height = height;

    }
    public void update(){
    }
}
