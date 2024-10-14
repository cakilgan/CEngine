package io.github.cakilgan.game.scene.snakeGame;

import io.github.cakilgan.cgraphics.c2d.render.C2DDebugDraw;
import io.github.cakilgan.cgraphics.c2d.render.font.C2DFont;
import io.github.cakilgan.cgraphics.c2d.render.font.C2DFontRenderer;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.system.ecs.comp.CEOCameraLock;
import io.github.cakilgan.engine.system.ecs.comp.CEOTransform;
import io.github.cakilgan.engine.system.ecs.comp.core.CEOButton;
import io.github.cakilgan.engine.system.ecs.core.CEObject;
import io.github.cakilgan.engine.system.ecs.util.CEObjectID;
import io.github.cakilgan.engine.window.scene.CEScene;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class SnakeGameMenuScene extends CEScene {
    CEObjectID buttonObjectID = new CEObjectID("buttonObject"),buttonObject2ID = new CEObjectID("buttonObject2");
    CEObjectID fontObjectID = new CEObjectID("fontObject");
    C2DFontRenderer fontRenderer,buttonFont,button2Font;
    @Override
    public void init() {
        getCamera().setCanMoveWithWASD(false);

        C2DFont font = C2DFont.DEFAULT_FONT_2.copy();
        font.set(-10f,0);

        CEObject buttonObject = new CEObject(buttonObjectID);
        buttonObject.addTransform(new CEOTransform(new Vector2f(-960,590),new Vector2f(100,50),0f));
        buttonObject.addComponent("debug",new C2DDebugDraw());
        buttonObject.addComponent("button",new CEOButton());
        buttonFont = new C2DFontRenderer(font,new Vector2f(32f,32f));
        buttonFont.setZpos(5f);
        buttonFont.setParent(buttonObject);
        buttonFont.init();
        ((CEOButton)buttonObject.getComponent("button")).setButtonText("Play");
        ((CEOButton)buttonObject.getComponent("button")).setFontRenderer(buttonFont);

        C2DSprite sprite = CEngine.RESOURCE_MANAGER.getSpriteSheet("snakeAtlas").getSprites().get(34).copyWithoutTransform();
        sprite.setScale(new Vector2f(100,50));
        sprite.setZPos(4f);
        sprite.setDontSyncZpos(true);
        buttonObject.addComponent("sprite",sprite);
        addObject(buttonObject);

        C2DFont font2 = C2DFont.DEFAULT_FONT_2.copy();
        font2.set(-10f,0);
        CEObject buttonObject2 = new CEObject(buttonObject2ID);
        buttonObject2.addTransform(new CEOTransform(new Vector2f(-960,490),new Vector2f(100,50),0f));
        buttonObject2.addComponent("debug",new C2DDebugDraw());
        buttonObject2.addComponent("button",new CEOButton());
        button2Font = new C2DFontRenderer(font2,new Vector2f(32f,32f));
        button2Font.setZpos(5f);
        button2Font.setParent(buttonObject2);
        button2Font.init();
        ((CEOButton)buttonObject2.getComponent("button")).setButtonText("Exit");
        ((CEOButton)buttonObject2.getComponent("button")).setFontRenderer(button2Font);

        C2DSprite sprite2 = CEngine.RESOURCE_MANAGER.getSpriteSheet("snakeAtlas").getSprites().get(34).copyWithoutTransform();
        sprite2.setScale(new Vector2f(100,50));
        sprite2.setZPos(4f);
        sprite2.setDontSyncZpos(true);
        buttonObject2.addComponent("sprite",sprite2);
        addObject(buttonObject2);


        font.set(-5f,0);
        fontRenderer = new C2DFontRenderer(font,new Vector2f(32f,32f));
        CEObject fontObject = new CEObject(fontObjectID);
        fontObject.addTransform(new CEOTransform(new Vector2f(),new Vector2f(32*200f,32f),0f));
        fontObject.addComponent("debug",new C2DDebugDraw());
        fontObject.addComponent("camLock",new CEOCameraLock(new Vector2f(-fontObject.getTransform().getScale().x/2f,1080-fontObject.getTransform().getScale().y/2f)));
        fontRenderer.setParent(fontObject);
        fontRenderer.init();
        addObject(fontObject);

        super.init();
    }

    @Override
    public void update(double dt) {
        CEOButton button = (CEOButton) getObject(this.buttonObjectID).getComponent("button");
        CEOButton button2 = (CEOButton) getObject(this.buttonObject2ID).getComponent("button");
        fontRenderer.setText("pos: "+button.mousePos.x+" "+button.mousePos.y+" canClick: "+button.canClick);
        if (button.canClick){
            buttonFont.setColorize(new Vector3f(1,0,0));
        }else{
            buttonFont.setColorize(new Vector3f(1,1,1));
        }
        if (button2.canClick){
            button2Font.setColorize(new Vector3f(1,0,0));
        }else{
            button2Font.setColorize(new Vector3f(1,1,1));
        }
        if (CEngine.MOUSE.isLeftClicked()){
            if (button.canClick){
                fontRenderer.setText("pos: "+button.mousePos.x+" "+button.mousePos.y+" canClick: "+button.canClick+" clicked");
                CEngine.SCENE.setScene(new SnakeGameScene());
            }
            if (button2.canClick){
             CEngine.ENGINE.setShouldExit(true);
            }
        }
        fontRenderer.update(dt);
        buttonFont.update(dt);
        button2Font.update(dt);
        super.update(dt);
    }

    @Override
    public String getName() {
        return "Snake Game Menu";
    }
}
