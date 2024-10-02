#version 330 core
out vec4 FragColor;
uniform sampler2D _texture;
in vec2 TexCoord;
void main()
{
FragColor = texture(_texture,TexCoord);
}