package org.jugregator.op1buddy.ui.icons.synth

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.Synth.Sampler: ImageVector
    get() {
        if (_Sampler != null) {
            return _Sampler!!
        }
        _Sampler = ImageVector.Builder(
            name = "Synth.SynthSampler",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF36597D)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(1.49f, 8.7f)
                curveTo(1.49f, 8.7f, 1.59f, 2f, 2.81f, 2f)
                curveTo(5.21f, 2f, 3.39f, 6.53f, 5.39f, 6.53f)
                curveTo(7.39f, 6.53f, 5.97f, 4.53f, 7.99f, 4.53f)
                curveTo(10.25f, 4.53f, 6.99f, 13.89f, 10.31f, 13.89f)
                curveTo(13.17f, 13.89f, 10.51f, 5.4f, 12.72f, 5.4f)
                curveTo(14.93f, 5.4f, 14.51f, 8.72f, 14.51f, 8.72f)
            }
            path(fill = SolidColor(Color(0xFF36597D))) {
                moveTo(1.49f, 10.32f)
                curveTo(2.313f, 10.32f, 2.98f, 9.653f, 2.98f, 8.83f)
                curveTo(2.98f, 8.007f, 2.313f, 7.34f, 1.49f, 7.34f)
                curveTo(0.667f, 7.34f, 0f, 8.007f, 0f, 8.83f)
                curveTo(0f, 9.653f, 0.667f, 10.32f, 1.49f, 10.32f)
                close()
            }
            path(fill = SolidColor(Color(0xFF36597D))) {
                moveTo(14.51f, 10.32f)
                curveTo(15.333f, 10.32f, 16f, 9.653f, 16f, 8.83f)
                curveTo(16f, 8.007f, 15.333f, 7.34f, 14.51f, 7.34f)
                curveTo(13.687f, 7.34f, 13.02f, 8.007f, 13.02f, 8.83f)
                curveTo(13.02f, 9.653f, 13.687f, 10.32f, 14.51f, 10.32f)
                close()
            }
        }.build()

        return _Sampler!!
    }

@Suppress("ObjectPropertyName")
private var _Sampler: ImageVector? = null
