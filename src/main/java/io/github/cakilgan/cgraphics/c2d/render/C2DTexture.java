package io.github.cakilgan.cgraphics.c2d.render;

import io.github.cakilgan.clogger.CLogger;
import io.github.cakilgan.clogger.system.CLoggerSystem;
import io.github.cakilgan.cresourcemanager.resources.FileResource;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class C2DTexture implements AutoCloseable{
    static CLogger logger = CLoggerSystem.logger(C2DTexture.class);
    FileResource path;
    public FileResource getPath() {
        return path;
    }
    boolean hasCreatedAlready;

    public boolean hasCreatedAlready() {
        return hasCreatedAlready;
    }

    Vector2i scale;

    public Vector2i getScale() {
        return scale;
    }

    private int texID;
    public C2DTexture(FileResource resource, Vector2i scale){
      this.path = resource;
      this.scale = scale;
      this.texID = -1;
    }
    public void create(){
        if (hasCreatedAlready){
            logger.warn("textures is already created!");
            return;
        }
        hasCreatedAlready = true;
       texID =glGenTextures();
        glBindTexture(GL_TEXTURE_2D,texID);

        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);


        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);

        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);


        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        stbi_set_flip_vertically_on_load(true);

        ByteBuffer img = stbi_load(path.id.getID(),width,height,channels,0);

        if (img!=null){
            this.scale.x = (width.get(0));
            this.scale.y = (height.get(0));

            if (channels.get(0)==3){
                glTexImage2D(GL_TEXTURE_2D,0,GL_RGB,width.get(0),height.get(0)
                        ,0,GL_RGB,GL_UNSIGNED_BYTE,img);
                logger.info("loaded image successfully from --> "+path.id.getID());
            }else if (channels.get(0)==4){
                glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA,width.get(0),height.get(0)
                        ,0,GL_RGBA,GL_UNSIGNED_BYTE,img);
                logger.info("loaded image successfully from --> "+path.id.getID());
            }else{
                logger.error("unknown number of channels --> "+channels.get(0));
            }

        }else{
            logger.error("cannot load image from -> "+path.id.getID());
        }

        stbi_image_free(img);

    }
    public void bind(){
         glBindTexture(GL_TEXTURE_2D,texID);
    }
    public void unbind(){
        glBindTexture(GL_TEXTURE_2D,0);
    }

    @Override
    public void close() throws Exception {
        unbind();
    }

    public void dispose() {
        if (texID != -1) {
            glDeleteTextures(texID); // Texture kaynağını serbest bırak
            texID = -1; // Tekrar serbest bırakmayı önlemek için ID'yi -1 yap
            logger.info("Texture disposed successfully: " + path.id.getID());
        } else {
            logger.warn("Texture already disposed: " + path.id.getID());
        }
    }
}
