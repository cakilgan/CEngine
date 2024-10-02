package io.github.cakilgan.cgraphics.c2d.render;

import io.github.cakilgan.cgraphics.CGShader;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;

import java.util.ArrayList;
import java.util.List;

public class C2DRenderer {
    public List<C2DSprite> sprites;
    public C2DRenderer(){
        sprites = new ArrayList<>();
    }
    public void render(CGShader shader){
        for (C2DSprite sprite : sprites) {
            sprite.bind();
            sprite.render(shader);
            sprite.unbind();
        }
    }
}
