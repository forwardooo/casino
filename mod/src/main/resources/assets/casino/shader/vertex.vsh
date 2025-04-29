#version 120
attribute vec3 position;
attribute vec2 texCoord;

uniform mat4 modelViewMatrix;
uniform mat4 projectMatrix;
uniform vec2 size;

varying vec2 texCoords;

void main()
{
    gl_Position = projectMatrix * modelViewMatrix * (vec4(position, 1.0) * vec4(size, 1, 1));
    texCoords = texCoord;
}