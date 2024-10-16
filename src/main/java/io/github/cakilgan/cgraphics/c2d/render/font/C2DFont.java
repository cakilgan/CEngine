package io.github.cakilgan.cgraphics.c2d.render.font;

import eu.hansolo.tilesfx.tools.CtxCornerRadii;
import io.github.cakilgan.cgraphics.c2d.render.C2DTexture;
import io.github.cakilgan.cgraphics.c2d.render.mesh.C2DGeo;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DQuadSprite;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSpriteSheet;
import io.github.cakilgan.cresourcemanager.resources.FileResource;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.system.ecs.comp.CEOTransform;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglef;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class C2DFont {
    public static C2DFont DEFAULT_FONT = new C2DFont(
            new C2DTexture(CEngine.RESOURCE_MANAGER.textures().getFile("default_font_2.png"),new Vector2i(1024,384)),
            64,
            64,
            16,
            6,
            0
    ," !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~");
    public static C2DFont DEFAULT_FONT_2 = new C2DFont(
            new C2DTexture(CEngine.RESOURCE_MANAGER.textures().getFile("def_font.png"),new Vector2i(126,49)),
            7,
            7,
            18,
            7,
            0
            ," !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~");
    C2DFontRenderer renderer;
    public void setRenderer(C2DFontRenderer renderer) {
        this.renderer = renderer;
    }
    public C2DFontRenderer getRenderer() {
        return renderer;
    }



    String forparse;
    C2DSpriteSheet spriteSheet;
    public C2DFont(C2DTexture texture,int charHeight,int charWidth,int xCount,int yCount,int spacing,String parseString){
        if (!texture.hasCreatedAlready()){
        texture.create();
        }
        spriteSheet = new C2DSpriteSheet(texture,charWidth,charHeight,xCount,yCount,spacing);
        this.forparse = parseString;
        parse(parseString);
        set(-30,0);
    }
    public void parse(String parse){
        char[] chars = parse.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            map.put(chars[i],i);
        }
    }
    float xSpacing=0,ySpacing=0;
    public void set(float xSpacing,float ySpacing){
        this.xSpacing = xSpacing;
        this.ySpacing = ySpacing;
    }
    public C2DSprite[] firstInit(){
        CEOTransform transform = getRenderer().getParent().getTransform();
        float xSpacing = renderer.fontSize.x+this.xSpacing;
        float ySpacing = renderer.fontSize.y+this.ySpacing;
        int xCount = (int) (transform.getScale().x/xSpacing);
        int yCount = (int) (transform.getScale().y/ySpacing);
        int loopCount = xCount*yCount;
        C2DSprite[] charSprites = new C2DSprite[loopCount];
        int spriteCount=-1;
        float xAdd= xSpacing,yAdd =ySpacing;
        float xMul = 0,yMul = 0;
        float xPos,yPos;
        Vector2f startPosition = new Vector2f(transform.getScale().x/2-xSpacing/2,transform.getScale().y/2-ySpacing/2); // Sol üstte 50,50'den başlasın

        for (int i = 0; i < loopCount; i++) {
            xPos = startPosition.x - (i % xCount * xAdd);
            yPos = startPosition.y - ((i / xCount) * yAdd);

            C2DSprite spriteCopy = getForChar(' ');
            C2DSprite sprite = C2DQuadSprite.createForBatch(
                    spriteCopy.getTexture(),
                    ((C2DGeo) spriteCopy.mesh()).getTexCoords(),
                    spriteCopy.getPosition(),
                    spriteCopy.getScale(),
                    spriteCopy.getRotation()
            );
            sprite.setZPos(getRenderer().getParent().getTransform().getZPos());
            sprite.setScale(renderer.fontSize);
            if (i!=0){
            sprite.setCompPos(new Vector2f(xPos+this.xSpacing, yPos-this.ySpacing));
            }else{
                sprite.setCompPos(new Vector2f(xPos, yPos-this.ySpacing));
            }
            charSprites[i] = sprite;
        }
        return charSprites;
    }
    public C2DSprite[] text(String text){
        int loopCount = text.length();
        C2DSprite[] charSprites = new C2DSprite[loopCount];
        char[] c = text.toCharArray();
        for (int i = 0; i < loopCount; i++) {
            charSprites[i] = getForChar(c[i]);
        }
        return charSprites;
    }
    public C2DSprite _ch(int i){
        return getSpriteSheet().getSprites().get(i);
    }
    HashMap<Character,Integer> map = new HashMap<>();
    public C2DSprite getForChar(char c){
        return _ch(map.get(c));
    }
    public C2DFont copy(){
        return new C2DFont(this.getSpriteSheet().getTexture(),spriteSheet.spriteHeight,spriteSheet.spriteWidth, spriteSheet.xCount, spriteSheet.yCount, spriteSheet.spacing, forparse);
    }
    public C2DSpriteSheet getSpriteSheet() {
        return spriteSheet;
    }
}
