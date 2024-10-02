package io.github.cakilgan.cgraphics.c2d.render.sprite;

import io.github.cakilgan.cgraphics.c2d.render.C2DTexture;
import io.github.cakilgan.cgraphics.c2d.render.mesh.C2DGeo;
import io.github.cakilgan.cgraphics.c2d.render.mesh.C2DQuad;
import io.github.cakilgan.cresourcemanager.resources.FileResource;
import io.github.cakilgan.cresourcemanager.resources.file.TextureFileResource;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.system.CEComponent;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class C2DSpriteSheet {
    private C2DTexture texture;
    private List<C2DSprite> sprites;

    public void setSprites(List<C2DSprite> sprites) {
        this.sprites = sprites;
    }

    int spriteWidth,spriteHeight,xCount,yCount,spacing;
    public C2DSpriteSheet(C2DTexture texture, int spriteWidth, int spriteHeight, int xCount, int yCount, int spriteSpacing) {
        if (texture==null){
            return;
        }
        this.sprites = new ArrayList<>();
        this.texture = texture;
        this.texture.create();
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.xCount = xCount;
        this.yCount = yCount;
        this.spacing = spriteSpacing;
        float xSpace = 1.0f / ((float) texture.getScale().x / spriteWidth);
        float ySpace = 1.0f / ((float) texture.getScale().y / spriteHeight);
        Vector2f tc1, tc2, tc3, tc4;
        for (int i = 0; i < yCount; i++) {
            for (int i1 = 0; i1 < xCount; i1++) {
                tc1 = new Vector2f(xSpace + i1 * xSpace, 1.0f - ySpace - (ySpace * i));
                tc2 = new Vector2f(i1 * xSpace, 1.0f - ySpace - (ySpace * i));
                tc3 = new Vector2f(i1 * xSpace, 1f - (ySpace * i));
                tc4 = new Vector2f(xSpace + i1 * xSpace, 1f - (ySpace * i));
                Vector2f[] texcoords = new Vector2f[]{tc1, tc2, tc3, tc4};
                C2DSprite sprite = new C2DQuadSprite(texture, texcoords, new Vector2f(), new Vector2f(spriteWidth, spriteHeight), 0f);
                sprites.add(sprite);
            }
        }
    }

    public static C2DSpriteSheet readFromTextureMetadata(TextureFileResource textureFile) {
        textureFile.loadMetaData();
        C2DSpriteSheet rtrn = new C2DSpriteSheet(textureFile.getTexture(),
                textureFile.getIntData("spriteWidth").getData(),
                textureFile.getIntData("spriteHeight").getData(),
                textureFile.getIntData("xCount").getData(),
                textureFile.getIntData("yCount").getData(),
                textureFile.getIntData("spriteSpacing").getData());
        return rtrn;
    }

    public C2DSpriteSheet resize(Vector2f size) {
        for (C2DSprite sprite : sprites) {
            sprite.scale = size;
        }
        return this;
    }

    public C2DSpriteSheet repos(Vector2f compPos) {
        for (C2DSprite sprite : sprites) {
            sprite.setCompPos(compPos);
        }
        return this;
    }

    // Mirror method to flip the sprites horizontally or vertically
    public static C2DSpriteSheet mirror(C2DSpriteSheet temp,boolean horizontal, boolean vertical) {

        C2DSpriteSheet spriteSheet = new C2DSpriteSheet(temp.texture,temp.spriteWidth,temp.spriteHeight,temp.xCount,temp.yCount,temp.spacing);

        for (int i = 0; i < temp.sprites.size(); i++) {
            Vector2f[] texcoords = new Vector2f[]{
                    new Vector2f(((C2DGeo)temp.sprites.get(i).mesh()).getTexCoords()[0]),
                    new Vector2f(((C2DGeo)temp.sprites.get(i).mesh()).getTexCoords()[1]),
                    new Vector2f(((C2DGeo)temp.sprites.get(i).mesh()).getTexCoords()[2]),
                    new Vector2f(((C2DGeo)temp.sprites.get(i).mesh()).getTexCoords()[3]),
            };

            if (horizontal) {
                // Swap left and right texture coordinates
                Vector2f temp1 = texcoords[0];  // Top-right
                Vector2f temp2 = texcoords[3];  // Bottom-right
                texcoords[0] = texcoords[1];    // Top-left
                texcoords[3] = texcoords[2];    // Bottom-left
                texcoords[1] = temp1;           // Top-right
                texcoords[2] = temp2;           // Bottom-right
            }

            if (vertical) {
                // Swap top and bottom texture coordinates
                Vector2f temp1 = texcoords[0];  // Top-right
                Vector2f temp2 = texcoords[1];  // Top-left
                texcoords[0] = texcoords[3];    // Bottom-right
                texcoords[1] = texcoords[2];    // Bottom-left
                texcoords[3] = temp1;           // Top-right
                texcoords[2] = temp2;           // Top-left
            }

            ((C2DGeo)spriteSheet.sprites.get(i).mesh()).setTexCoords(texcoords);
        }
        return spriteSheet;
    }

    public List<C2DSprite> getSprites() {
        return sprites;
    }

    public C2DTexture getTexture() {
        return texture;
    }
}
