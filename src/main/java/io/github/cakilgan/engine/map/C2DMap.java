package io.github.cakilgan.engine.map;

import io.github.cakilgan.cgraphics.c2d.render.C2DDebugDraw;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSpriteSheet;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.map.comp.C2DMapObject;
import io.github.cakilgan.engine.system.ecs.comp.CEOTransform;
import io.github.cakilgan.engine.system.ecs.core.CEObject;
import io.github.cakilgan.engine.system.ecs.util.CEObjectID;
import io.github.cakilgan.engine.window.scene.CEScene;
import io.github.cakilgan.physics.CPBody;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class C2DMap {
    public HashMap<String, C2DSpriteSheet> spriteSheets;
    HashMap<String,C2DSprite> sprites;
    C2DMapObject[] mapObjects;
    Vector2i startPoint;
    String mapName;
    int mapWidth,mapHeight;
    int objectWidth,objectHeight;
    int xCount,yCount;
    int count;
    public C2DMap(Vector2i startPoint,String mapName){
        this.startPoint = startPoint;
        this.mapName = mapName;
    }
    public void init(int mapHeight,int mapWidth,int objectHeight,int objectWidth){
        this.mapHeight = mapHeight;
        this.objectHeight = objectHeight;
        this.mapWidth = mapWidth;
        this.objectWidth = objectWidth;
        yCount = mapHeight/objectHeight;
        xCount = mapWidth/objectWidth;
        count = xCount*yCount;
        this.sprites = new HashMap<>();
        this.spriteSheets = new HashMap<>();
    }
    public void compile(){
        CEOTransform transform = new CEOTransform(new Vector2f(startPoint.x,startPoint.y),new Vector2f(mapWidth,mapHeight),0f);

        float xSpacing = objectWidth;
        float ySpacing = objectHeight;
        int xCount = (int) (transform.getScale().x/xSpacing);
        int yCount = (int) (transform.getScale().y/ySpacing);
        int loopCount = xCount*yCount;

        mapObjects = new C2DMapObject[loopCount];
        int spriteCount=-1;
        float xAdd= xSpacing,yAdd =ySpacing;
        float xMul = 0,yMul = 0;
        float xPos,yPos;
        Vector2f startPosition = new Vector2f(transform.getScale().x/2-xSpacing/2,transform.getScale().y/2-ySpacing/2); // Sol üstte 50,50'den başlasın

        for (int i = 0; i < loopCount; i++) {
            xPos = startPosition.x - (i % xCount * xAdd);
            yPos = startPosition.y - ((i / xCount) * yAdd);

            mapObjects[i] = new C2DMapObject(new CEObject(new CEObjectID(mapName+":"+yPos+"-"+xPos)),new Vector2i((int) xPos, (int) yPos),new Vector2i(objectWidth,objectHeight));
            objectIDHashMap.put(i,mapObjects[i].getObject().getID());
        }


    }
    public void objectSetup(CEScene scene){
        for (C2DMapObject mapObject : mapObjects) {
            scene.addObject(mapObject.getObject());
        }
    }
    boolean debugDrawAll;

    public boolean isDebugDrawAll() {
        return debugDrawAll;
    }

    public void setDebugDrawAll(){
        int i = 0;
        for (C2DMapObject mapObject : mapObjects) {
            mapObject.getObject().addComponent("debug",new C2DDebugDraw().setColorcode(new Vector3f(1,0,1)));
            i++;
        }
        debugDrawAll = true;
    }
    public void deleteDebugDrawAll(){
        int i = 0;
        for (C2DMapObject mapObject : mapObjects) {
            i++;
            mapObject.getObject().removeComponent("debug");
        }
        debugDrawAll = false;
    }
    boolean flag;
    public void updatePosition(int x,int y){
        for (C2DMapObject mapObject : mapObjects) {
            mapObject.getPos().set(x,y);
        }
        flag = true;
    }
    public void update(CEScene scene){
        if (flag){
            objectIDHashMap.forEach(new BiConsumer<Integer, CEObjectID>() {
                @Override
                public void accept(Integer integer, CEObjectID ceObjectID) {
                   Vector2i pos =  mapObjects[integer].getPos();
                   scene.getObject(ceObjectID).getTransform().getPos().add(new Vector2f(pos.x,pos.y));
                }
            });
            flag = false;
        }
    }
    HashMap<Integer,CEObjectID> objectIDHashMap = new HashMap<>();
    private String decompile(Vector2i pos){
        return mapName+":"+pos.y+"-"+pos.x;
    }
    public void addBody(int index,String compCode,CPBody<?> body){
        mapObjects[index].getObject().addComponent(compCode,body);
    }
    public void  addSprite(C2DSprite sprite,String code){
        sprites.put(code,sprite);
    }
    public void setSprite(int index,String compCode,String code){
        C2DSprite sprite = sprites.get(code).copyWithoutTransform();
        sprite.setScale(new Vector2f(objectWidth,objectHeight));
        mapObjects[index].getObject().addComponent(compCode,sprite);
    }

    public C2DMapObject[] getMapObjects() {
        return mapObjects;
    }
}
