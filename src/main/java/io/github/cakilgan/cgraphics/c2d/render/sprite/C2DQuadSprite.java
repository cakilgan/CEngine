package io.github.cakilgan.cgraphics.c2d.render.sprite;

import io.github.cakilgan.cgraphics.c2d.render.C2DTexture;
import io.github.cakilgan.cgraphics.c2d.render.mesh.C2DQuad;
import org.joml.Vector2f;

public class C2DQuadSprite extends C2DSprite{

    public C2DQuadSprite(C2DTexture texture) {
        super(texture, new C2DQuad());
    }



    public C2DQuadSprite(C2DTexture texture,Vector2f[] txCoords, Vector2f pos, Vector2f scale, float rotation) {
        super(texture, new C2DQuad(txCoords), pos, scale, rotation);
    }

    public C2DQuadSprite(C2DTexture texture, Vector2f pos, Vector2f scale, float rotation) {
        super(texture, new C2DQuad(), pos, scale, rotation);
    }
    public static C2DQuadSprite createForBatch(C2DTexture texture, Vector2f pos, Vector2f scale, float rotation){
        return new C2DQuadSprite(texture,C2DQuad.DEFAULT_TEXTURE_COORDINATES,pos,scale,rotation);
    }
    public static C2DQuadSprite createForBatch(C2DTexture texture,Vector2f[] texCoords, Vector2f pos, Vector2f scale, float rotation){
        return new C2DQuadSprite(texture,texCoords,pos,scale,rotation);
    }
}
