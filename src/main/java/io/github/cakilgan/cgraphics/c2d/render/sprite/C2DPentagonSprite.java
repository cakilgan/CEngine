package io.github.cakilgan.cgraphics.c2d.render.sprite;

import io.github.cakilgan.cgraphics.c2d.render.C2DTexture;
import io.github.cakilgan.cgraphics.c2d.render.mesh.C2DPentagon;
import org.joml.Vector2f;

public class C2DPentagonSprite extends C2DSprite{
    public C2DPentagonSprite(C2DTexture texture) {
        super(texture, new C2DPentagon());
    }

    public C2DPentagonSprite(C2DTexture texture, Vector2f pos, Vector2f scale, float rotation) {
        super(texture, new C2DPentagon(), pos, scale, rotation);
    }
}
