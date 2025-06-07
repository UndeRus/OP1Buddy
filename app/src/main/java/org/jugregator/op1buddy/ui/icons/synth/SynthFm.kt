package org.jugregator.op1buddy.ui.icons.synth

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.Synth.Fm: ImageVector
    get() {
        if (_Fm != null) {
            return _Fm!!
        }
        _Fm = ImageVector.Builder(
            name = "Synth.SynthFm",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF36597D)),
                strokeLineWidth = 2f
            ) {
                moveTo(1.01f, 13.24f)
                verticalLineTo(2.76f)
                horizontalLineTo(5.13f)
            }
            path(
                stroke = SolidColor(Color(0xFF36597D)),
                strokeLineWidth = 2f
            ) {
                moveTo(4.37f, 7.25f)
                horizontalLineTo(1.06f)
            }
            path(
                stroke = SolidColor(Color(0xFF36597D)),
                strokeLineWidth = 2f
            ) {
                moveTo(7.48f, 13.24f)
                lineTo(8.1f, 2.76f)
                horizontalLineTo(8.97f)
                lineTo(11f, 10.35f)
                horizontalLineTo(11.39f)
                lineTo(13.6f, 2.76f)
                horizontalLineTo(14.63f)
                lineTo(14.99f, 13.24f)
            }
        }.build()

        return _Fm!!
    }

@Suppress("ObjectPropertyName")
private var _Fm: ImageVector? = null
