package io.github.cakilgan.game.comp;

import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DQuadSprite;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSpriteSheet;
import io.github.cakilgan.clogger.CLogger;
import io.github.cakilgan.clogger.system.CLoggerSystem;
import io.github.cakilgan.cresourcemanager.resources.DirectoryResource;
import io.github.cakilgan.cresourcemanager.resources.SpriteResource;
import io.github.cakilgan.cresourcemanager.resources.SpriteSheetResource;
import io.github.cakilgan.cresourcemanager.resources.TextureResource;
import io.github.cakilgan.cresourcemanager.resources.file.TextureFileResource;
import io.github.cakilgan.cscriptengine.engines.MapFileScriptEngine;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.map.C2DMap;
import io.github.cakilgan.engine.system.CEComponent;
import io.github.cakilgan.engine.system.HasLogger;
import io.github.cakilgan.game.scene.MainGameScene1;
import io.github.cakilgan.game.scene.MainGameScene2;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;

public class MainGame implements CEComponent, HasLogger {
    @Override
    public void init() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
       DirectoryResource gameResources =  CEngine.RESOURCE_MANAGER.getDirectoryResource("main").getDir("game");
       CEngine.RESOURCE_MANAGER.addResource("gameRes",gameResources);

       //backgrounds
        CEngine.RESOURCE_MANAGER.addResource("background_1",new TextureResource(gameResources.getDir("assets").getDir("background").getFile("background_layer_1.png"),new Vector2i(320,180)));
        CEngine.RESOURCE_MANAGER.addResource("background_2",new TextureResource(gameResources.getDir("assets").getDir("background").getFile("background_layer_2.png"),new Vector2i(320,180)));
        CEngine.RESOURCE_MANAGER.addResource("background_3",new TextureResource(gameResources.getDir("assets").getDir("background").getFile("background_layer_3.png"),new Vector2i(320,180)));

        CEngine.RESOURCE_MANAGER.addResource("b1",new SpriteResource("b1",C2DQuadSprite.createForBatch(CEngine.RESOURCE_MANAGER.getTexture("background_1"),new Vector2f(0,0),new Vector2f(0,0),0f)));
        CEngine.RESOURCE_MANAGER.addResource("b2",new SpriteResource("b2",C2DQuadSprite.createForBatch(CEngine.RESOURCE_MANAGER.getTexture("background_2"),new Vector2f(0,0),new Vector2f(0,0),0f)));
        CEngine.RESOURCE_MANAGER.addResource("b3",new SpriteResource("b3",C2DQuadSprite.createForBatch(CEngine.RESOURCE_MANAGER.getTexture("background_3"),new Vector2f(0,0),new Vector2f(0,0),0f)));

        //character spriteSheet
        CEngine.RESOURCE_MANAGER.addResource("character",new SpriteSheetResource("character_main",C2DSpriteSheet.readFromTextureMetadata(new TextureFileResource(gameResources.getDir("assets").getDir("character").getFile("char_blue.png"),new Vector2i(448,392)))));

        //shop
        CEngine.RESOURCE_MANAGER.addResource("shop",new SpriteSheetResource("shop",C2DSpriteSheet.readFromTextureMetadata(new TextureFileResource(gameResources.getDir("assets").getDir("decorations").getFile("shop_anim.png"),new Vector2i(708,128)))));

        //extra
        //grass
        //textures
        CEngine.RESOURCE_MANAGER.addResource("grass1",new TextureResource(gameResources.getDir("assets").getDir("decorations").getFile("grass_1.png"),new Vector2i(8,3)));
        CEngine.RESOURCE_MANAGER.addResource("grass2",new TextureResource(gameResources.getDir("assets").getDir("decorations").getFile("grass_2.png"),new Vector2i(8,3)));
        CEngine.RESOURCE_MANAGER.addResource("grass3",new TextureResource(gameResources.getDir("assets").getDir("decorations").getFile("grass_3.png"),new Vector2i(8,3)));

        CEngine.RESOURCE_MANAGER.addResource("grass",new SpriteSheetResource("grass",new C2DSpriteSheet(null,0,0,0,0,0)));

        C2DSprite grass1 =C2DQuadSprite.createForBatch(CEngine.RESOURCE_MANAGER.getTexture("grass1"),new Vector2f(),new Vector2f(),0f) ;
        C2DSprite grass2 =C2DQuadSprite.createForBatch(CEngine.RESOURCE_MANAGER.getTexture("grass2"),new Vector2f(),new Vector2f(),0f) ;
        C2DSprite grass3 =C2DQuadSprite.createForBatch(CEngine.RESOURCE_MANAGER.getTexture("grass3"),new Vector2f(),new Vector2f(),0f) ;

        CEngine.RESOURCE_MANAGER.getSpriteSheet("grass").setSprites(Arrays.asList(grass1,grass2,grass3));

        //tilesets
        CEngine.RESOURCE_MANAGER.addResource("tilesets_main",new SpriteSheetResource("tilesets_main",C2DSpriteSheet.readFromTextureMetadata(new TextureFileResource(gameResources.getDir("assets").getFile("oak_woods_tileset.png"),new Vector2i(504,360)))));

        //set scene
        CEngine.SCENE.setScene(new MainGameScene1());
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
