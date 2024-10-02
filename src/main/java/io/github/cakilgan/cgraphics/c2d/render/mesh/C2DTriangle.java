package io.github.cakilgan.cgraphics.c2d.render.mesh;

public class C2DTriangle extends C2DGeo {
    static float[] vertices = {
            -0.5f, -0.5f, 0.0f,   1.0f, 0.0f,  // Sol alt (vertex) -> (1, 0) (texture)  -- Ters çevrildi

            0.5f, -0.5f, 0.0f,   0.0f, 0.0f,  // Sağ alt (vertex) -> (0, 0) (texture)  -- Ters çevrildi

            0.0f,  0.5f, 0.0f,   0.5f, 1.0f,  // Sağ üst (vertex) -> (0, 1) (texture)  -- Ters çevrildi
    };
    public C2DTriangle(){
        super(vertices,3);
    }
}
