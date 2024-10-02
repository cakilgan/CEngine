package io.github.cakilgan.cgraphics.c2d.render.mesh;

public class C2DPentagon extends C2DGeo {
    static float[] vertices = {
            0.0f,  0.5f,  0.0f,  0.5f, 1.0f, // Üst nokta
            -0.475f,  0.154f,  0.0f,  0.0f, 0.75f, // Sol üst
            -0.293f, -0.404f,  0.0f,  0.25f, 0.0f, // Sol alt
            0.293f, -0.404f,  0.0f,  0.75f, 0.0f, // Sağ alt
            0.475f,  0.154f,  0.0f,  1.0f, 0.75f  // Sağ üst
    };
    public C2DPentagon() {
        super(vertices, 5);
    }
}
