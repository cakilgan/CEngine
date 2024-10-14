package io.github.cakilgan.engine.system.ecs.comp.core;

import io.github.cakilgan.cgraphics.c2d.render.font.C2DFontRenderer;
import io.github.cakilgan.engine.CEngine;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class CEOButton extends CEOComponent {
    String buttonText;
    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    C2DFontRenderer fontRenderer;
    public void setFontRenderer(C2DFontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
    }

    public boolean canClick;
    public float xs,xe,ys,ye;
    public Vector3f mousePos;

    @Override
    public void init() {
        mousePos = new Vector3f(0,0,0);
        super.init();
    }

    @Override
    public void update(double dt) {
        fontRenderer.setText(buttonText);
        mousePos = getParent().getParent().getScene().getCamera().screenToWorld((float) CEngine.MOUSE.getX(), (float) CEngine.MOUSE.getY());
        Vector2f objpos = getParent().getTransform().getPos();
        xs = objpos.x+getParent().getTransform().getScale().x/2;
        xe = objpos.x-getParent().getTransform().getScale().x/2;
        ys = objpos.y+getParent().getTransform().getScale().y/2+10;
        ye = objpos.y-getParent().getTransform().getScale().y/2+10;
        if (mousePos.x<xs&&mousePos.x>xe){
            if (mousePos.y<ys&&mousePos.y>ye){
                canClick = true;
            }else{
                canClick = false;
            }
        }else{
            canClick = false;
        }
        super.update(dt);
    }
}
