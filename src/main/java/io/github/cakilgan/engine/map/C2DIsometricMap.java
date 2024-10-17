package io.github.cakilgan.engine.map;

import io.github.cakilgan.cgraphics.c2d.render.C2DDebugDraw;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DQuadSprite;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.map.comp.C2DMapObject;
import io.github.cakilgan.engine.system.ecs.comp.CEOTransform;
import io.github.cakilgan.engine.system.ecs.core.CEObject;
import io.github.cakilgan.engine.system.ecs.util.CEObjectID;
import io.github.cakilgan.engine.window.scene.CEScene;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.HashMap;

public class C2DIsometricMap {
    public HashMap<String, C2DSprite> sprites;
    public C2DMapObject[] mapObjects;
    public Vector2i startPoint;
    public String mapName;
    public int mapWidth, mapHeight;
    public int tileWidth, tileHeight;
    public int xCount, yCount;
    public int totalTiles;

    public C2DIsometricMap(Vector2i startPoint, String mapName) {
        this.startPoint = startPoint;
        this.mapName = mapName;
        this.sprites = new HashMap<>();
    }

    public void init(int mapHeight, int mapWidth, int tileHeight, int tileWidth) {
        this.mapHeight = mapHeight;
        this.tileHeight = tileHeight;
        this.mapWidth = mapWidth;
        this.tileWidth = tileWidth;
        this.xCount = mapWidth / tileWidth;
        this.yCount = mapHeight / tileHeight;
        this.totalTiles = xCount * yCount;
    }

    public int xSpacing=0,ySpacing=0;
    public void compile() {
        CEOTransform transform = new CEOTransform(new Vector2f(startPoint.x, startPoint.y), new Vector2f(mapWidth, mapHeight), 0f);

        mapObjects = new C2DMapObject[totalTiles];
        float xPos, yPos;

        // İzometrik karo boyutlarıyla orantılı olacak şekilde düzenleme
        float halfTileWidth = tileWidth / 2f+xSpacing;
        float halfTileHeight = tileHeight / 2f+ySpacing;

        for (int i = 0; i < totalTiles; i++) {
            int row = i / xCount;
            int col = i % xCount;

            // İzometrik pozisyon hesaplama - boşluğu azaltmak için hesaplama düzenlendi
            xPos = (col - row) * halfTileWidth;
            yPos = (col + row) * halfTileHeight;

            mapObjects[i] = new C2DMapObject(
                    new CEObject(new CEObjectID(mapName + ":" + row + "-" + col)),
                    new Vector2i((int) xPos, (int) yPos),
                    new Vector2i(tileWidth, tileHeight)
            );
        }
    }


    public void objectSetup(CEScene scene) {
        for (C2DMapObject mapObject : mapObjects) {
            scene.addObject(mapObject.getObject());
        }
    }

    public void useForObjectSetup(CEScene scene) {
        for (C2DMapObject mapObject : mapObjects) {
            C2DSprite sprite = C2DQuadSprite.createForBatch(null, new Vector2f(), new Vector2f(tileWidth, tileHeight), 0f);
            sprite.getColor().set(0, 0, 0, 0);  // Şeffaf sprite
            mapObject.getObject().addComponent("sprite", sprite);
            scene.addObject(mapObject.getObject());
        }
    }

    public void colorize(Vector4f color) {
        for (C2DMapObject mapObject : mapObjects) {
            mapObject.getObject().getSprite("sprite").getColor().set(color);
        }
    }

    public void setDebugDrawAll() {
        for (C2DMapObject mapObject : mapObjects) {
            mapObject.getObject().addComponent("debug", new C2DDebugDraw().setColorcode(new Vector3f(1, 0, 1)));
        }
    }

    public void deleteDebugDrawAll() {
        for (C2DMapObject mapObject : mapObjects) {
            mapObject.getObject().removeComponent("debug");
        }
    }

    public void addSprite(C2DSprite sprite, String code) {
        sprites.put(code, sprite);
    }

    public void setSprite(int index, String compCode, String code) {
        if (index >= mapObjects.length) {
            System.out.println("Index out of bounds: " + index);
            return;
        }
        C2DSprite sprite = sprites.get(code).copyWithoutTransform();
        sprite.setScale(new Vector2f(tileWidth, tileHeight));
        mapObjects[index].getObject().addComponent(compCode, sprite);
    }
    public void setSpriteOnRun(int index, String compCode, String code) {
        if (index >= mapObjects.length) {
            System.out.println("Index out of bounds: " + index);
            return;
        }
        C2DSprite sprite = sprites.get(code).copyWithoutTransform();
        sprite.setScale(new Vector2f(tileWidth, tileHeight));
        if (getMapObjects()[index].getObject().getSprite("sprite") == null) {
            // Yeni bir sprite kopyası oluştur ve onu ekle
            ((CEScene)CEngine.SCENE.getCurrentScene()).getRenderer().add(sprite);
            getMapObjects()[index].getObject().addComponent("sprite", sprite);
        }else{
            getMapObjects()[index].getObject().getSprite("sprite").set(sprite);
        }
    }

    public void setSpriteAllOnRun(C2DSprite sprite,Vector2f scale){
        // Tüm nesneleri ters sırayla gez
        for (int i = getMapObjects().length - 1; i >= 0; i--) {
            // Sprite'ın kopyasını oluştur
            C2DSprite spriteCopy = sprite.copyWithoutTransform();

            // Kopyanın ölçeğini ayarla
            spriteCopy.setScale(scale);

            // Sprite'ı sahneye ekle
            // Eğer nesnenin mevcut sprite'ı yoksa, yeni sprite'ı ekle
            if (getMapObjects()[i].getObject().getSprite("sprite") == null) {
                // Yeni bir sprite kopyası oluştur ve onu ekle
                ((CEScene)CEngine.SCENE.getCurrentScene()).getRenderer().add(spriteCopy);
                getMapObjects()[i].getObject().addComponent("sprite", spriteCopy);
            }else{
                getMapObjects()[i].getObject().getSprite("sprite").set(spriteCopy);
            }
        }
    }
    public void setSpriteAll(C2DSprite sprite,Vector2f scale){
        for (int i = getMapObjects().length - 1; i >= 0; i--) {
            C2DSprite sprite1 = sprite.copyWithoutTransform();
            sprite1.setScale(scale);
            getMapObjects()[i].getObject().addComponent("sprite",sprite1);
        }
    }
    public C2DMapObject[] getMapObjects() {
        return mapObjects;
    }
}

