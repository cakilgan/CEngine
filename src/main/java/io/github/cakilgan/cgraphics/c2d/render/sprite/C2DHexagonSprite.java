package io.github.cakilgan.cgraphics.c2d.render.sprite;

import io.github.cakilgan.cgraphics.c2d.render.C2DTexture;
import io.github.cakilgan.cgraphics.c2d.render.mesh.C2DHexagon;
import io.github.cakilgan.cgraphics.c2d.render.mesh.C2DMesh;
import org.joml.Vector2f;

public class C2DHexagonSprite extends C2DSprite{
    public C2DHexagonSprite(C2DTexture texture) {
        super(texture, new C2DHexagon());
    }

    public C2DHexagonSprite(C2DTexture texture, Vector2f pos, Vector2f scale, float rotation) {
        super(texture, new C2DHexagon(), pos, scale, rotation);
    }
}
