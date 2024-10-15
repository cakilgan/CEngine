package io.github.cakilgan.engine.system.ecs.comp.core;

import io.github.cakilgan.cgraphics.c2d.render.font.C2DFontRenderer;
import io.github.cakilgan.engine.CEngine;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class CEOButton extends CEOComponent {
    String buttonText;
    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    C2DFontRenderer fontRenderer;

    public C2DFontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public void setFontRenderer(C2DFontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
    }

    public boolean canClick;
    public float xs,xe,ys,ye;
    public Vector3f mousePos;
    public Vector4f buttonVertex= new Vector4f();

    @Override
    public void init() {
        mousePos = new Vector3f(0,0,0);
        super.init();
    }

    @Override
    public void update(double dt) {
        if (fontRenderer != null) {
            fontRenderer.setText(buttonText);
        }

        mousePos =
                new Vector3f((float) CEngine.MOUSE.getX(),
                        (float) CEngine.MOUSE.getY(),0f)
        ;

        Vector2f objPos = getParent().getParent().getScene().getCamera().worldToScreen(new Vector3f(getParent().getTransform().getPos().x,getParent().getTransform().getPos().y,0f));

        xs = objPos.x + getParent().getTransform().getScale().x / 2;
        xe = objPos.x - getParent().getTransform().getScale().x / 2;
        ys = objPos.y + getParent().getTransform().getScale().y / 2-10;
        ye = objPos.y - getParent().getTransform().getScale().y / 2-10;
        buttonVertex = new Vector4f(xs,xe,ys,ye);

        if (mousePos.x < xs && mousePos.x > xe && mousePos.y < ys && mousePos.y > ye) {
            canClick = true;
        } else {
            canClick = false;
        }
        super.update(dt);
    }
}
