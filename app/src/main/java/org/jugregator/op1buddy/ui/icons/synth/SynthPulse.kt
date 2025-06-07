package org.jugregator.op1buddy.ui.icons.synth

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.Synth.Pulse: ImageVector
    get() {
        if (_Pulse != null) {
            return _Pulse!!
        }
        _Pulse = ImageVector.Builder(
            name = "Synth.SynthPulse",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(0f, 0f)
                    horizontalLineToRelative(16f)
                    verticalLineToRelative(16f)
                    horizontalLineToRelative(-16f)
                    close()
                }
            ) {
                path(
                    stroke = SolidColor(Color(0xFF36597D)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Square
                ) {
                    moveTo(1.03f, 11.5f)
                    horizontalLineTo(1.53f)
                    verticalLineTo(7.41f)
                    horizontalLineTo(4.17f)
                    verticalLineTo(11.5f)
                    horizontalLineTo(6.79f)
                    verticalLineTo(4.5f)
                    horizontalLineTo(9.36f)
                    verticalLineTo(11.5f)
                    horizontalLineTo(12f)
                    verticalLineTo(7.41f)
                    horizontalLineTo(14.69f)
                    verticalLineTo(11.5f)
                    horizontalLineTo(15.08f)
                }
            }
        }.build()

        return _Pulse!!
    }

@Suppress("ObjectPropertyName")
private var _Pulse: ImageVector? = null
