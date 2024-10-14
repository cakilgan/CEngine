package io.github.cakilgan.cgraphics.c2d.render.font;

import io.github.cakilgan.cgraphics.c2d.render.C2DTexture;
import io.github.cakilgan.cgraphics.c2d.render.mesh.C2DGeo;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.system.ecs.comp.core.CEOComponent;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class C2DFontRenderer extends CEOComponent {

    C2DFontComponent component;
    public void setText(String text) {
        component.set(text);
    }
    public String getText(){
        return component.getComponent();
    }

    C2DFont font;
    Vector2f fontSize;
    public Vector2f getFontSize() {
        return fontSize;
    }

    public C2DFontRenderer(C2DFont font, Vector2f fontSize){
        this.font = font;
        this.font.setRenderer(this);
        this.fontSize = fontSize;
        this.component = new C2DFontComponent();
    }

    float zpos = 0f;

    public void setZpos(float zpos) {
        this.zpos = zpos;
    }

    Vector3f colorize = new Vector3f(1,1,1);
    public void setColorize(Vector3f colorize) {
        this.colorize = colorize;
    }

    int initLength;
    @Override
    public void init() {
        C2DSprite[] sprites = font.firstInit();
        for (int i = 0; i < sprites.length; i++) {
            sprites[i].setZPos(zpos);
            sprites[i].setDontSyncZpos(true);
            sprites[i].getColor().set(colorize.x,colorize.y,colorize.z,1);
            getParent().addComponent("font["+i+"]",sprites[i]);
        }
        initLength = sprites.length;
        super.init();
    }

    @Override
    public void update(double dt) {
        if (this.component.component!=null){
            C2DSprite[] sprites = font.text(component.component);
            for (int i = 0; i < sprites.length; i++) {
                C2DSprite spriteCopy = sprites[i];
                if (spriteCopy==null){
                    CEngine.LOGGER.info("sprite is null! "+i);
                }
                C2DSprite sprite = (C2DSprite) getParent().getComponent("font["+i+"]");
                if (sprite!=null){
                    sprite.getColor().set(colorize.x,colorize.y,colorize.z,1);
                ((C2DGeo)sprite.mesh()).setTexCoords(((C2DGeo)spriteCopy.mesh()).getTexCoords());
                }
            }
            if (sprites.length<initLength){
                for (int i = sprites.length; i < initLength; i++) {
                    C2DSprite spriteCopy = font.spriteSheet.getSprites().get(0);
                    C2DSprite sprite = (C2DSprite) getParent().getComponent("font["+i+"]");
                    sprite.getColor().set(colorize.x,colorize.y,colorize.z,1);
                    ((C2DGeo)sprite.mesh()).setTexCoords(((C2DGeo)spriteCopy.mesh()).getTexCoords());
                }
            }
        }
        super.update(dt);
    }
}
