package io.github.cakilgan.cgraphics;

import io.github.cakilgan.clogger.CLogger;
import io.github.cakilgan.clogger.system.CLoggerSystem;
import io.github.cakilgan.cresourcemanager.resources.ShaderProgramResource;
import io.github.cakilgan.cresourcemanager.resources.file.ShaderResource;
import io.github.cakilgan.engine.system.CEComponent;
import io.github.cakilgan.engine.system.RunOnFirstLineOfWhileLoop;
import io.github.cakilgan.engine.system.RunOnLastLineOfWhileLoop;
import org.joml.Matrix2f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class CGShader implements IC2DShader, CEComponent, RunOnLastLineOfWhileLoop {
    static CLogger logger = CLoggerSystem.logger(CGShader.class);
    boolean isActive;
    ShaderProgramResource resource;
    public CGShader(ShaderProgramResource resource){
        this.resource = resource;
    }
    public boolean isActive() {
        return isActive;
    }
    @Override
    public void start() {
        isActive = true;
        glUseProgram(this.resource.id.getID());
    }

    @Override
    public void stop() {
        isActive = false;
        glUseProgram(0);
    }
    public void refresh(){
        this.isActive = glIsProgram(this.resource.id.getID());
    }
    @Override
    public void link() {
        glLinkProgram(this.resource.id.getID());
        logger.debug("linking program.");
        int success = glGetProgrami(this.resource.id.getID(),GL_LINK_STATUS);
        if (success==GL_FALSE){
            int length = glGetProgrami(this.resource.id.getID(),GL_INFO_LOG_LENGTH);
            logger.error("error in phase : link id/2");
            logger.error("[2] log : "+glGetProgramInfoLog(this.resource.id.getID(),length));
        }
    }

    @Override
    public int createVertexShader() {
        this.resource.vertex.shaderID = createShader(this.resource.vertex,GL_VERTEX_SHADER);
        return this.resource.vertex.shaderID;
    }

    @Override
    public int createFragmentShader() {
       this.resource.fragment.shaderID =  createShader(this.resource.fragment,GL_FRAGMENT_SHADER);
       return this.resource.fragment.shaderID;
    }

    @Override
    public int createShader(ShaderResource resource, int type) {
        int shaderID;
        shaderID = glCreateShader(type);
        glShaderSource(shaderID,resource.getShaderText());
        logger.debug("binding shader source -> \n"+resource.getShaderText().trim()+"\n to shader -> "+shaderID);
        glCompileShader(shaderID);
        logger.debug("compiling shader -> "+shaderID);
        int succes = glGetShaderi(shaderID,GL_COMPILE_STATUS);
        if (succes==0){
            int length = glGetShaderi(shaderID,GL_INFO_LOG_LENGTH);
            logger.error("error in phase : compile id/1");
            logger.error("[1] log : "+glGetShaderInfoLog(shaderID,length));
        }
        glAttachShader(this.resource.id.getID(),shaderID);
        logger.debug("attached shader -> "+shaderID+" to shaderProgram -> "+this.resource.id.getID());
        return shaderID;
    }

    @Override
    public void createProgram() {
        if (this.resource.id.getID()==0){
            this.resource.id.setID(glCreateProgram());
            logger.debug("created shader program --> id: "+this.resource.id.getID());
        }else{
            logger.warn("shader program is already created.");
        }
    }

    public void _mat4f(String v0, Matrix4f v1){
        start();
        C2DShaderRecord shaderRecord = _default(v0);
        shaderRecord.buffer = MemoryUtil.memAllocFloat(16);
        v1.get(shaderRecord.buffer);
        glUniformMatrix4fv(shaderRecord.location,false,shaderRecord.buffer);
    }
    public void _mat3f(String v0, Matrix3f v1){
        C2DShaderRecord shaderRecord = _default(v0);
        shaderRecord.buffer = MemoryUtil.memAllocFloat(12);
        v1.get(shaderRecord.buffer);
        glUniformMatrix3fv(shaderRecord.location,false,shaderRecord.buffer);
    }
    public void _mat2f(String v0, Matrix2f v1){
        C2DShaderRecord shaderRecord = _default(v0);
        shaderRecord.buffer = MemoryUtil.memAllocFloat(8);
        v1.get(shaderRecord.buffer);
        glUniformMatrix2fv(shaderRecord.location,false,shaderRecord.buffer);
    }
    public void _vec4(String v0,float[] v1){
        C2DShaderRecord shaderRecord = _default(v0);
        FloatBuffer buffer = MemoryUtil.memAllocFloat(v1.length);
        buffer.put(v1).flip();
        glUniform4fv(shaderRecord.location,buffer);
    }
    public void _4f(String v0,float v1,float v2,float v3,float v4){
        C2DShaderRecord shaderRecord = _default(v0);
        glUniform4f(shaderRecord.location,v1,v2,v3,v4);
    }
    public void _3f(String v0,float v1,float v2,float v3){
        C2DShaderRecord shaderRecord = _default(v0);
        glUniform3f(shaderRecord.location,v1,v2,v3);
    }
    public void _2f(String v0,float v1,float v2){
        C2DShaderRecord shaderRecord = _default(v0);
        glUniform2f(shaderRecord.location,v1,v2);
    }
    public void _1f(String v0,float v1){
        C2DShaderRecord shaderRecord = _default(v0);
        glUniform1f(shaderRecord.location,v1);
    }
    public void _4i(String v0,int v1,int v2,int v3,int v4){
        C2DShaderRecord shaderRecord = _default(v0);
        glUniform4i(shaderRecord.location,v1,v2,v3,v4);
    }
    public void _3i(String v0,int v1,int v2,int v3){
        C2DShaderRecord shaderRecord = _default(v0);
        glUniform3i(shaderRecord.location,v1,v2,v3);
    }
    public void _2i(String v0,int v1,int v2){
        C2DShaderRecord shaderRecord = _default(v0);
        glUniform2i(shaderRecord.location,v1,v2);
    }
    public void _1i(String v0,int v1){
        C2DShaderRecord shaderRecord = _default(v0);
        glUniform1i(shaderRecord.location,v1);

    }
    public void _tex(String v0,int s0){
        C2DShaderRecord shaderRecord = _default(v0);
        glUniform1i(shaderRecord.location,s0);

    }
    public void _tarray(String v0,int[] ms0){
        C2DShaderRecord shaderRecord = _default(v0);
        glUniform1iv(shaderRecord.location,ms0);
    }
    public C2DShaderRecord _default(String v0){
        C2DShaderRecord record = new C2DShaderRecord(0,null);
        int location = glGetUniformLocation(this.resource.id.getID(),v0);
        //logger.info("uniform location -> "+location);
        record.location = location;
        return record;
    }

    @Override
    public void init() {
        createProgram();
        createVertexShader();
        createFragmentShader();
        link();
    }

    @Override
    public void loop() {
        start();
    }

    @Override
    public void dispose() {
        glDeleteShader(this.resource.fragment.shaderID);
        glDeleteShader(this.resource.vertex.shaderID);
    }

    @Override
    public void runLastLineOfWhileLoop() {
        dispose();
        stop();
    }

    public static class C2DShaderRecord {
        FloatBuffer buffer;
        int location;
        public C2DShaderRecord(int location, FloatBuffer buffer){
            this.buffer = buffer;
            this.location = location;
        }
    }

}
