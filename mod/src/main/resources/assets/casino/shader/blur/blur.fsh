#version 120

const int weightSize = 50;

varying vec2 texCoords;
uniform sampler2D image;

uniform vec2 size;
uniform vec2 dir;
uniform int radius;
uniform float weight[weightSize];

uniform float carveSize;
uniform bool corners[4];

vec2 oneTexel = (vec2(1, 1) / size);

void main() {
    if(carveSize > 0) {
        vec2 minProximity = vec2(carveSize) / size;

        float xProximity = 0.5 - abs(texCoords.x - 0.5);
        float yProximity = 0.5 - abs(texCoords.y - 0.5);
        if (xProximity < minProximity.x && yProximity < minProximity.y)
        {
            vec2 carveTexel = vec2(carveSize, carveSize) / size;
            vec2 maxCarveCoords = 1 - carveTexel;

            if ((texCoords.x < carveTexel.x && ((corners[0] && texCoords.y < carveTexel.y) || (corners[3] && texCoords.y > maxCarveCoords.y)))
            || (texCoords.x > maxCarveCoords.x && ((corners[1] && texCoords.y < carveTexel.y) || (corners[2] && texCoords.y > maxCarveCoords.y))))
            {
                gl_FragColor = vec4(0, 0, 0, 0);
                return;
            }
        }
    }

    vec3 result = texture2D(image, texCoords).rgb * weight[0];
    for(int i = 1; i < radius; ++i)
    {
        vec2 m = dir * i;
        result += texture2D(image, texCoords + oneTexel * m).rgb * weight[i];
        result += texture2D(image, texCoords - oneTexel * m).rgb * weight[i];
    }
    gl_FragColor = vec4(result, 1.0);
}
