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

val Icons.Synth.Drwave: ImageVector
    get() {
        if (_Drwave != null) {
            return _Drwave!!
        }
        _Drwave = ImageVector.Builder(
            name = "Synth.SynthDrwave",
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
                    moveTo(6.31f, 1.01f)
                    lineTo(4.6f, 4.84f)
                }
                path(
                    stroke = SolidColor(Color(0xFF36597D)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(5.08f, 7.17f)
                    lineTo(2.87f, 8.83f)
                }
                path(
                    stroke = SolidColor(Color(0xFF36597D)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(10.61f, 1.01f)
                    lineTo(8.9f, 4.84f)
                }
                path(
                    stroke = SolidColor(Color(0xFF36597D)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(9.38f, 7.17f)
                    lineTo(7.17f, 8.83f)
                }
                path(
                    stroke = SolidColor(Color(0xFF36597D)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(14.79f, 1.01f)
                    lineTo(13.08f, 4.84f)
                }
                path(
                    stroke = SolidColor(Color(0xFF36597D)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(2.92f, 11.16f)
                    lineTo(1.21f, 14.99f)
                }
                path(
                    stroke = SolidColor(Color(0xFF36597D)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(7.22f, 11.16f)
                    lineTo(5.51f, 14.99f)
                }
                path(
                    stroke = SolidColor(Color(0xFF36597D)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(11.4f, 11.16f)
                    lineTo(9.69f, 14.99f)
                }
                path(
                    stroke = SolidColor(Color(0xFF36597D)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(13.56f, 7.17f)
                    lineTo(11.35f, 8.83f)
                }
            }
        }.build()

        return _Drwave!!
    }

@Suppress("ObjectPropertyName")
private var _Drwave: ImageVector? = null
