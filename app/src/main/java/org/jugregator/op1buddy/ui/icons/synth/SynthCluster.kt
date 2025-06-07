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

val Icons.Synth.Cluster: ImageVector
    get() {
        if (_Cluster != null) {
            return _Cluster!!
        }
        _Cluster = ImageVector.Builder(
            name = "Synth.SynthCluster",
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
                    moveTo(1.04f, 8f)
                    horizontalLineTo(3.1f)
                }
                path(
                    stroke = SolidColor(Color(0xFF36597D)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(12.88f, 8f)
                    horizontalLineTo(14.94f)
                }
                path(
                    stroke = SolidColor(Color(0xFF36597D)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(8f, 1.04f)
                    verticalLineTo(3.1f)
                }
                path(
                    stroke = SolidColor(Color(0xFF36597D)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(8f, 12.94f)
                    verticalLineTo(15f)
                }
                path(
                    stroke = SolidColor(Color(0xFF36597D)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(3.17f, 3.46f)
                    lineTo(8.09f, 8.08f)
                }
                path(fill = SolidColor(Color(0xFF36597D))) {
                    moveTo(7.99f, 10.23f)
                    curveTo(9.222f, 10.23f, 10.22f, 9.232f, 10.22f, 8f)
                    curveTo(10.22f, 6.768f, 9.222f, 5.77f, 7.99f, 5.77f)
                    curveTo(6.758f, 5.77f, 5.76f, 6.768f, 5.76f, 8f)
                    curveTo(5.76f, 9.232f, 6.758f, 10.23f, 7.99f, 10.23f)
                    close()
                }
            }
        }.build()

        return _Cluster!!
    }

@Suppress("ObjectPropertyName")
private var _Cluster: ImageVector? = null
