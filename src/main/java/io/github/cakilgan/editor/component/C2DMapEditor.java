package io.github.cakilgan.editor.component;

import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSpriteSheet;
import io.github.cakilgan.cresourcemanager.resources.DirectoryResource;
import io.github.cakilgan.cresourcemanager.resources.SpriteSheetResource;
import io.github.cakilgan.cresourcemanager.resources.file.TextureFileResource;
import io.github.cakilgan.editor.scene.C2DMapEditorScene1;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.system.CEComponent;
import org.joml.Vector2i;

import static org.lwjgl.opengl.GL11.*;

public class C2DMapEditor implements CEComponent {
    @Override
    public void init() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
        DirectoryResource gameResources =  CEngine.RESOURCE_MANAGER.manager.getDirectoryResource("main").getDir("game");
        CEngine.RESOURCE_MANAGER.addResource("gameRes",gameResources);
        CEngine.RESOURCE_MANAGER.addResource("snakeAtlas",new SpriteSheetResource("snakeAtlas", C2DSpriteSheet.readFromTextureMetadata(new TextureFileResource(gameResources.getDir("assets").getDir("snakegame").getFile("atlas.png"),new Vector2i(170,170)))));
        CEngine.SCENE.setScene(new C2DMapEditorScene1());
    }

    @Override
    public void loop() {

    }

    @Override
    public void dispose() {

    }
}
