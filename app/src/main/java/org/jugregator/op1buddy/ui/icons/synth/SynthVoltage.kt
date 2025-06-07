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

val Icons.Synth.Voltage: ImageVector
    get() {
        if (_Voltage != null) {
            return _Voltage!!
        }
        _Voltage = ImageVector.Builder(
            name = "Synth.SynthVoltage",
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
                    moveTo(1f, 9.09f)
                    horizontalLineTo(2.38f)
                    lineTo(3.36f, 6.33f)
                    lineTo(5.59f, 9.58f)
                    lineTo(7.62f, 4.39f)
                    lineTo(9.71f, 13.61f)
                    lineTo(12.17f, 6.67f)
                    lineTo(13.73f, 9.09f)
                    horizontalLineTo(15f)
                }
                path(fill = SolidColor(Color(0xFF36597D))) {
                    moveTo(15.62f, 4.35f)
                    horizontalLineTo(14.34f)
                    curveTo(14.251f, 4.335f, 14.17f, 4.29f, 14.112f, 4.221f)
                    curveTo(14.054f, 4.152f, 14.021f, 4.065f, 14.021f, 3.975f)
                    curveTo(14.021f, 3.885f, 14.054f, 3.798f, 14.112f, 3.729f)
                    curveTo(14.17f, 3.66f, 14.251f, 3.615f, 14.34f, 3.6f)
                    horizontalLineTo(15.62f)
                    curveTo(15.674f, 3.591f, 15.73f, 3.594f, 15.783f, 3.609f)
                    curveTo(15.836f, 3.624f, 15.885f, 3.65f, 15.927f, 3.685f)
                    curveTo(15.969f, 3.721f, 16.003f, 3.765f, 16.026f, 3.815f)
                    curveTo(16.049f, 3.865f, 16.062f, 3.92f, 16.062f, 3.975f)
                    curveTo(16.062f, 4.03f, 16.049f, 4.085f, 16.026f, 4.135f)
                    curveTo(16.003f, 4.185f, 15.969f, 4.229f, 15.927f, 4.265f)
                    curveTo(15.885f, 4.3f, 15.836f, 4.326f, 15.783f, 4.341f)
                    curveTo(15.73f, 4.356f, 15.674f, 4.359f, 15.62f, 4.35f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF36597D))) {
                    moveTo(1.66f, 4.35f)
                    horizontalLineTo(0.38f)
                    curveTo(0.326f, 4.359f, 0.27f, 4.356f, 0.217f, 4.341f)
                    curveTo(0.164f, 4.326f, 0.115f, 4.3f, 0.073f, 4.265f)
                    curveTo(0.031f, 4.229f, -0.003f, 4.185f, -0.026f, 4.135f)
                    curveTo(-0.049f, 4.085f, -0.061f, 4.03f, -0.061f, 3.975f)
                    curveTo(-0.061f, 3.92f, -0.049f, 3.865f, -0.026f, 3.815f)
                    curveTo(-0.003f, 3.765f, 0.031f, 3.721f, 0.073f, 3.685f)
                    curveTo(0.115f, 3.65f, 0.164f, 3.624f, 0.217f, 3.609f)
                    curveTo(0.27f, 3.594f, 0.326f, 3.591f, 0.38f, 3.6f)
                    horizontalLineTo(1.66f)
                    curveTo(1.749f, 3.615f, 1.83f, 3.66f, 1.888f, 3.729f)
                    curveTo(1.947f, 3.798f, 1.979f, 3.885f, 1.979f, 3.975f)
                    curveTo(1.979f, 4.065f, 1.947f, 4.152f, 1.888f, 4.221f)
                    curveTo(1.83f, 4.29f, 1.749f, 4.335f, 1.66f, 4.35f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF36597D))) {
                    moveTo(1f, 5f)
                    curveTo(0.901f, 5f, 0.806f, 4.961f, 0.735f, 4.892f)
                    curveTo(0.664f, 4.823f, 0.623f, 4.729f, 0.62f, 4.63f)
                    verticalLineTo(3.33f)
                    curveTo(0.635f, 3.241f, 0.68f, 3.16f, 0.749f, 3.102f)
                    curveTo(0.818f, 3.043f, 0.905f, 3.011f, 0.995f, 3.011f)
                    curveTo(1.085f, 3.011f, 1.172f, 3.043f, 1.241f, 3.102f)
                    curveTo(1.31f, 3.16f, 1.355f, 3.241f, 1.37f, 3.33f)
                    verticalLineTo(4.62f)
                    curveTo(1.37f, 4.719f, 1.331f, 4.814f, 1.262f, 4.885f)
                    curveTo(1.193f, 4.956f, 1.099f, 4.997f, 1f, 5f)
                    close()
                }
            }
        }.build()

        return _Voltage!!
    }

@Suppress("ObjectPropertyName")
private var _Voltage: ImageVector? = null
