package io.github.cakilgan.cgraphics.c2d.render.mesh;

public class C2DHexagon extends C2DGeo {
    // Altıgenin köşe noktaları ve texture koordinatları
    static float[] vertices = {
            //  x,      y,     z,    u,    v
            0.0f,  0.5f,  0.0f,  0.5f, 1.0f,  // Üst nokta
            -0.433f,  0.25f,  0.0f,  0.25f, 0.75f, // Sol üst
            -0.433f, -0.25f,  0.0f,  0.25f, 0.25f, // Sol alt
            0.0f, -0.5f,  0.0f,  0.5f, 0.0f,  // Alt nokta
            0.433f, -0.25f,  0.0f,  0.75f, 0.25f, // Sağ alt
            0.433f,  0.25f,  0.0f,  0.75f, 0.75f,  // Sağ üst
    };

    // Constructor
    public C2DHexagon() {
        super(vertices, 6); // Vertices ve vertex sayısı gönderiliyor
    }
}

