#version 120

varying vec2 texCoords;

uniform vec2 size;
uniform vec4 color;
uniform float radius;

uniform float carveSize;

vec2 minTexel = vec2(radius + carveSize) / size;

float easeOutCubic(float x) {
    return x * x * x;
}

void main() {
    float xProximity = 0.5 - abs(texCoords.x - 0.5);
    float yProximity = 0.5 - abs(texCoords.y - 0.5);

    float xMultiplier = 1;
    if(xProximity < minTexel.x) {
        xMultiplier = xProximity / minTexel.x;
    }

    float yMultiplier = 1;
    if (yProximity < minTexel.y) {
        yMultiplier = yProximity / minTexel.y;
    }

    float multiplier = xMultiplier * yMultiplier;
    if (multiplier < 1) {
        multiplier = easeOutCubic(multiplier);
    }

    gl_FragColor = vec4(color.xyz, color.w * multiplier);
}
