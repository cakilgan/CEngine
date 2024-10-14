package io.github.cakilgan.editor.scene;

import io.github.cakilgan.cgraphics.c2d.render.C2DBatchRenderer;
import io.github.cakilgan.cgraphics.c2d.render.font.C2DFont;
import io.github.cakilgan.cgraphics.c2d.render.font.C2DFontRenderer;
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
import io.github.cakilgan.engine.system.ecs.comp.CEOCameraLock;
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

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class C2DMapEditorScene2  extends CEScene {
    MapCore core;


    public void setCore(MapCore core) {
        this.core = core;
    }
    List<C2DFontRenderer> buttonFontRenderers= new ArrayList<>();
    List<CEObjectID> buttonObjects= new ArrayList<>();

    public C2DFontRenderer addButton(CEObjectID buttonObjectID, int textCount){
        C2DFont font = C2DFont.DEFAULT_FONT_2.copy();
        font.set(-10f,5f);
        CEObject buttonObject = new CEObject(buttonObjectID);
        buttonObject.addTransform(new CEOTransform(new Vector2f(-960,590),new Vector2f(32*textCount-((textCount*10-10)),50),0f));
        //buttonObject.addComponent("debug",new C2DDebugDraw());
        buttonObject.addComponent("button",new CEOButton());
        C2DFontRenderer buttonFont = new C2DFontRenderer(font,new Vector2f(32f,32f));
        buttonFont.setZpos(5f);
        buttonFont.setParent(buttonObject);
        buttonFont.init();
        ((CEOButton)buttonObject.getComponent("button")).setFontRenderer(buttonFont);

        C2DSprite sprite = CEngine.RESOURCE_MANAGER.getSpriteSheet("snakeAtlas").getSprites().get(34).copyWithoutTransform();
        sprite.setScale(new Vector2f(32*textCount-((textCount*10-10)),50));
        sprite.setZPos(4f);
        sprite.setDontSyncZpos(true);
        buttonObject.addComponent("sprite",sprite);
        addObject(buttonObject);
        buttonFontRenderers.add(buttonFont);
        buttonObjects.add(buttonObjectID);
        return buttonFont;
    }
    public CEOButton getButton(CEObjectID id){
        return (CEOButton) getObject(id).getComponent("button");
    }
    CEObjectID selectorObjectID = new CEObjectID("selectorObject");
    C2DMap map;
    SpriteSelector spriteSelector;
    char[] chars = "1234567890abcdefghijklmoprstuvyzwx".toCharArray();
    @Override
    public void init() {

        addButton(new CEObjectID("b_AddSpriteSheet"),"Add SpriteSheet".length());
        getButton(buttonObjects.get(0)).setButtonText("Add SpriteSheet");
        getObject(buttonObjects.get(0)).addComponent("camLock",new CEOCameraLock(new Vector2f(-230,1080-550)));

        addButton(new CEObjectID("b_Back"),"Back".length());
        getButton(buttonObjects.get(1)).setButtonText("Back");
        getObject(buttonObjects.get(1)).addComponent("camLock",new CEOCameraLock(new Vector2f(-230,1080-600)));


        addButton(new CEObjectID("b_Save"),"Save".length());
        getButton(buttonObjects.get(2)).setButtonText("Save");
        getObject(buttonObjects.get(2)).addComponent("camLock",new CEOCameraLock(new Vector2f(-230,1080-650)));


        spriteSelector = new SpriteSelector(this);
        spriteSelector.init(500,500,50,50);
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
        selectorObject.addTransform(new CEOTransform(new Vector2f(map.getMapObjects()[map.getMapObjects().length-1].getPos().x,map.getMapObjects()[map.getMapObjects().length-1].getPos().y),new Vector2f(core.objWidth,core.objHeight),0));
        selectorObject.addComponent("buttonC",new CEOButton());
        C2DSprite sprite = C2DQuadSprite.createForBatch(null,new Vector2f(),new Vector2f(core.objWidth,core.objHeight),0);
        sprite.getColor().set(new Vector4f(1,0,0,1));
        selectorObject.addComponent("sprite", sprite);
        addObject(selectorObject);
        super.init();
    }

    C2DSprite clipboard;
    @Override
    public void update(double dt) {

        if (clipboard!=null){
            getObject(selectorObjectID).getSprite("sprite").set(clipboard);
        }
        for (CEObjectID buttonObject : buttonObjects) {
            if (getButton(buttonObject).canClick){
                getButton(buttonObject).getFontRenderer().setColorize(new Vector3f(1,0,0));
            }else{
                getButton(buttonObject).getFontRenderer().setColorize(new Vector3f(1,1,1));
            }
        }
        for (C2DMapObject mapObject : map.getMapObjects()) {
            if (((CEOButton)mapObject.getObject().getComponent("button")).canClick){
                getObject(selectorObjectID).getTransform().getPos().set(mapObject.getPos());
            }
        }
        if (CEngine.KEYBOARD.isKeyHolding(GLFW.GLFW_KEY_LEFT_CONTROL)){
            if (CEngine.KEYBOARD.isKeyJustPressed(GLFW.GLFW_KEY_UP)){
                getCamera().zoom(0.9f);
            }
            if (CEngine.KEYBOARD.isKeyJustPressed(GLFW.GLFW_KEY_DOWN)){
                getCamera().zoom(1.1f);
            }
        }
        if (CEngine.MOUSE.isLeftClicked()){
            if (getButton(buttonObjects.get(0)).canClick){
                String input = JOptionPane.showInputDialog("Sprite Sheet Path?: ");
                if (input!=null){
                    try {
                        spriteSelector.addSpriteSheetOnRun("tileset",input,new Vector2i(170,170));
                    }catch (Exception e){
                        return;
                    }
                }
            }
            if (getButton(buttonObjects.get(1)).canClick){
                CEngine.SCENE.setScene(new C2DMapEditorScene1());
            }
            if (getButton(buttonObjects.get(2)).canClick){
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

                } catch (Exception e) {
                    return;
                }
            }
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
        for (C2DFontRenderer buttonFontRenderer : buttonFontRenderers) {
            buttonFontRenderer.update(dt);
        }
        spriteSelector.update();
        super.update(dt);
    }

    @Override
    public String getName() {
        return "C2DMap Editor Scene 2";
    }
}
