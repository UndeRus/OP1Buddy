package org.jugregator.op1buddy.ui.icons.synth

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.Synth.String: ImageVector
    get() {
        if (_String != null) {
            return _String!!
        }
        _String = ImageVector.Builder(
            name = "Synth.SynthString",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF36597D)),
                strokeLineWidth = 1f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(8f, 13.03f)
                curveTo(10.85f, 13.03f, 13.16f, 10.778f, 13.16f, 8f)
                curveTo(13.16f, 5.222f, 10.85f, 2.97f, 8f, 2.97f)
                curveTo(5.15f, 2.97f, 2.84f, 5.222f, 2.84f, 8f)
                curveTo(2.84f, 10.778f, 5.15f, 13.03f, 8f, 13.03f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF36597D)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(5.36f, 1.01f)
                verticalLineTo(14.99f)
            }
            path(
                stroke = SolidColor(Color(0xFF36597D)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(8f, 1.01f)
                verticalLineTo(14.99f)
            }
            path(
                stroke = SolidColor(Color(0xFF36597D)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(10.64f, 1.01f)
                verticalLineTo(14.99f)
            }
        }.build()

        return _String!!
    }

@Suppress("ObjectPropertyName")
private var _String: ImageVector? = null
