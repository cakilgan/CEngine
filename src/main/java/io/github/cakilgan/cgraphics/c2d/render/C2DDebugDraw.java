package io.github.cakilgan.cgraphics.c2d.render;


import io.github.cakilgan.engine.system.ecs.comp.core.CEOComponent;
import org.joml.Vector3f;

public class C2DDebugDraw extends CEOComponent {
    Vector3f colorcode = new Vector3f(0,1,0);

    public C2DDebugDraw setColorcode(Vector3f colorcode) {
        this.colorcode = colorcode;
        return this;
    }

    public Vector3f getColor() {
        return colorcode;
    }
}

