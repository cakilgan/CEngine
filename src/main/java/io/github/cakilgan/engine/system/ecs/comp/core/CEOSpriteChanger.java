package io.github.cakilgan.engine.system.ecs.comp.core;

import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;

public class CEOSpriteChanger extends CEOComponent {
    String spritecode;

    public void setSpritecode(String spritecode) {
        this.spritecode = spritecode;
    }
    C2DSprite currentSprite;
    public void setCurrentSprite(C2DSprite currentSprite) {
        this.currentSprite = currentSprite;
    }

    @Override
    public void init() {
        getParent().getSprite(spritecode).set(currentSprite);
        currentSprite.init();
        super.init();
    }

    @Override
    public void update(double dt) {
        getParent().getSprite(spritecode).set(currentSprite);
        currentSprite.update(dt);
        super.update(dt);
    }

    public CEOComponent getCurrentSprite() {
        return currentSprite;
    }
}
