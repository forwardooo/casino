#version 120

// Входные координаты текстуры для фрагмента
varying vec2 texCoords;

// Униформы для настройки различных аспектов шейдера
uniform vec2 size;                     // Размер в пространстве экрана
uniform vec4 color;                    // Основной цвет формы
uniform float carveSize;               // Размер выемки (отступ между формой и её краями)
uniform float outline = 0;             // Ширина обводки, по умолчанию 0 (нет обводки)
uniform vec4 outlineColor;             // Цвет обводки
uniform vec4 outlineGradientColor;     // Цвет для градиента в обводке
uniform bool corners[4];               // Массив для включения или выключения обводки для каждого угла
uniform bool outLines[4];              // Массив для включения или выключения обводки для каждой стороны (верх, правый, низ, левый)
uniform float circleRadius = 0;        // Радиус для рисования круга внутри формы
uniform vec4 circleColor;              // Цвет круга
uniform vec2 circlePos = vec2(0, 0);   // Позиция круга в текстурном пространстве (нормализовано)

uniform float outlineGradientHeight = 0.5; // Высота, на которой начинается градиент обводки (0.5 означает 50% высоты)

// Функция для вычисления расстояния от центра круга
float getDistanceFromCenter() {
    vec2 pos = vec2(abs(texCoords.x - circlePos.x), abs(texCoords.y - circlePos.y)); // Вычисляем расстояние от центра круга
    pos = (pos * size) / max(size.x, size.y);  // Масштабируем в зависимости от размера
    return length(pos);  // Возвращаем евклидово расстояние
}

// Функция проверки, является ли цвет "нулевым" (все компоненты равны 0.0)
bool isNullColor(vec4 color) {
    return color.r == 0.0 && color.g == 0.0 && color.b == 0.0 && color.a == 0.0;
}

void main() {
    // Минимальное расстояние для проверки выемки и обводки, основанное на carveSize и outline
    vec2 minProximity = vec2(carveSize + outline) / size;

    // Вычисляем близость к центру формы (используется для логики выемки)
    float xProximity = 0.5 - abs(texCoords.x - 0.5);
    float yProximity = 0.5 - abs(texCoords.y - 0.5);

    // Если фрагмент находится внутри круга, закрашиваем его цветом круга
    if (circleRadius > 0 && getDistanceFromCenter() <= circleRadius) {
        gl_FragColor = circleColor;
    } else {
        // Иначе, закрашиваем фрагмент основным цветом
        gl_FragColor = color;
    }

    // Если фрагмент слишком далеко от центра (по близости), игнорируем его
    if (xProximity >= minProximity.x && yProximity >= minProximity.y) {
        return;
    }

    // Вычисляем размер области для выемки, используя carveSize
    vec2 carveTexel = vec2(carveSize, carveSize) / size;
    vec2 maxCarveCoords = 1 - carveTexel;

    // Проверяем, находится ли фрагмент в одном из углов, и игнорируем его, если он внутри выемки
    if ((texCoords.x <= carveTexel.x && ((corners[0] && texCoords.y <= carveTexel.y) || (corners[3] && texCoords.y >= maxCarveCoords.y)))
    || (texCoords.x >= maxCarveCoords.x && ((corners[1] && texCoords.y <= carveTexel.y) || (corners[2] && texCoords.y >= maxCarveCoords.y)))) {
        discard;  // Удаляем фрагмент, если он внутри области выемки угла
    } else {
        // Если обводка включена
        if (outline > 0) {
            // Вычисляем размер области для обводки, используя ширину outline
            vec2 outlineTexel = vec2(outline, outline) / size;
            vec2 maxOutlineCoords = 1 - outlineTexel;  // Максимальные координаты для области обводки
            vec2 maxOutlineCarveCoords = 1 - minProximity;  // Максимальные координаты для выемки

            // Проверяем, находится ли фрагмент на одной из включенных сторон обводки
            bool isOutline =
            (outLines[3] && texCoords.x <= outlineTexel.x) || // Левая сторона обводки
            (outLines[1] && texCoords.x >= maxOutlineCoords.x) || // Правая сторона обводки
            (outLines[0] && texCoords.y <= outlineTexel.y) || // Верхняя сторона обводки
            (outLines[2] && texCoords.y >= maxOutlineCoords.y); // Нижняя сторона обводки

            // Проверяем, находится ли фрагмент на углу обводки
            bool isOutlineCorner =
            (texCoords.x <= minProximity.x && ((corners[0] && texCoords.y <= minProximity.y && outLines[0]) ||
            (corners[3] && texCoords.y >= maxOutlineCarveCoords.y && outLines[2]))) ||
            (texCoords.x >= maxOutlineCarveCoords.x && ((corners[1] && texCoords.y <= minProximity.y && outLines[0]) ||
            (corners[2] && texCoords.y >= maxOutlineCarveCoords.y && outLines[2])));

            // Если фрагмент находится на обводке или углу, применяем градиент обводки
            if (isOutline || isOutlineCorner) {
                // Вычисляем начало и конец градиента в зависимости от outlineGradientHeight
                float gradientStartY = 1.0 - outlineGradientHeight; // Начало градиента снизу
                float gradientEndY = 1.0; // Конец градиента сверху

                // Вычисляем фактор градиента в зависимости от вертикальной текстурной координаты
                float gradientFactor = (texCoords.y - gradientStartY) / (gradientEndY - gradientStartY);
                gradientFactor = clamp(gradientFactor, 0.0, 1.0); // Ограничиваем фактор от 0 до 1

                // Если цвет градиента "нулевой", используем только основной цвет обводки
                if (isNullColor(outlineGradientColor)) {
                    gl_FragColor = outlineColor;
                } else {
                    // Иначе смешиваем основной цвет обводки и градиентный цвет
                    gl_FragColor = mix(outlineColor, outlineGradientColor, gradientFactor);
                }
            }
        }
    }
}
