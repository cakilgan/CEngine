package io.github.cakilgan.game.comp;

import io.github.cakilgan.cgraphics.c2d.render.C2DTexture;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DQuadSprite;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSpriteSheet;
import io.github.cakilgan.clogger.CLogger;
import io.github.cakilgan.clogger.system.CLoggerSystem;
import io.github.cakilgan.cresourcemanager.resources.DirectoryResource;
import io.github.cakilgan.cresourcemanager.resources.SpriteResource;
import io.github.cakilgan.cresourcemanager.resources.SpriteSheetResource;
import io.github.cakilgan.cresourcemanager.resources.file.TextureFileResource;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.system.CEComponent;
import io.github.cakilgan.engine.system.HasLogger;
import io.github.cakilgan.game.scene.snakeGame.SnakeGameMenuScene;
import io.github.cakilgan.game.scene.snakeGame.SnakeGameScene;
import org.joml.Vector2f;
import org.joml.Vector2i;

import static org.lwjgl.opengl.GL11.*;

public class SnakeGame implements CEComponent, HasLogger {
    @Override
    public void init() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
        DirectoryResource gameResources =  CEngine.RESOURCE_MANAGER.manager.getDirectoryResource("main").getDir("game");
        CEngine.RESOURCE_MANAGER.addResource("gameRes",gameResources);



        //snakegame
        CEngine.RESOURCE_MANAGER.addResource("snakeAtlas",new SpriteSheetResource("snakeAtlas", C2DSpriteSheet.readFromTextureMetadata(new TextureFileResource(gameResources.getDir("assets").getDir("snakegame").getFile("atlas.png"),new Vector2i(170,170)))));
        //set scene
        CEngine.SCENE.setScene(new SnakeGameMenuScene());
    }

    @Override
    public void loop() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public CLogger getLogger() {
        return CLoggerSystem.logger(this.getClass());
    }
}
