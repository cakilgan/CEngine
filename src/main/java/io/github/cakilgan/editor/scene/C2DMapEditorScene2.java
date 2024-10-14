package io.github.cakilgan.editor.scene;

import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DQuadSprite;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.core.CakilganCore;
import io.github.cakilgan.core.io.FileHelper;
import io.github.cakilgan.cscriptengine.engines.MapFileScriptEngine;
import io.github.cakilgan.editor.core.MapCore;
import io.github.cakilgan.editor.core.SpriteSelector;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.map.C2DMap;
import io.github.cakilgan.engine.map.comp.C2DMapObject;
import io.github.cakilgan.engine.system.ecs.comp.CEOTransform;
import io.github.cakilgan.engine.system.ecs.comp.core.CEOButton;
import io.github.cakilgan.engine.system.ecs.comp.core.CEOObjectLock;
import io.github.cakilgan.engine.system.ecs.core.CEObject;
import io.github.cakilgan.engine.system.ecs.util.CEObjectID;
import io.github.cakilgan.engine.window.scene.CEScene;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class C2DMapEditorScene2  extends CEScene {
    MapCore core;
    public void setCore(MapCore core) {
        this.core = core;
    }

    CEObjectID selectorObjectID = new CEObjectID("selectorObject");
    C2DMap map;
    SpriteSelector spriteSelector;
    char[] chars = "1234567890abcdefghijklmoprstuvyzwx".toCharArray();
    @Override
    public void init() {

        spriteSelector = new SpriteSelector(this);
        spriteSelector.init(500,500,50,50);
        spriteSelector.addSpriteSheet("tileset","C:\\Users\\enesk\\IdeaProjects\\CEngine\\src\\main\\resources\\game\\assets\\snakegame\\atlas.png",new Vector2i(170,170));
        //spriteSelector.addSpriteSheet("C:\\Users\\enesk\\IdeaProjects\\CEngine\\src\\main\\resources\\engine\\assets\\textures\\player.png",new Vector2i(256,256));

        map = new C2DMap(new Vector2i(core.mapX,core.mapY),core.mapName);
        map.init(core.mapHeight,core.mapWidth,core.objHeight,core.objWidth);
        map.compile();
        map.setDebugDrawAll();
        map.useForObjectSetup(this);
        for (C2DMapObject mapObject : map.getMapObjects()) {
            mapObject.getObject().addComponent("button",new CEOButton());
        }
        CEObject selectorObject= new CEObject(selectorObjectID);
        selectorObject.addTransform(new CEOTransform(new Vector2f(map.getMapObjects()[map.getMapObjects().length-1].getPos().x,map.getMapObjects()[map.getMapObjects().length-1].getPos().y),new Vector2f(96,96),0));
        selectorObject.addComponent("buttonC",new CEOButton());
        C2DSprite sprite = C2DQuadSprite.createForBatch(null,new Vector2f(),new Vector2f(96,96),0);
        sprite.getColor().set(new Vector4f(1,0,0,1));
        selectorObject.addComponent("sprite", sprite);
        addObject(selectorObject);
        super.init();
    }

    C2DSprite clipboard;
    @Override
    public void update(double dt) {

        for (C2DMapObject mapObject : map.getMapObjects()) {
            if (((CEOButton)mapObject.getObject().getComponent("button")).canClick){
                getObject(selectorObjectID).getTransform().getPos().set(mapObject.getPos());
            }
        }
        if (CEngine.MOUSE.isLeftClicked()){
            if (CEngine.KEYBOARD.isKeyHolding(GLFW.GLFW_KEY_LEFT_CONTROL)){
                for (C2DMapObject mapObject : spriteSelector.getMap().getMapObjects()) {
                    if (((CEOButton)mapObject.getObject().getComponent("button")).canClick){
                        C2DSprite sprite = mapObject.getObject().getSprite("sprite").copyWithoutTransform();
                        clipboard = sprite;
                        for (C2DMapObject mapObject2 : map.getMapObjects()) {
                            if (clipboard!=null){
                                C2DSprite sprite2 = clipboard;
                                sprite2.setScale(mapObject2.getObject().getTransform().getScale());
                                mapObject2.getObject().getSprite("sprite").set(sprite2);
                            }
                            getObject(selectorObjectID).getTransform().getPos().set(mapObject2.getPos());
                        }
                    }
                }
            }
            for (C2DMapObject mapObject : map.getMapObjects()) {
                if (((CEOButton)mapObject.getObject().getComponent("button")).canClick){
                    if (clipboard!=null){
                        C2DSprite sprite = clipboard;
                        sprite.setScale(mapObject.getObject().getTransform().getScale());
                        mapObject.getObject().getSprite("sprite").set(sprite);
                    }
                    getObject(selectorObjectID).getTransform().getPos().set(mapObject.getPos());
                }
            }
            for (C2DMapObject mapObject : spriteSelector.getMap().getMapObjects()) {
                if (((CEOButton)mapObject.getObject().getComponent("button")).canClick){
                    C2DSprite sprite = mapObject.getObject().getSprite("sprite").copyWithoutTransform();
                    clipboard = sprite;
                }
            }
        }

        if (CEngine.KEYBOARD.isKeyJustPressed(GLFW.GLFW_KEY_ENTER)){
            FileHelper helper = CakilganCore.createHelper(new File(core.mapName+".c2dmap"));
            try {
                helper.resetNotAppend();
                helper.writeln("constructor {");
                helper.writeln(core.mapX+","+core.mapY+","+core.mapName);
                helper.writeln("}");
                helper.writeln("init {");
                helper.writeln(core.mapHeight+","+core.mapWidth+","+core.objHeight+","+core.objWidth);
                helper.writeln("}");
                helper.writeln("addSpriteSheet {");
                helper.writeln(spriteSelector.getSpriteSheetCode()+" "+spriteSelector.getSpriteSheetPath()+" "+spriteSelector.getScale().x+" "+spriteSelector.getScale().y+",");
                helper.writeln("}");
                helper.writeln("addPointer {");
                for (int i = 0; i < spriteSelector.getMap().getMapObjects().length; i++) {
                    if (!spriteSelector.getMap().getMapObjects()[i].getObject().getSprite("sprite").getPointer().isEmpty()){
                    helper.writeln(spriteSelector.getMap().getMapObjects()[i].getObject().getSprite("sprite").getPointer()+" "+spriteSelector.getSpriteSheetCode()+"["+i+"],");
                    }
                }
                helper.writeln("}");
                helper.writeln("parseMap {");
                helper.writeln("");
                int totalObjects = (core.mapHeight / core.objWidth) * (core.mapWidth / core.objWidth); // Toplam obje sayısı
                for (int i = 0; i < totalObjects; i++) {
                   C2DMapObject obj = map.getMapObjects()[i];

                    if (obj != null && obj.getObject() != null) {
                        C2DSprite sprite = obj.getObject().getSprite("sprite");
                        if (sprite != null) {
                            helper.write(sprite.getPointer());
                        }
                    }

                    // Satır sonuna gelindiğinde yeni satıra geç
                    if ((i + 1) % (core.mapWidth / core.objWidth) == 0) {
                        helper.writeln("");
                    }
                }

                helper.writeln("");
                helper.writeln("}");

                helper.exitAndSave();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        spriteSelector.update();
        super.update(dt);
    }

    @Override
    public String getName() {
        return "C2DMap Editor Scene 2";
    }
}
