package io.github.cakilgan.cgraphics.c2d.render;

import io.github.cakilgan.cgraphics.CGShader;
import io.github.cakilgan.cgraphics.c2d.render.mesh.C2DGeo;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.clogger.CLogger;
import io.github.cakilgan.clogger.system.CLoggerSystem;
import io.github.cakilgan.engine.CEngine;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public class C2DRenderBatch implements Comparable<C2DRenderBatch>{
    public static CLogger logger = CLoggerSystem.logger(C2DRenderBatch.class);

    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TCOORDS_SIZE = 2;
    private final int TID_SIZE = 1;


    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TCOORDS_OFFSET = COLOR_OFFSET+COLOR_SIZE*Float.BYTES;
    private final int TID_OFFSET = TCOORDS_OFFSET+TCOORDS_SIZE*Float.BYTES;
    private final int VERTEX_SIZE = 9;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private C2DSprite[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;
    private int[] texS = {0,1,2,3,4,5,6,7};

    private List<C2DTexture> textureList;
    private int vaoID, vboID;
    private int maxBatchSize;
    private CGShader shader;
    private int z_i;
    public C2DRenderBatch(int maxBatchSize,int z_i) {
        shader = CEngine.BATCH_SHADER;
        this.sprites = new C2DSprite[maxBatchSize];
        this.maxBatchSize = maxBatchSize;
        this.z_i = z_i;

        // 4 vertices quads
        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];


        this.numSprites = 0;
        this.hasRoom = true;
        this.textureList = new ArrayList<>();
    }

    public void start() {
        //1.0f,-1.9

        // Generate and bind a Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable the buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2,TCOORDS_SIZE,GL_FLOAT,false,VERTEX_SIZE_BYTES,TCOORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3,TID_SIZE,GL_FLOAT,false,VERTEX_SIZE_BYTES,TID_OFFSET);
        glEnableVertexAttribArray(3);

    }

    public void addSprite(C2DSprite spr) {
        int index = this.numSprites;
        this.sprites[index] = spr;
        this.numSprites++;


        if (spr.getTexture()!=null){
            if (!textureList.contains(spr.getTexture())){
                textureList.add(spr.getTexture());
            }
        }
        // Add properties to local vertices array
        loadVertexProperties(index);
        if (numSprites >= this.maxBatchSize) {
            this.hasRoom = false;
        }
    }
    public void render() {
        boolean rbData = false;
        for (int i = 0; i <numSprites; i++) {
            C2DSprite sprite = sprites[i];
            if (sprite.isFlag()){
                if (!textureList.contains(sprite.getTexture())){
                    textureList.add(sprite.getTexture());
                }
                loadVertexProperties(i);
                sprite.setFlag(false);
                rbData = true;
            }
        }
        if (rbData){
            glBindBuffer(GL_ARRAY_BUFFER,vboID);
            glBufferSubData(GL_ARRAY_BUFFER,0,vertices);
        }


        // Use shader
        shader.start();
        if(textureList !=null){
            for (int j = 0; j < textureList.size(); j++) {
                glActiveTexture(GL_TEXTURE0+j+1);
                if (textureList.get(j)!=null){
                    textureList.get(j).bind();
                }
            }
        }
        shader._tarray("uTex_a",texS);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for (C2DTexture t2DTexture : textureList) {
            if (t2DTexture!=null){
                t2DTexture.unbind();
            }
        }
        shader.stop();
    }

    private void loadVertexProperties(int index) {
        C2DSprite sprite = this.sprites[index];

        // Find offset within array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();
        Vector2f[] tCoords = ((C2DGeo)sprite.mesh()).getTexCoords();

        int t_id = 0;
        if (sprite.getTexture()!=null){
            for (int j = 0; j < textureList.size(); j++) {
                if (textureList.get(j)==sprite.getTexture()){
                    t_id = j+1;
                    break;
                }
            }
        }

        float x_pos = sprite.getPosition().x;
        float y_pos = sprite.getPosition().y;

        boolean isRotated = sprite.getRotation() != 0.0f||sprite.getRotation()!=360.0f;
        Matrix4f transformMatrix = new Matrix4f().identity();
        if(isRotated){
            transformMatrix.translate(x_pos,y_pos,0f);
            transformMatrix.rotate((float) Math.toRadians(sprite.getRotation()),
                    0,0,1f);
            transformMatrix.scale(sprite.getScale().x,sprite.getScale().y,1);
        }
        // Add vertices with the appropriate properties
        float xAdd = 0.5f;
        float yAdd = 0.5f;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    xAdd = -0.5f; // Sol alt köşe
                    yAdd = -0.5f;
                    break;
                case 1:
                    xAdd = 0.5f;  // Sağ alt köşe
                    yAdd = -0.5f;
                    break;
                case 2:
                    xAdd = 0.5f;  // Sağ üst köşe
                    yAdd = 0.5f;
                    break;
                case 3:
                    xAdd = -0.5f; // Sol üst köşe
                    yAdd = 0.5f;
                    break;
            }

            Vector4f currentPos = new Vector4f(sprite.getPosition().x + (xAdd * sprite.getScale().x),
                    sprite.getPosition().y + (yAdd * sprite.getScale().y),
                    0,1);
            if (isRotated){
                currentPos = new Vector4f(xAdd,yAdd,0,1).mul(transformMatrix);
            }
            // Load position
            vertices[offset] =currentPos.x;
            vertices[offset + 1] =currentPos.y;

            // Load color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            vertices[offset+6] =tCoords[i].x;
            vertices[offset+7] = tCoords[i].y;


            vertices[offset+8] = t_id;

            offset += VERTEX_SIZE;
        }

        ((C2DGeo) sprite.mesh()).setVertices(vertices);
    }

    private int[] generateIndices() {
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[6 * maxBatchSize];
        for (int i=0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }
        return elements;
    }

    private void loadElementIndices(int[] elements, int index) {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        // 3, 2, 0, 0, 2, 1        7, 6, 4, 4, 6, 5
        // Triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset + 0;

        // Triangle 2
        elements[offsetArrayIndex + 3] = offset + 0;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    public boolean hasRoom() {
        return this.hasRoom;
    }
    public boolean canAddSprite(){
        return this.numSprites<8;
    }
    public boolean haveTexture(C2DTexture texture){
        return this.textureList.contains(texture);
    }

    public int getZ_i() {
        return z_i;
    }

    @Override
    public int compareTo(C2DRenderBatch o) {
        return Integer.compare(this.z_i,o.z_i);
    }
    public void dispose() {
        // Unbind the VAO and VBO
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // Delete the VBO
        if (vboID != 0) {
            glDeleteBuffers(vboID);
            vboID = 0; // Set to 0 to avoid double deletion
        }

        // Delete the VAO
        if (vaoID != 0) {
            glDeleteVertexArrays(vaoID);
            vaoID = 0; // Set to 0 to avoid double deletion
        }

        // Delete any textures in the texture list
        for (C2DTexture texture : textureList) {
            texture.dispose(); // Assuming the C2DTexture class has a dispose method
        }
        textureList.clear(); // Clear the list after disposing of textures

        // Optionally, clear the vertices array
        vertices = null; // Help garbage collector
        sprites = null; // Help garbage collector

        CEngine.LOGGER.info("C2DRenderBatch disposed!");
    }
}

