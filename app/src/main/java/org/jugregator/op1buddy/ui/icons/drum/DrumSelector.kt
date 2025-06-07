package org.jugregator.op1buddy.ui.icons.drum

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.Drum.DrumSelector: ImageVector
    get() {
        if (_DrumSelector != null) {
            return _DrumSelector!!
        }
        _DrumSelector = ImageVector.Builder(
            name = "Drum.DrumSelector",
            defaultWidth = 29.dp,
            defaultHeight = 36.dp,
            viewportWidth = 29f,
            viewportHeight = 36f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF1C1C1C)),
                strokeLineWidth = 3f,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(14.699f, 28.553f)
                curveTo(21.713f, 28.553f, 27.399f, 22.609f, 27.399f, 15.276f)
                curveTo(27.399f, 7.944f, 21.713f, 2f, 14.699f, 2f)
                curveTo(7.686f, 2f, 2f, 7.944f, 2f, 15.276f)
                curveTo(2f, 22.609f, 7.686f, 28.553f, 14.699f, 28.553f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF1C1C1C)),
                strokeLineWidth = 3f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(14.699f, 14.553f)
                curveTo(13.078f, 14.553f, 11.997f, 15.84f, 11.997f, 17.535f)
                curveTo(11.997f, 19.23f, 13.348f, 20.36f, 14.699f, 20.36f)
                curveTo(16.32f, 20.36f, 17.401f, 18.947f, 17.401f, 17.535f)
                curveTo(17.401f, 16.123f, 16.116f, 14.554f, 14.699f, 14.553f)
                close()
                moveTo(14.699f, 14.553f)
                verticalLineTo(34.484f)
            }
        }.build()

        return _DrumSelector!!
    }

@Suppress("ObjectPropertyName")
private var _DrumSelector: ImageVector? = null
