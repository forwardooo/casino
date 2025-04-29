#version 120

varying vec2 texCoords;

uniform sampler2D image;
uniform vec2 minUV;
uniform vec2 maxUV;
uniform vec2 size;
uniform vec4 color;
uniform float carveSize;
uniform float outline = 0;
uniform vec4 outlineColor;
uniform bool corners[4];

vec2 uvSize = maxUV - minUV;

vec2 carveTexel = vec2(carveSize, carveSize) / size;
vec2 maxCarveCoords = 1 - carveTexel;
vec2 outlineTexel = vec2(outline, outline) / size;
vec2 maxOutlineCoords = 1 - outlineTexel;
vec2 outlineCarveTexel = carveTexel + outlineTexel;
vec2 maxOutlineCarveCoords = 1 - outlineCarveTexel;

vec4 blend(vec4 color1, vec4 color2) {
    float minusAlpha = 1 - color2.w;
    return (vec4(
    color1.x * minusAlpha + color2.x * color2.w,
    color1.y * minusAlpha + color2.y * color2.w,
    color1.z * minusAlpha + color2.z * color2.w,
    color1.w * minusAlpha + color2.w
    ));
}

void main() {
    vec2 coords = minUV + (texCoords * uvSize);
    vec4 textureColor = texture2D(image, coords) * color;

    if ((coords.x < carveTexel.x && ((corners[0] && coords.y < carveTexel.y) || (corners[3] && coords.y > maxCarveCoords.y)))
    || (coords.x > maxCarveCoords.x && ((corners[1] && coords.y < carveTexel.y) || (corners[2] && coords.y > maxCarveCoords.y))))
    {
        discard;
    } else
    {
        if (outline > 0)
        {
            if (coords.x < outlineTexel.x || coords.x > maxOutlineCoords.x
            || coords.y < outlineTexel.y || coords.y > maxOutlineCoords.y
            || (coords.x < outlineCarveTexel.x && ((corners[0] && coords.y < outlineCarveTexel.y) || (corners[3] && coords.y > maxOutlineCarveCoords.y)))
            || (coords.x > maxOutlineCarveCoords.x && ((corners[1] && coords.y < outlineCarveTexel.y) || (corners[2] && coords.y > maxOutlineCarveCoords.y))))
            {
                if (outlineColor.w < 1)
                {
                    gl_FragColor = blend(textureColor, outlineColor);
                } else {
                    gl_FragColor = outlineColor;
                }
            } else
            {
                gl_FragColor = textureColor;
            }
        } else
        {
            gl_FragColor = textureColor;
        }
    }
}