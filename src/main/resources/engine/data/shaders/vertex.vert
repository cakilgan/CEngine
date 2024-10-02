#version 330 core
layout (location = 0) in vec3 aPos; // position has attribute position 0
layout (location = 1) in vec2 texCoords;
uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
out vec2 TexCoord;
void main()
{
gl_Position = projection*view*model*vec4(aPos.xyz, 1);
TexCoord = texCoords;
}