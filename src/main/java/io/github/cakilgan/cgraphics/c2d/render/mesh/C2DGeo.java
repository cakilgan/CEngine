package io.github.cakilgan.cgraphics.c2d.render.mesh;

import io.github.cakilgan.cgraphics.CGShader;
import io.github.cakilgan.engine.CEngine;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class C2DGeo extends C2DMesh{
    float zpos = 0f;

    public void setZpos(float zpos) {
        this.zpos = zpos;
    }

    float[] vertices;
    Vector2f[] texCoords;

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public Vector2f[] getTexCoords() {
        return texCoords;
    }

    public void setTexCoords(Vector2f[] texCoords) {
        this.texCoords = texCoords;
    }

    int count;
    int VAO,VBO;
    public C2DGeo(float[] vertices,int count){
        this.vertices = vertices;
        this.count = count;
    }
    public C2DGeo(float[] vertices,Vector2f[] texCoords){
        this.vertices = vertices;
        this.texCoords = texCoords;
    }
    @Override
    public void init() {

        VAO = GL30.glGenVertexArrays();
        VBO = GL15.glGenBuffers();

        GL30.glBindVertexArray(VAO);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
        vertexBuffer.clear();

        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);

        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

        GL30.glBindVertexArray(0);
    }
    public void uploadModel(Vector2f pos, Vector2f scale, float rotation, CGShader shader){
        Matrix4f matrix4f = new Matrix4f()
                .translate(new Vector3f(pos.x,pos.y,zpos))
                .rotate(Math.toRadians(rotation),0f,0f,1f)
                .scale(new Vector3f(scale.x,scale.y,0f));
        uploadModel(matrix4f,shader);
    }
    public void uploadModel(Vector2f pos,Vector2f scale,float rotation){
        uploadModel(pos,scale,rotation, CEngine.DEFAULT_SHADER);
    }
    public void uploadModel(Matrix4f matrix4f, CGShader shader){
        shader._mat4f("model",matrix4f);
    }
    @Override
    public void render() {
        glDrawArrays(GL_TRIANGLE_FAN,0,count);
    }

    @Override
    public void bind() {
        GL30.glBindVertexArray(VAO);
    }

    @Override
    public void unbind() {
        GL30.glBindVertexArray(0);
    }
    public void dispose(){
        GL15.glDeleteBuffers(VBO);
        GL30.glDeleteVertexArrays(VAO);
    }

    public int getZPos() {
        return (int) zpos;
    }
}
