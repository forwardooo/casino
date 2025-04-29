#version 120

varying vec2 texCoords;

uniform sampler2D image;
uniform vec2 minUV;
uniform vec2 maxUV;

vec2 uvSize = maxUV - minUV;

void main() {
    gl_FragColor = texture2D(image, minUV + (texCoords * uvSize));
}