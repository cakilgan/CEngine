package io.github.cakilgan.game.comp;

import io.github.cakilgan.cgraphics.c2d.render.C2DTexture;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DQuadSprite;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSpriteSheet;
import io.github.cakilgan.cresourcemanager.resources.DirectoryResource;
import io.github.cakilgan.cresourcemanager.resources.SpriteResource;
import io.github.cakilgan.cresourcemanager.resources.SpriteSheetResource;
import io.github.cakilgan.cresourcemanager.resources.file.TextureFileResource;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.system.CEComponent;
import io.github.cakilgan.game.scene.flappyBirdGame.FlappyBirdGameScene;
import org.joml.Vector2f;
import org.joml.Vector2i;

import static org.lwjgl.opengl.GL11.*;

public class FlappyBirdGame implements CEComponent {
    @Override
    public void init() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
        DirectoryResource gameResources =  CEngine.RESOURCE_MANAGER.manager.getDirectoryResource("main").getDir("game").getDir("assets").getDir("flappyBirdGame");
        CEngine.RESOURCE_MANAGER.addResource("gameRes",gameResources);

        //background
        CEngine.RESOURCE_MANAGER.addResource("b1",new SpriteResource("b1",C2DQuadSprite.createForBatch(new C2DTexture(gameResources.getDir("map").getFile("background.png"),new Vector2i(288,512)),new Vector2f(),new Vector2f(0,0),0f)));
        //pipe
        CEngine.RESOURCE_MANAGER.addResource("pipe",new SpriteResource("pipe",C2DQuadSprite.createForBatch(new C2DTexture(gameResources.getDir("map").getFile("pipe.png"),new Vector2i(52,320)),new Vector2f(),new Vector2f(),0f)));
        //ground
        CEngine.RESOURCE_MANAGER.addResource("ground",new SpriteResource("ground",C2DQuadSprite.createForBatch(new C2DTexture(gameResources.getDir("map").getFile("ground.png"),new Vector2i(336,112)),new Vector2f(),new Vector2f(),0f)));

        //character
        CEngine.RESOURCE_MANAGER.addResource("character",new SpriteSheetResource("character", C2DSpriteSheet.readFromTextureMetadata(new TextureFileResource(gameResources.getDir("char").getDir("sheets").getFile("bird_anim.png"),new Vector2i(102,24)))));

        CEngine.SCENE.setScene(new FlappyBirdGameScene());
    }

    @Override
    public void loop() {

    }

    @Override
    public void dispose() {

    }
}
