package org.jugregator.op1buddy.ui.icons.synth

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.ui.icons.Icons

val Icons.Synth.Dsynth: ImageVector
    get() {
        if (_Dsynth != null) {
            return _Dsynth!!
        }
        _Dsynth = ImageVector.Builder(
            name = "Synth.SynthDsynth",
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
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(8f, 11.72f)
                    curveTo(10.955f, 11.72f, 13.35f, 9.325f, 13.35f, 6.37f)
                    curveTo(13.35f, 3.415f, 10.955f, 1.02f, 8f, 1.02f)
                    curveTo(5.045f, 1.02f, 2.65f, 3.415f, 2.65f, 6.37f)
                    curveTo(2.65f, 9.325f, 5.045f, 11.72f, 8f, 11.72f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF36597D))) {
                    moveTo(8f, 8.61f)
                    curveTo(9.237f, 8.61f, 10.24f, 7.607f, 10.24f, 6.37f)
                    curveTo(10.24f, 5.133f, 9.237f, 4.13f, 8f, 4.13f)
                    curveTo(6.763f, 4.13f, 5.76f, 5.133f, 5.76f, 6.37f)
                    curveTo(5.76f, 7.607f, 6.763f, 8.61f, 8f, 8.61f)
                    close()
                }
                path(
                    stroke = SolidColor(Color(0xFF36597D)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(8f, 6.37f)
                    verticalLineTo(14.98f)
                }
            }
        }.build()

        return _Dsynth!!
    }

@Suppress("ObjectPropertyName")
private var _Dsynth: ImageVector? = null
