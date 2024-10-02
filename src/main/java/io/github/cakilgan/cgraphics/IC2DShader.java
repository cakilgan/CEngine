package io.github.cakilgan.cgraphics;

import io.github.cakilgan.cresourcemanager.resources.file.ShaderResource;

public interface IC2DShader {
    void start();
    void stop();
    void link();
    int createVertexShader();
    int createFragmentShader();
    int createShader(ShaderResource resource,int type);
    void createProgram();
}
