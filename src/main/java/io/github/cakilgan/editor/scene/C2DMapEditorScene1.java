package io.github.cakilgan.editor.scene;

import io.github.cakilgan.cgraphics.c2d.render.C2DDebugDraw;
import io.github.cakilgan.cgraphics.c2d.render.font.C2DFont;
import io.github.cakilgan.cgraphics.c2d.render.font.C2DFontRenderer;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.editor.core.MapCore;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.system.ecs.comp.CEOCameraLock;
import io.github.cakilgan.engine.system.ecs.comp.CEOTransform;
import io.github.cakilgan.engine.system.ecs.comp.core.CEOButton;
import io.github.cakilgan.engine.system.ecs.core.CEObject;
import io.github.cakilgan.engine.system.ecs.util.CEObjectID;
import io.github.cakilgan.engine.window.scene.CEScene;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_0;

public class C2DMapEditorScene1 extends CEScene {

    public C2DFontRenderer addButton(CEObjectID buttonObjectID,int textCount){
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
    public C2DFontRenderer addTextArea(CEObjectID textAreaObjectID,int textCount){
        C2DFont font = C2DFont.DEFAULT_FONT_2.copy();
        font.set(-7f,5f);
        CEObject buttonObject = new CEObject(textAreaObjectID);
        buttonObject.addTransform(new CEOTransform(new Vector2f(-960,590),new Vector2f(32*textCount-((textCount*10-10)),50),0f));
        //buttonObject.addComponent("debug",new C2DDebugDraw());
        C2DFontRenderer buttonFont = new C2DFontRenderer(font,new Vector2f(32f,32f));
        buttonFont.setZpos(5f);
        buttonFont.setParent(buttonObject);
        buttonFont.init();

        addObject(buttonObject);
        buttonFontRenderers.add(buttonFont);
        return buttonFont;
    }
    public CEOButton getButton(CEObjectID id){
        return (CEOButton) getObject(id).getComponent("button");
    }
    List<C2DFontRenderer> buttonFontRenderers = new ArrayList<>();
    List<CEObjectID> buttonObjects = new ArrayList<>();
    CEObjectID createMapButton = new CEObjectID("b_CreateMap");
    CEObjectID createMapTextArea = new CEObjectID("txA_CreateMap"),createMapNameText = new CEObjectID("txa_CreateMapName");
    CEObjectID mapWidthTextArea = new CEObjectID("txA_MapWidth"),mapWidthText = new CEObjectID("txa_MapWidthValue");
     C2DFontRenderer createMapTextAreaFontRenderer,mapWidthTextAreaFontRenderer;

    StringBuilder renderText = new StringBuilder();
    public void c(int code,String text){
        if (CEngine.KEYBOARD.isKeyJustPressed(code)){
            if (CEngine.KEYBOARD.isCapsLockOn()){
                renderText.append(text.toUpperCase(Locale.ROOT));
            }else{
                renderText.append(text);
            }
        }
    }
    public C2DFontRenderer addATextAreaWithATag(String tag,Vector2f camLock,float xAddToName){
      CEObjectID textArea = new CEObjectID("txA_"+tag);
      CEObjectID textAreaName = new CEObjectID("txA(Name)_"+tag);
      C2DFontRenderer  createMapTextAreaFontRenderer = addTextArea(textArea,50);
        getObject(textArea).addComponent("camLock",new CEOCameraLock(
                new Vector2f(
                        camLock.x,//-getObject(createMapButton).getTransform().getScale().x/2f-675,
                        camLock.y//1080-getObject(createMapButton).getTransform().getScale().y/2f-50
                )
        ));
        addTextArea(textAreaName,tag.length()+1).setText(tag);
        getObject(textAreaName).addComponent("camLock",new CEOCameraLock(
                new Vector2f(
                        -getObject(createMapButton).getTransform().getScale().x/2f+xAddToName,
                        camLock.y
                )
        ));
        this.textAreas.add(createMapTextAreaFontRenderer);
        return createMapTextAreaFontRenderer;
    }
    List<C2DFontRenderer> textAreas = new ArrayList<>();
    CEObjectID textAreaHolderID = new CEObjectID("textAreaHolder");
    @Override
    public void init() {
        getCamera().setCanMoveWithWASD(false);
        CEObject textAreaHolder = new CEObject(textAreaHolderID);
        textAreaHolder.addTransform(new CEOTransform(new Vector2f(),new Vector2f(32*50-((50*10-10)),50),0f));
        textAreaHolder.addComponent("debug",new C2DDebugDraw().setColorcode(new Vector3f(1,0,0)));
        addObject(textAreaHolder);

        addButton(createMapButton,10);
        getButton(createMapButton).setButtonText("Create Map");
        getObject(createMapButton).addComponent("camLock",
                new CEOCameraLock(
                        new Vector2f(
                                -getObject(createMapButton).getTransform().getScale().x/2f,
                                1080-getObject(createMapButton).getTransform().getScale().y/2f
                        )));

        this.addATextAreaWithATag("Map Name: ",new Vector2f(-810,1080-75),-10f);
        this.addATextAreaWithATag("Start X: ",new Vector2f(-780-10,1080-125),0f);
        this.addATextAreaWithATag("Start Y: ",new Vector2f(-780-10,1080-175),0f);
        this.addATextAreaWithATag("Map Width: ",new Vector2f(-820-10,1080-225),-20f);
        this.addATextAreaWithATag("Map Height: ",new Vector2f(-840-10,1080-275),-30f);
        this.addATextAreaWithATag("Object Width: ",new Vector2f(-880-10,1080-325),-50f);
        this.addATextAreaWithATag("Object Height: ",new Vector2f(-900-10,1080-375),-60f);
        super.init();
    }
    int focus = 0;
    public void checkInputs(){
        c(GLFW.GLFW_KEY_A,"a");
        c(GLFW.GLFW_KEY_B,"b");
        c(GLFW.GLFW_KEY_C,"c");
        c(GLFW.GLFW_KEY_D,"d");
        c(GLFW.GLFW_KEY_E,"e");
        c(GLFW.GLFW_KEY_F,"f");
        c(GLFW.GLFW_KEY_G,"g");
        c(GLFW.GLFW_KEY_H,"h");
        c(GLFW.GLFW_KEY_I,"i");
        c(GLFW.GLFW_KEY_J,"j");
        c(GLFW.GLFW_KEY_K,"k");
        c(GLFW.GLFW_KEY_L,"l");
        c(GLFW.GLFW_KEY_M,"m");
        c(GLFW.GLFW_KEY_N,"n");
        c(GLFW.GLFW_KEY_O,"o");
        c(GLFW.GLFW_KEY_P,"p");
        c(GLFW.GLFW_KEY_R,"r");
        c(GLFW.GLFW_KEY_S,"s");
        c(GLFW.GLFW_KEY_T,"t");
        c(GLFW.GLFW_KEY_U,"u");
        c(GLFW.GLFW_KEY_V,"v");
        c(GLFW.GLFW_KEY_Y,"y");
        c(GLFW.GLFW_KEY_Z,"z");
        c(GLFW.GLFW_KEY_W,"w");
        c(GLFW_KEY_1,"1");
        c(GLFW_KEY_2,"2");
        c(GLFW_KEY_3,"3");
        c(GLFW_KEY_4,"4");
        c(GLFW_KEY_5,"5");
        c(GLFW_KEY_6,"6");
        c(GLFW_KEY_7,"7");
        c(GLFW_KEY_8,"8");
        c(GLFW_KEY_9,"9");
        c(GLFW_KEY_0,"0");
        c(GLFW.GLFW_KEY_SPACE," ");
        if (CEngine.KEYBOARD.isKeyJustPressed(GLFW.GLFW_KEY_BACKSPACE)){
            if (!renderText.isEmpty()){
                renderText = new StringBuilder(renderText.substring(0, renderText.length() - 1));
            }
        }
        if (CEngine.KEYBOARD.isKeyHolding(GLFW.GLFW_KEY_LEFT_SHIFT)||CEngine.KEYBOARD.isKeyHolding(GLFW.GLFW_KEY_RIGHT_SHIFT)){
            if (CEngine.KEYBOARD.isKeyJustPressed(GLFW.GLFW_KEY_1)){
                renderText.append("!");
            };
        }
    }
    @Override
    public void update(double dt) {
        if (focus>textAreas.size()-1||focus<0){
            focus = 0;
        }
        getObject(textAreaHolderID).getTransform().getPos().set(textAreas.get(focus).getParent().getTransform().getPos());
        renderText = new StringBuilder(textAreas.get(focus).getText());
        checkInputs();
        textAreas.get(focus).setText(renderText.toString());

        if (CEngine.KEYBOARD.isKeyJustPressed(GLFW_KEY_DOWN)){
            focus++;
        }
        if (CEngine.KEYBOARD.isKeyJustPressed(GLFW_KEY_UP)){
            focus--;
        }
        if (CEngine.MOUSE.isLeftClicked()){
            for (CEObjectID buttonObject : buttonObjects) {
                if (getButton(buttonObject).canClick){
                    MapCore core = new MapCore();
                    String mapName;
                    int mapX,mapY;
                    int mapWidth,mapHeight;
                    int objWidth,objHeight;
                    mapName = textAreas.get(0).getText();
                    mapX = Integer.parseInt(textAreas.get(1).getText());
                    mapY = Integer.parseInt(textAreas.get(2).getText());
                    mapWidth = Integer.parseInt(textAreas.get(3).getText());
                    mapHeight = Integer.parseInt(textAreas.get(4).getText());
                    objWidth = Integer.parseInt(textAreas.get(5).getText());
                    objHeight = Integer.parseInt(textAreas.get(6).getText());
                    core.set(mapName,mapX,mapY,mapWidth,mapHeight,objWidth,objHeight);
                    C2DMapEditorScene2 scene2 = new C2DMapEditorScene2();
                    scene2.setCore(core);
                    CEngine.SCENE.setScene(scene2);
                }
            }
        }
        if (CEngine.KEYBOARD.isKeyJustPressed(GLFW_KEY_ENTER)){
            textAreas.get(0).setText("MapTest");
            textAreas.get(1).setText("0");
            textAreas.get(2).setText("0");
            textAreas.get(3).setText("4800");
            textAreas.get(4).setText("2400");
            textAreas.get(5).setText("96");
            textAreas.get(6).setText("96");
        }

        for (C2DFontRenderer textArea : textAreas) {
            textArea.update(dt);
        }
        for (C2DFontRenderer buttonFontRenderer : buttonFontRenderers) {
            buttonFontRenderer.update(dt);
        }
        super.update(dt);
    }

    @Override
    public String getName() {
        return "C2DMap Editor Scene 1";
    }
}
