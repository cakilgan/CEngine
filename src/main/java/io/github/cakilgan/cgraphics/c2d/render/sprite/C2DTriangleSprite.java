package io.github.cakilgan.cgraphics.c2d.render.sprite;

import io.github.cakilgan.cgraphics.c2d.render.C2DTexture;
import io.github.cakilgan.cgraphics.c2d.render.mesh.C2DTriangle;
import org.joml.Vector2f;

public class C2DTriangleSprite extends C2DSprite{

    public C2DTriangleSprite(C2DTexture texture) {
        super(texture, new C2DTriangle());
    }

    public C2DTriangleSprite(C2DTexture texture, Vector2f pos, Vector2f scale, float rotation) {
        super(texture, new C2DTriangle(), pos, scale, rotation);
    }
}
