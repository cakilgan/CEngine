package io.github.cakilgan.cgraphics.c2d.render.mesh;

import org.joml.Vector2f;

public class C2DQuad extends C2DGeo {
    static float[] vertices = {
            -0.5f, -0.5f, 0.0f,   1.0f, 0.0f,  // Sol alt (vertex) -> (1, 0) (texture)  -- Ters çevrildi

            0.5f, -0.5f, 0.0f,   0.0f, 0.0f,  // Sağ alt (vertex) -> (0, 0) (texture)  -- Ters çevrildi

            0.5f,  0.5f, 0.0f,   0.0f, 1.0f,  // Sağ üst (vertex) -> (0, 1) (texture)  -- Ters çevrildi

            -0.5f,  0.5f, 0.0f,   1.0f, 1.0f   // Sol üst (vertex) -> (1, 1) (texture)  -- Ters çevrildi
    };
    public C2DQuad(){
        super(vertices,4);
    }
    public C2DQuad(Vector2f[] txCoords){
        super(vertices, txCoords);
    }
    public static Vector2f[] DEFAULT_TEXTURE_COORDINATES = new Vector2f[]{
            new Vector2f(1, 0),  // Sağ alt köşe
            new Vector2f(0, 0),  // Sol alt köşe
            new Vector2f(0, 1),  // Sol üst köşe
            new Vector2f(1, 1),   // Sağ üst köşe
    };
    public static C2DQuad createForBatch(){
        return new C2DQuad(DEFAULT_TEXTURE_COORDINATES);
    }
}
